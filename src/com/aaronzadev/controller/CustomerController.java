package com.aaronzadev.controller;


import com.aaronzadev.model.dao.CustomerDAO;
import com.aaronzadev.model.implementation.CustomerDAOImp;
import com.aaronzadev.model.pojo.Customer;
import com.aaronzadev.util.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;

public class CustomerController implements Initializable, EventHandler<ActionEvent> {

    @FXML private TextField txtNames, txtApm, txtNum, txtPhoneOne, txtApp, txtStreet, txtCol, txtPhoneTwo;

    @FXML private JFXButton btnSearch, btnAdd, btnUpdate, btnClean, btnAll;

    @FXML private ComboBox<Character> cbLetters;

    @FXML private JFXListView<Customer> lvCustomers;

    private Executor executor;
    private Alert alert;
    private CustomerDAO custdao;
    private Customer cCustom;
    private Optional<ButtonType> option;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initUi();
        fillCombo();

        executor = MExecutor.getExecutor();
        custdao = new CustomerDAOImp(DbConnection.getConnection());

    }

    private void initUi() {

        btnAdd.setOnAction(this);
        btnSearch.setOnAction(this);
        btnClean.setOnAction(this);
        btnUpdate.setOnAction(this);
        btnAll.setOnAction(this);


        DataValidator.setMaxSizeValidator(txtNames, 30);
        DataValidator.setMaxSizeValidator(txtApm, 15);
        DataValidator.setMaxSizeValidator(txtPhoneOne, 10);
        DataValidator.setMaxSizeValidator(txtApp, 15);
        DataValidator.setMaxSizeValidator(txtStreet, 45);
        DataValidator.setMaxSizeValidator(txtCol, 50);
        DataValidator.setMaxSizeValidator(txtPhoneTwo, 10);
        DataValidator.setIntegerValidator(txtNum);



        lvCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue != null) {

                txtNames.setText(newValue.getCustFName());
                txtApp.setText(newValue.getCustLPName());
                txtApm.setText(newValue.getCustLMName());
                txtStreet.setText(newValue.getStreet());
                txtNum.setText(String.valueOf(newValue.getNum()));
                txtCol.setText(newValue.getColony());
                txtPhoneOne.setText(newValue.getFPhonNum());
                txtPhoneTwo.setText(newValue.getSPhonNum());

            }
        });

    }

    private void fillCombo () {
        cbLetters.setItems(CBoxManager.getAlphabet());
    }

    @Override
    public void handle(ActionEvent event) {

        if(event.getSource() == btnAdd) {
            //TODO remplazar equals por isEmpty en todas las validaciones...! :D
            if(!txtNames.getText().isEmpty() && !txtApp.getText().isEmpty() && !txtApm.getText().isEmpty() &&
                !txtStreet.getText().isEmpty() && !txtNum.getText().isEmpty() && !txtCol.getText().isEmpty() &&
                !txtPhoneOne.getText().isEmpty() && !txtPhoneTwo.getText().isEmpty()) {

                alert = AlertMaker.GetWarnAlert("Esta seguro de registrar este nuevo cliente?");
                option = alert.showAndWait();

                if(option.get() == ButtonType.OK){

                    Customer c = new Customer();
                    c.setCustFName(txtNames.getText());
                    c.setCustLPName(txtApp.getText());
                    c.setCustLMName(txtApm.getText());
                    c.setStreet(txtStreet.getText());
                    c.setNum(Integer.parseInt(txtNum.getText()));
                    c.setColony(txtCol.getText());
                    c.setFPhonNum(txtPhoneOne.getText());
                    c.setSPhonNum(txtPhoneTwo.getText());

                    addCustomer(c);

                }

            }
            else {

                alert = AlertMaker.GetErrorAlert("Asegurese de introducir la informacion completa");
                alert.showAndWait();
            }

        }
        else if (event.getSource() == btnUpdate){

            if(!txtNames.getText().isEmpty() && !txtApp.getText().isEmpty() && !txtApm.getText().isEmpty() &&
                    !txtStreet.getText().isEmpty() && !txtNum.getText().isEmpty() && !txtCol.getText().isEmpty() &&
                    !txtPhoneOne.getText().isEmpty() && !txtPhoneTwo.getText().isEmpty() && cCustom != null) {

                alert = AlertMaker.GetWarnAlert("Esta seguro que desea actualizar la informacion del cliente?");
                option = alert.showAndWait();

                if(option.get() == ButtonType.OK){

                    Customer c = new Customer();
                    c.setCustId(cCustom.getCustId());
                    c.setCustFName(txtNames.getText());
                    c.setCustLPName(txtApp.getText());
                    c.setCustLMName(txtApm.getText());
                    c.setStreet(txtStreet.getText());
                    c.setNum(Integer.parseInt(txtNum.getText()));
                    c.setColony(txtCol.getText());
                    c.setFPhonNum(txtPhoneOne.getText());
                    c.setSPhonNum(txtPhoneTwo.getText());

                    updateCustomer(c);

                }

            }
            else {

                alert = AlertMaker.GetErrorAlert("Asegurese de introducir la informacion completa");
                alert.showAndWait();
            }


        }
        else if (event.getSource() == btnSearch){

            if (!txtPhoneOne.getText().isEmpty()) {

                searchByPhoneNum(txtPhoneOne.getText());

            } else {

                alert = AlertMaker.GetErrorAlert("Introduzca el número telefonico principal del cliente a buscar");
                alert.showAndWait();

            }


        }
        else if (event.getSource() == btnAll){

            if(cbLetters.getSelectionModel().getSelectedIndex() >= 0){

                searchByLetter(cbLetters.getSelectionModel().getSelectedItem().toString());

            }
            else {

                alert = AlertMaker.GetErrorAlert("Debe seleccionar una letra de la lista desplegable para realizar la busqueda");
                alert.showAndWait();

            }

        }
        else if (event.getSource() == btnClean){

            cleanFields();

        }

    }

    private void searchByPhoneNum(String phone) {

        Task<Customer> tcustom = new Task<Customer>() {
            @Override
            protected Customer call() throws Exception {
                return custdao.GetByPhone(phone);
            }
        };

        tcustom.setOnSucceeded(e ->{

            if (tcustom.getValue() != null) {

                cCustom = tcustom.getValue();

                txtNames.setText(cCustom.getCustFName());
                txtApp.setText(cCustom.getCustLPName());
                txtApm.setText(cCustom.getCustLMName());
                txtStreet.setText(cCustom.getStreet());
                txtNum.setText(String.valueOf(cCustom.getNum()));
                txtCol.setText(cCustom.getColony());
                txtPhoneTwo.setText(cCustom.getSPhonNum());

            } else {

                txtPhoneOne.clear();
                alert = AlertMaker.GetErrorAlert("No se ha encontrado el cliente con N° Telefonico " + phone);
                alert.showAndWait();

            }

        });

        executor.execute(tcustom);

    }

    private void addCustomer (Customer cust){

        Task<Integer> tinsert = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return custdao.Insert(cust);
            }
        };

        tinsert.setOnSucceeded(e ->{
            if(tinsert.getValue() > 0){

                cleanFields();

                alert = AlertMaker.GetInfoAlert("Usuario Registrado Correctamente!");
                alert.showAndWait();
            }
        });

        tinsert.setOnFailed(e -> {

            //TODO Remplazar el bloque try catch por throws exception en la firma de los metodos! :D

            if(tinsert.getException() instanceof SQLIntegrityConstraintViolationException){

                alert = AlertMaker.GetErrorAlert("El numero telefonico principal: "+cust.getFPhonNum()+" ya se encuentra registrado, intentelo con otro número.");
                alert.showAndWait();
            }
            else {

                alert = AlertMaker.GetErrorAlert("Error: "+tinsert.getException().getMessage()+" Intentelo nuevamente, si el problema persiste contacte al administrador del sistema.");
                alert.showAndWait();

            }

        });

        executor.execute(tinsert);

    }


    private void updateCustomer (Customer cust){

        Task<Integer> tupdate = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return custdao.Update(cust);
            }
        };

        tupdate.setOnSucceeded(e ->{
            if(tupdate.getValue() > 0){

                cleanFields();

                alert = AlertMaker.GetInfoAlert("El Usuario se ha Actualizado Correctamente!");
                alert.showAndWait();
            }
        });

        tupdate.setOnFailed(e -> {

            //TODO Remplazar el bloque try catch por throws exception en la firma de los metodos! :D

            if(tupdate.getException() instanceof SQLIntegrityConstraintViolationException){

                alert = AlertMaker.GetErrorAlert("El numero telefonico principal: "+cust.getFPhonNum()+" ya se encuentra registrado, intentelo con otro número.");
                alert.showAndWait();
            }
            else {

                alert = AlertMaker.GetErrorAlert("Error: "+tupdate.getException().getMessage()+" Intentelo nuevamente, si el problema persiste contacte al administrador del sistema.");
                alert.showAndWait();
            }

        });

        executor.execute(tupdate);

    }


    private void searchByLetter (String letter) {

        Task<ObservableList<Customer>> tcustom = new Task<ObservableList<Customer>>() {
            @Override
            protected ObservableList<Customer> call() throws Exception {
                return FXCollections.observableArrayList(custdao.GetAllByLetter(letter));
            }
        };

        tcustom.setOnSucceeded(e ->{

            if (!tcustom.getValue().isEmpty()) {

                lvCustomers.setItems(tcustom.getValue());

            } else {

                cbLetters.getSelectionModel().clearSelection();
                lvCustomers.getItems().clear();
                alert = AlertMaker.GetErrorAlert("No se ha encontrado el clientes con la letra "+letter);
                alert.showAndWait();

            }

        });

        executor.execute(tcustom);

    }

    private void cleanFields(){

        txtNames.clear();
        txtApm.clear();
        txtNum.clear();
        txtPhoneOne.clear();
        txtApp.clear();
        txtStreet.clear();
        txtCol.clear();
        txtPhoneTwo.clear();
        cbLetters.getSelectionModel().clearSelection();
        lvCustomers.getItems().clear();

    }
}
