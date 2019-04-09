package com.aaronzadev.controller;

import com.aaronzadev.model.dao.RefOperatorDAO;
import com.aaronzadev.model.dao.RefillDAO;
import com.aaronzadev.model.implementation.RefOperatorDAOImp;
import com.aaronzadev.model.implementation.RefillDAOImp;
import com.aaronzadev.model.pojo.RefOperator;
import com.aaronzadev.model.pojo.Refill;
import com.aaronzadev.model.pojo.User;
import com.aaronzadev.util.*;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
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
import javafx.scene.control.TextField;


public class RefillController implements Initializable, EventHandler<ActionEvent> {

    @FXML private TextField txtAmount, txtCommission, txtReference, txtObs, txtPhoneNum;

    @FXML private ComboBox<RefOperator> cbOps;
    
    @FXML private JFXButton btnRefill;
    
    private Executor exec;
    private Alert alert;
    private RefillDAO rdao;
    private User user;
    private Optional<ButtonType> option;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btnRefill.setOnAction(this);

        DataValidator.setFloatValidator(txtAmount);
        DataValidator.setFloatValidator(txtCommission);
        DataValidator.setIntegerValidator(txtPhoneNum);
        DataValidator.setMaxSizeValidator(txtReference, 30);
        DataValidator.setMaxSizeValidator(txtObs, 100);

        user = SessionManager.getCurrentSession(null).getCurrentUser();

        exec = MExecutor.getExecutor();
        rdao = new RefillDAOImp(DbConnection.getConnection());
        
        fillCombo();
        
    } 
    
    @Override
    public void handle(ActionEvent event) {
        
        if (cbOps.getSelectionModel().getSelectedItem() != null && !txtAmount.getText().isEmpty() && !txtCommission.getText().isEmpty()
                && !txtReference.getText().isEmpty() && !txtPhoneNum.getText().isEmpty()) {

            alert = AlertMaker.GetWarnAlert("Esta seguro de registrar esta recarga?");
            option = alert.showAndWait();

            if (option.get() == ButtonType.OK) {

                Refill ref = new Refill();
                ref.setUserID(user.getUserID());
                ref.setOperatorID(cbOps.getSelectionModel().getSelectedItem().getOperatorID());
                ref.setAmount(Float.parseFloat(txtAmount.getText()));
                ref.setCommission(Float.parseFloat(txtCommission.getText()));
                ref.setPhoneNum(txtPhoneNum.getText());
                ref.setReference(txtReference.getText());
                ref.setObs(txtObs.getText());
                
                addRefill(ref);
                
            }

        } else {

            alert = AlertMaker.GetErrorAlert("Asegurese de introducir la informacion completa");
            alert.showAndWait();

        }
        
    }
    
    private void addRefill (Refill p){
    
        Task<Integer> tAddRef = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return rdao.Insert(p);
            }
        };
        
        tAddRef.setOnSucceeded(e ->{
             if(tAddRef.getValue() > 0){
               
               cleanFields();
                 
               alert = AlertMaker.GetInfoAlert("Recarga registrada correctamente!");
               alert.showAndWait();
           }
        });
    
        exec.execute(tAddRef);   
    
    }
    
    private void fillCombo (){    
        
        Task<ObservableList<RefOperator>> tOps = new Task<ObservableList<RefOperator>>() {
            @Override
            protected ObservableList<RefOperator> call() throws Exception {
                RefOperatorDAO refdao = new RefOperatorDAOImp(DbConnection.getConnection());
                return FXCollections.observableArrayList(refdao.GetAll());
            }
        };
        
        tOps.setOnSucceeded(e -> {
        
            cbOps.setItems(tOps.getValue());
        
        });       
        
        exec.execute(tOps);  
    
    }
    
    private void cleanFields() {
        
        cbOps.getSelectionModel().clearSelection();
        txtAmount.clear();
        txtCommission.clear();
        txtReference.clear();
        txtPhoneNum.clear();
        txtObs.clear();

    }

    
}
