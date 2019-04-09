package com.aaronzadev.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;

import com.aaronzadev.model.dao.RefOperatorDAO;
import com.aaronzadev.model.dao.RefillDAO;
import com.aaronzadev.model.dao.UserDAO;
import com.aaronzadev.model.implementation.RefOperatorDAOImp;
import com.aaronzadev.model.implementation.RefillDAOImp;
import com.aaronzadev.model.implementation.UserDAOImp;
import com.aaronzadev.model.pojo.RefOperator;
import com.aaronzadev.model.pojo.Refill;
import com.aaronzadev.model.pojo.User;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

public class HistoryRefillController implements Initializable, EventHandler<ActionEvent> {

    @FXML private ComboBox<RefOperator> cbOps;

    @FXML private ComboBox<String> cbOne;

    @FXML private ComboBox<User> cbUsers;

    @FXML private JFXListView<Refill> lvRefills;

    @FXML private JFXButton btnSearchOne, btnSearchTwo, btnSearchThree, btnExport;

    private Executor exec;
    private RefillDAO rdao;
    private Alert alert;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO implementar la opcion de guardar reportes

        initUi();

        exec = MExecutor.getExecutor();
        rdao = new RefillDAOImp(DbConnection.getConnection());

        fillCombos();

    }

    private void initUi () {

        btnSearchOne.setOnAction(this);
        btnSearchTwo.setOnAction(this);
        btnSearchThree.setOnAction(this);
        btnExport.setOnAction(this);

        lvRefills.setCellFactory(cell -> new ListCellRefill());

    }

    @Override
    public void handle(ActionEvent event) {

        if(event.getSource() == btnSearchOne){

            if(cbOne.getSelectionModel().getSelectedIndex() == 0) {

                getAllRefillsToday();

            }
            else if (cbOne.getSelectionModel().getSelectedIndex() == 1){

                getAllRefillsOfWeek();

            }
            else if (cbOne.getSelectionModel().getSelectedIndex() == 2){

                getAllRefillsOfMonth();

            }
            else{

                alert = AlertMaker.GetErrorAlert("Debe seleccionar un perido de tiempo para realizar la busqueda");
                alert.showAndWait();

            }
        }
        else if (event.getSource() == btnSearchTwo){

            if(cbOps.getSelectionModel().getSelectedIndex() >= 0 ){

                // TODO revisar limpiar la seleccion de los demas combos
                //cbOne.getSelectionModel().clearSelection();
                //cbUsers.getSelectionModel().clearSelection();

                getAllRefillsByOperator(cbOps.getSelectionModel().getSelectedItem().getOperatorID());

            }
            else {

                alert = AlertMaker.GetErrorAlert("Debe seleccionar un Operador movil para realizar la busqueda");
                alert.showAndWait();

            }

        }
        else if (event.getSource() == btnSearchThree){

            if(cbUsers.getSelectionModel().getSelectedIndex() >= 0){

                getAllRefillsByUser(cbUsers.getSelectionModel().getSelectedItem().getUserID());

            }
            else{

                alert = AlertMaker.GetErrorAlert("Debe seleccionar un Usuario para realizar la busqueda");
                alert.showAndWait();

            }

        }

    }


    private void fillCombos (){

        cbOne.setItems(CBoxManager.getMethods());

        Task<ObservableList<RefOperator>> tOps = new Task<ObservableList<RefOperator>>() {
            @Override
            protected ObservableList<RefOperator> call() throws Exception {
                RefOperatorDAO refdao = new RefOperatorDAOImp(DbConnection.getConnection());
                return FXCollections.observableArrayList(refdao.GetAll());
            }
        };

        tOps.setOnSucceeded(e -> {

            if(!tOps.getValue().isEmpty()){

               cbOps.setItems(tOps.getValue());

            }

        });

        Task<ObservableList<User>> tusers = new Task<ObservableList<User>>() {
            @Override
            protected ObservableList<User> call() throws Exception {
                UserDAO udao = new UserDAOImp(DbConnection.getConnection());
                return FXCollections.observableArrayList(udao.GetAll());
            }
        };

        tusers.setOnSucceeded(e -> {

            if(!tusers.getValue().isEmpty()){

                cbUsers.setItems(tusers.getValue());

            }

        });

        exec.execute(tusers);

        exec.execute(tOps);

    }

    private void getAllRefillsToday() {

        Task<ObservableList<Refill>> task = new Task<ObservableList<Refill>>() {
            @Override
            protected ObservableList<Refill> call() throws Exception {
                return FXCollections.observableArrayList(rdao.GetAllOfToday());
            }
        };

        task.setOnSucceeded(e -> {

            if(!task.getValue().isEmpty()) {

                lvRefills.setItems(task.getValue());
                alert = AlertMaker.GetInfoAlert("Se han encontrado "+task.getValue().size()+" resultados");
                alert.showAndWait();

            }
            else {

                lvRefills.getItems().clear();
                alert = AlertMaker.GetErrorAlert("No se han encontrado recargas del dia de hoy");
                alert.showAndWait();

            }
        });

        exec.execute(task);

    }


    private void getAllRefillsOfWeek() {

        Task<ObservableList<Refill>> task = new Task<ObservableList<Refill>>() {
            @Override
            protected ObservableList<Refill> call() throws Exception {
                return FXCollections.observableArrayList(rdao.GetAllOfWeek());
            }
        };

        task.setOnSucceeded(e -> {

            if(!task.getValue().isEmpty()) {

                lvRefills.setItems(task.getValue());
                alert = AlertMaker.GetInfoAlert("Se han encontrado "+task.getValue().size()+" resultados");
                alert.showAndWait();

            }
            else {

                lvRefills.getItems().clear();
                alert = AlertMaker.GetErrorAlert("No se han encontrado recargas en esta semana");
                alert.showAndWait();

            }
        });

        exec.execute(task);

    }


    private void getAllRefillsOfMonth() {

        Task<ObservableList<Refill>> task = new Task<ObservableList<Refill>>() {
            @Override
            protected ObservableList<Refill> call() throws Exception {
                return FXCollections.observableArrayList(rdao.GetAllOfMonth());
            }
        };

        task.setOnSucceeded(e -> {

            if(!task.getValue().isEmpty()) {

                lvRefills.setItems(task.getValue());
                alert = AlertMaker.GetInfoAlert("Se han encontrado "+task.getValue().size()+" resultados");
                alert.showAndWait();

            }
            else {

                lvRefills.getItems().clear();
                alert = AlertMaker.GetErrorAlert("No se han encontrado recargas este mes");
                alert.showAndWait();

            }
        });

        exec.execute(task);

    }

    private void getAllRefillsByUser(int userID) {

        Task<ObservableList<Refill>> task = new Task<ObservableList<Refill>>() {
            @Override
            protected ObservableList<Refill> call() throws Exception {
                return FXCollections.observableArrayList(rdao.GetAllByUser((short) userID));
            }
        };

        task.setOnSucceeded(e -> {

            if(!task.getValue().isEmpty()) {

                lvRefills.setItems(task.getValue());
                alert = AlertMaker.GetInfoAlert("Se han encontrado "+task.getValue().size()+" resultados");
                alert.showAndWait();

            }
            else {

                lvRefills.getItems().clear();
                alert = AlertMaker.GetErrorAlert("Este usuario no ha realizado recargas");
                alert.showAndWait();

            }
        });

        exec.execute(task);


    }

    private void getAllRefillsByOperator(short operatorID) {

        Task<ObservableList<Refill>> task = new Task<ObservableList<Refill>>() {
            @Override
            protected ObservableList<Refill> call() throws Exception {
                return FXCollections.observableArrayList(rdao.GetAllByOperator(operatorID));
            }
        };

        task.setOnSucceeded(e -> {

            if (!task.getValue().isEmpty()) {

                lvRefills.setItems(task.getValue());
                alert = AlertMaker.GetInfoAlert("Se han encontrado " + task.getValue().size() + " resultados");
                alert.showAndWait();

            } else {

                lvRefills.getItems().clear();
                alert = AlertMaker.GetErrorAlert("No se han encontrado recargas de este Operador Movil");
                alert.showAndWait();

            }
        });

        exec.execute(task);

    }


}
