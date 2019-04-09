package com.aaronzadev.controller;


import com.aaronzadev.model.dao.UserDAO;
import com.aaronzadev.model.implementation.UserDAOImp;
import com.aaronzadev.model.pojo.Role;
import com.aaronzadev.model.pojo.User;
import com.aaronzadev.util.*;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserController implements Initializable, EventHandler<ActionEvent> {

    @FXML private JFXListView<User> lvUsers;

    @FXML private TextField txtNick, txtNames, txtApPat, txtApMat;

    @FXML private PasswordField txtPass;
    
    @FXML private JFXButton btnUpdate, btnAdd, btnClean;
    
    @FXML private ComboBox<Role> cbRoles;
    
    private Optional<ButtonType> option;
    
    private UserDAO udao;
    private Executor exec;
    private Alert alert;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        DataValidator.setMaxSizeValidator(txtNick, 20);
        DataValidator.setMaxSizeValidator(txtPass, 20);
        DataValidator.setMaxSizeValidator(txtNames, 30);
        DataValidator.setMaxSizeValidator(txtApPat, 15);
        DataValidator.setMaxSizeValidator(txtApMat, 15);
        
        btnAdd.setOnAction(this);
        btnUpdate.setOnAction(this);
        btnClean.setOnAction(this);
        
        exec = MExecutor.getExecutor();

        udao = new UserDAOImp(DbConnection.getConnection());

        fillCombo();
        
        setupTableView();
        
        fillList();
        
    }   
    
    private void setupTableView() {
        
        lvUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            
            if(lvUsers.getSelectionModel().getSelectedItem() != null){
            
                txtNick.setText(newValue.getNickName());
                txtNick.setEditable(false);
                txtPass.setText("12345678");
                txtNames.setText(newValue.getFNames());
                txtApPat.setText(newValue.getLPName());
                txtApMat.setText(newValue.getLMName());
                cbRoles.getSelectionModel().select(newValue.getRole()-1);
            
            }            
        });
        
    }
    
    
    @Override
    public void handle(ActionEvent event) {
       
        if (event.getSource() == btnAdd) {

            if (!txtNick.getText().isEmpty() && !txtPass.getText().isEmpty() && !txtNames.getText().isEmpty()
                    && !txtApPat.getText().isEmpty()&& !txtApMat.getText().isEmpty()
                    && cbRoles.getSelectionModel().getSelectedIndex() >= 0) {

                User u = new User();
                u.setNickName(txtNick.getText());
                u.setPassword(txtPass.getText());
                u.setFNames(txtNames.getText());
                u.setLPName(txtApPat.getText());
                u.setLMName(txtApMat.getText());
                u.setRole(cbRoles.getSelectionModel().getSelectedItem().getRoleID());
                
                alert = AlertMaker.GetWarnAlert("Esta seguro de registrar este usuario?, "
                        + "tome en cuenta que el nombre de usuario debe ser unico y posteriormente no podra modificarse.");                
                option = alert.showAndWait();
                
                if(option.get() == ButtonType.OK){
                
                    addUser(u);
                    
                }               

            }
            else {
                
                alert = AlertMaker.GetErrorAlert("Asegurese de introducir la informacion completa");
                alert.showAndWait();           
            }
        
        } else if (event.getSource() == btnUpdate) {
            
            if (!txtPass.getText().isEmpty() && !txtNames.getText().isEmpty()
                    && !txtApPat.getText().isEmpty() && !txtApMat.getText().isEmpty()
                    && cbRoles.getSelectionModel().getSelectedIndex() >= 0) {

                User u = new User();   
                u.setUserID(lvUsers.getSelectionModel().getSelectedItem().getUserID());
                u.setFNames(txtNames.getText());
                u.setLPName(txtApPat.getText());
                u.setLMName(txtApMat.getText());
                u.setRole(cbRoles.getSelectionModel().getSelectedItem().getRoleID());
                
                if (txtPass.getText().equals("12345678")) {

                    alert = AlertMaker.GetWarnAlert("Esta seguro de actualizar la información?, "
                            + "tome en cuenta que el nombre de usuario no puede modificarse");
                    option = alert.showAndWait();

                    if (option.get() == ButtonType.OK) {

                        updateUser(u, txtNick.getText());

                    }

                }
                else {

                    alert = AlertMaker.GetWarnAlert("Esta seguro de actualizar la información?, "
                            + "tome en cuenta que el nombre de usuario no puede modificarse");
                    option = alert.showAndWait();

                    if (option.get() == ButtonType.OK) {

                        u.setPassword(txtPass.getText());
                        updateUserPass(u, txtNick.getText());

                    }

                }

            }
            else {
                
                alert = AlertMaker.GetErrorAlert("Asegurese de introducir la informacion completa");
                alert.showAndWait();           
            }

        } else if (event.getSource() == btnClean) {

            cleanFields();
        }        
        
    }    
    
    private void fillCombo () {

        
        cbRoles.setItems(CBoxManager.getRoles());
    
    }
    
    private void fillList(){
        
        Task<ObservableList<User>> tusers = new Task<ObservableList<User>>() {
            @Override
            protected ObservableList<User> call() throws Exception {
                return FXCollections.observableArrayList(udao.GetAll());
            }
        };
        
        tusers.setOnSucceeded(e -> {
        
            if(tusers.getValue() != null){
            
                lvUsers.setItems(tusers.getValue());
                
            }
        
        });
    
        exec.execute(tusers);
    
    }
    
    private void addUser (User u){
    
        Task<Integer> tinsert = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return udao.Insert(u);
            }
        };
        
        tinsert.setOnSucceeded(e ->{
             if(tinsert.getValue() > 0){
               
               cleanFields();
               fillList();
               alert = AlertMaker.GetInfoAlert("El usuario "+u.getNickName()+" se ha registrado Correctamente!");
               alert.showAndWait();
           }
        });
    
        exec.execute(tinsert);       
    }
    
    private void updateUserPass (User u, String nick){
    
        Task<Integer> tupdate = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return udao.UpdateUserAndPass(u);
            }
        };
        
        tupdate.setOnSucceeded(e ->{
             if(tupdate.getValue() > 0){
               
               cleanFields();                 
               alert = AlertMaker.GetInfoAlert("El usuario "+nick+" se ha actualizado Correctamente!");
               alert.showAndWait();
           }
        });
    
        exec.execute(tupdate);   
    
    }
    
    private void updateUser (User u, String nick){
    
        Task<Integer> tupdate = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return udao.Update(u);
            }
        };
        
        tupdate.setOnSucceeded(e ->{
             if(tupdate.getValue() > 0){
               
               cleanFields();                 
               alert = AlertMaker.GetInfoAlert("El usuario "+nick+" se ha actualizado Correctamente!");
               alert.showAndWait();
           }
        });
    
        exec.execute(tupdate);   
    
    }
    
    private void cleanFields(){
            
        txtNick.clear();
        txtNick.setEditable(true);
        txtPass.clear();
        txtNames.clear();
        txtApPat.clear();
        txtApMat.clear();
        cbRoles.getSelectionModel().clearSelection();
        lvUsers.getSelectionModel().clearSelection();
        
    }

    
    
}
