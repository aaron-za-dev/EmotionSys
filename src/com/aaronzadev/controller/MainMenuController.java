package com.aaronzadev.controller;

//import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class MainMenuController implements Initializable, EventHandler<ActionEvent>{


    @FXML private JFXButton btnNewSale, btnSalesHistory, btnNewRefill, btnRefHistory, 
          btnNewService, btnBackServ, btnHistoryService, btnProducts, btnNewIn,  
          btnInHistory, btnCustom, btnUsers, btnSettings;

    @FXML private Label lblTime;
    
    private Stage stage;

    //TODO check permissions
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        btnNewSale.setOnAction(this);
        btnSalesHistory.setOnAction(this);
        btnNewRefill.setOnAction(this);
        btnRefHistory.setOnAction(this);
        btnNewService.setOnAction(this);
        btnBackServ.setOnAction(this);
        btnHistoryService.setOnAction(this);
        btnProducts.setOnAction(this);
        btnNewIn.setOnAction(this);
        btnInHistory.setOnAction(this);
        btnCustom.setOnAction(this);
        btnUsers.setOnAction(this);
        btnSettings.setOnAction(this);

        initStage();
        
        setRealTime();
        
    }   

    @Override
    public void handle(ActionEvent event) {

        if (event.getSource() == btnNewSale) {
            
             ShowMenu("SalesView.fxml","Nueva Venta");

        } else if (event.getSource() == btnSalesHistory) {
            
            ShowMenu("HistorySalesView.fxml","Detalle de Ventas");

        }        
        else if (event.getSource() == btnNewRefill) {
            
            ShowMenu("RefillView.fxml","Realizar Recarga");

        }
        else if (event.getSource() == btnRefHistory) {
            
            ShowMenu("HistoryRefillView.fxml","Detalle de Recargas");

        }
        else if (event.getSource() == btnNewService) {
            
            ShowMenu("NTSupportView.fxml","Nueva Orden de Servicio Tecnico");

        }
        else if (event.getSource() == btnBackServ) {
            
            ShowMenu("RTSupportView.fxml","Devolucion de equipo");

        }
        else if (event.getSource() == btnHistoryService) {
            
            ShowMenu("HTSupportView.fxml","Historial de Servicios");

        }
        else if (event.getSource() == btnProducts) {
            
            ShowMenu("ProductsView.fxml","Administrar Productos");

        }
        else if (event.getSource() == btnNewIn) {
            
            ShowMenu("InProductView.fxml","Entrada de Productos");            
             
        }
        else if (event.getSource() == btnInHistory){
            
            ShowMenu("HInProductView.fxml","Detalle de Entradas de Producto"); 
                    
        }
        else if (event.getSource() == btnCustom){

            ShowMenu("CustomerView.fxml","Administrar Clientes");

        }
        else if (event.getSource() == btnUsers){
            
             ShowMenu("UserView.fxml","Administrar Usuarios");
        
        }
        else if (event.getSource() == btnSettings){
            
            ShowMenu("SettingsView.fxml","Configuracion");     
            
        }
    } 
    
    private void setRealTime() {

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateFormat dff = new SimpleDateFormat("dd 'de' MMMM 'de' YYYY',' hh:mm a", new Locale("es"));
            lblTime.setText(dff.format(new Date()));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    
    }

    private void initStage(){

        stage = new Stage();
        stage.setResizable(false);
        stage.getIcons().add(new Image("/com/aaronzadev/view/icons/icon64.png"));
        stage.getIcons().add(new Image("/com/aaronzadev/view/icons/icon16.png"));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);

    }

    private void ShowMenu(String fxmlName, String title) {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("/com/aaronzadev/view/fxml/" +fxmlName));
            stage.setTitle("EmotionSys 1.0 - "+title);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            //TODO new FadeIn(root).play(); add close operation
        }catch (IOException e) {
            e.printStackTrace();
        }

    }    
    
}
