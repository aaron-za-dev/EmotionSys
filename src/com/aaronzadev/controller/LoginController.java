package com.aaronzadev.controller;


import com.aaronzadev.model.dao.UserDAO;
import com.aaronzadev.model.implementation.UserDAOImp;
import com.aaronzadev.model.pojo.User;
import com.aaronzadev.util.DbConnection;
import com.aaronzadev.util.MExecutor;
import com.aaronzadev.util.SessionManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;

import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginController implements Initializable, EventHandler<ActionEvent> {

    @FXML private TextField txtUsername;

    @FXML private PasswordField txtPassword;

    @FXML private JFXButton btnLogin;

    @FXML private JFXProgressBar pbLogin;

    @FXML private Label lblLogin;
    
    private Executor executor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btnLogin.setOnAction(this);

        executor = MExecutor.getExecutor();

    }    

    @Override
    public void handle(ActionEvent event) {
        
        if(!txtUsername.getText().equals("") && !txtPassword.getText().equals("")){
            
            DoLogin(txtUsername.getText(), txtPassword.getText(), event);
        
        }
        else{

            lblLogin.setText("Debe de introducir un usuario y contraseña validos.");
            lblLogin.setVisible(true);
            
        }
        
    }
    
    private void DoLogin(String nick, String pass, ActionEvent evt){
        
        showProgress(true);

        Task<User> task = new Task<User>() {

            @Override
            protected User call() throws Exception {

                Thread.sleep(3000);
                UserDAO user = new UserDAOImp(DbConnection.getConnection());
                return user.getByNickAndPass(nick, pass);
            }

        };

        task.setOnSucceeded(e -> {
            if (task.getValue() != null) {
                if (task.getValue().getRole() == 0) {
                    showProgress(false);
                    lblLogin.setText("Usuario bloqueado, contacte con el administrador del sistema");
                } else {
                    SessionManager.getCurrentSession(task.getValue());
                    LaunchMainMenu(evt);
                }
            } else {
                showProgress(false);
                lblLogin.setText("Usuario no encontrado, asegurese que la informacion proporcionada es correcta!");
            }
        });

        executor.execute(task);
    
    }
    
    private void showProgress (boolean flag){

        if(flag){

            btnLogin.setDisable(true);
            lblLogin.setVisible(true);
            lblLogin.setText("Iniciando Sesión, espere un momento...");
            pbLogin.setVisible(true);
        }
        else {

            btnLogin.setDisable(false);
            pbLogin.setVisible(false);

        }
    }
    
    private void LaunchMainMenu (Event e){

        try {
            
            //Parent root = FXMLLoader.load(getClass().getResource("/com/aaronzadev/view/fxml/MainMenuView.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/com/aaronzadev/view/fxml/TechPanelView.fxml"));
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.setTitle("EmotionSys 1.0 - Menú Principal");
            stage.getIcons().add(new Image("/com/aaronzadev/view/icons/icon64.png"));
            stage.getIcons().add(new Image("/com/aaronzadev/view/icons/icon16.png"));
            stage.setScene(new Scene(root));
            stage.show();
            // TODO agregr para cerrar todos los menús
            ((Node)(e.getSource())).getScene().getWindow().hide();
        }catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
