package com.aaronzadev.controller;

import com.aaronzadev.model.dao.CashOperationDAO;
import com.aaronzadev.model.implementation.CashOperationDAOImp;
import com.aaronzadev.model.pojo.CashOperation;
import com.aaronzadev.model.pojo.CashOperationType;
import com.aaronzadev.util.*;
import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;

public class CashDialogController implements Initializable, EventHandler<ActionEvent> {

    @FXML private ComboBox<CashOperationType> cbOps;

    @FXML private JFXButton btnSave;

    @FXML private TextField txtAmount, txtObs;

    private Executor executor;
    private Alert alert;

    private CashOperation operation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnSave.setOnAction(this);
        DataValidator.setMaxSizeValidator(txtObs, 150);

        executor = MExecutor.getExecutor();

        fillCombo();

    }

    @Override
    public void handle(ActionEvent event) {

        String reference = cbOps.getSelectionModel().getSelectedItem().getPrefixName() + operation.getReference();
        operation.setReference(reference);
        operation.setObsOp(txtObs.getText());

        addCash(operation, event);

    }

    private void fillCombo(){

        cbOps.setItems(CBoxManager.getOpsType());

    }

    public void initValues (CashOperation cshOp){

       operation = cshOp;

       txtAmount.setText(String.valueOf(cshOp.getAmount()));
       cbOps.getSelectionModel().select(cshOp.getTypeOperation() == 1 ? 1:2);

    }

    private void addCash(CashOperation cashOp, ActionEvent ev){

        Task<Integer> tadd = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                CashOperationDAO cod = new CashOperationDAOImp(DbConnection.getConnection());
                return cod.Insert(cashOp);
            }
        };

        tadd.setOnSucceeded(e ->{
            if(tadd.getValue() > 0){

                alert = AlertMaker.GetDoneAlert("La Venta se ha registrado correctamente");
                alert.showAndWait();

                closeStage(ev);

            }
            //TODO else....
        });

        tadd.setOnFailed(e-> {

            e.getSource().getException().printStackTrace();

        });

        executor.execute(tadd);

    }

    private void closeStage (ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

}
