package com.aaronzadev.controller;

import com.aaronzadev.model.dao.SaleDAO;
import com.aaronzadev.model.dao.UserDAO;
import com.aaronzadev.model.implementation.SaleDAOImp;
import com.aaronzadev.model.implementation.UserDAOImp;
import com.aaronzadev.model.pojo.FullSale;
import com.aaronzadev.model.pojo.Month;
import com.aaronzadev.model.pojo.SaleDetails;
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
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;

public class HistorySalesController implements Initializable, EventHandler<ActionEvent> {

    @FXML private ComboBox<String> cbSaleIds;

    @FXML private ComboBox<Month> cbMonths;

    @FXML private ComboBox<Integer> cbYears;

    @FXML private ComboBox<String> cbFast;

    @FXML private ComboBox<User> cbUsers;

    @FXML private JFXButton btnExport, btnDets, btnTwo, btnOne, btnThree;

    @FXML private Label lblDate, lblEmp, lblSaleId, lblIva, lblSub, lblTotal;

    @FXML private JFXListView<SaleDetails> lvDets;

    private SaleDAO sdao;
    private Executor executor;
    private Alert alert;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initUi();

        executor = MExecutor.getExecutor();

        fillCombos();

        sdao = new SaleDAOImp(DbConnection.getConnection());

    }

    @Override
    public void handle(ActionEvent event) {

        if(event.getSource() == btnOne){

            if(cbFast.getSelectionModel().getSelectedIndex() == 0) {

                getAllSalesToday();

            }
            else if (cbFast.getSelectionModel().getSelectedIndex() == 1){

                getAllSalesofWeek();

            }
            else if (cbFast.getSelectionModel().getSelectedIndex() == 2){

                getAllSalesofMonth();

            }
            else{

                alert = AlertMaker.GetErrorAlert("Debe seleccionar un metodo rapido de busqueda");
                alert.showAndWait();

            }
        }
        else if (event.getSource() == btnTwo){

            if(cbMonths.getSelectionModel().getSelectedIndex() >= 0 && cbYears.getSelectionModel().getSelectedIndex() >= 0){

                String date =
                        cbYears.getSelectionModel().getSelectedItem()+"/"+"0"+
                        cbMonths.getSelectionModel().getSelectedItem().getMonthID()+"/01";

                getAllSalesPersonalized(date);

            }
            else {

                alert = AlertMaker.GetErrorAlert("Debe seleccionar un Mes y Año para realizar la busqueda");
                alert.showAndWait();

            }

        }
        else if(event.getSource() == btnThree){

            if(cbUsers.getSelectionModel().getSelectedItem() != null){

                getAllSalesByUser(cbUsers.getSelectionModel().getSelectedItem().getUserID());

            }
            else{

                alert = AlertMaker.GetErrorAlert("Debe seleccionar un usuario de la lista para realizar la busqueda");
                alert.showAndWait();

            }

        }
        else if (event.getSource() == btnDets){

            if(cbSaleIds.getSelectionModel().getSelectedIndex() >= 0){

                getFullSaleById(Integer.parseInt(cbSaleIds.getSelectionModel().getSelectedItem()));

            }
            else{

                alert = AlertMaker.GetErrorAlert("Debe seleccionar el folio de venta para mostrar los detalles");
                alert.showAndWait();

            }

        }
    }

    private void initUi() {

        btnOne.setOnAction(this);
        btnTwo.setOnAction(this);
        btnThree.setOnAction(this);
        btnDets.setOnAction(this);
        btnExport.setOnAction(this);

        lvDets.setCellFactory(cell -> new ListCellSaleDets());

    }

    private void fillCombos (){

        cbMonths.setItems(CBoxManager.getMonths());
        cbYears.setItems(CBoxManager.getYears());
        cbFast.setItems(CBoxManager.getMethods());
        getAllUsers();

    }

    private void getAllSalesToday () {

        Task<ObservableList<String>> task = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws Exception {
                return FXCollections.observableArrayList(sdao.GetAllOfToday());
            }
        };

        task.setOnSucceeded(e -> {

            if(!task.getValue().isEmpty()) {

                cbSaleIds.setItems(task.getValue());
                cbSaleIds.setDisable(false);
                alert = AlertMaker.GetInfoAlert("Se han encontrado "+task.getValue().size()+" resultados");
                alert.showAndWait();

            }
            else {

                alert = AlertMaker.GetErrorAlert("No se han encontrado ventas del dia de hoy");
                alert.showAndWait();

            }
        });

        executor.execute(task);

    }


    private void getAllSalesofWeek () {

        Task<ObservableList<String>> task = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws Exception {
                return FXCollections.observableArrayList(sdao.GetAllOfWeek());
            }
        };

        task.setOnSucceeded(e -> {

            if(!task.getValue().isEmpty()) {

                cbSaleIds.setItems(task.getValue());
                cbSaleIds.setDisable(false);
                alert = AlertMaker.GetInfoAlert("Se han encontrado "+task.getValue().size()+" resultados");
                alert.showAndWait();

            }
            else {

                alert = AlertMaker.GetErrorAlert("No se han encontrado ventas en esta semana");
                alert.showAndWait();

            }
        });

        executor.execute(task);

    }


    private void getAllSalesofMonth () {

        Task<ObservableList<String>> task = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws Exception {
                return FXCollections.observableArrayList(sdao.GetAllOfMonth());
            }
        };

        task.setOnSucceeded(e -> {

            if(!task.getValue().isEmpty()) {

                cbSaleIds.setItems(task.getValue());
                cbSaleIds.setDisable(false);
                alert = AlertMaker.GetInfoAlert("Se han encontrado "+task.getValue().size()+" resultados");
                alert.showAndWait();

            }
            else {

                alert = AlertMaker.GetErrorAlert("No se han encontrado ventas en este mes");
                alert.showAndWait();

            }
        });

        executor.execute(task);

    }

    private void getAllSalesPersonalized(String date) {

        Task<ObservableList<String>> task = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws Exception {
                return FXCollections.observableArrayList(sdao.GetAllByDate(date));
            }
        };

        task.setOnSucceeded(e -> {

            if(!task.getValue().isEmpty()) {

                cbSaleIds.setItems(task.getValue());
                cbSaleIds.setDisable(false);
                alert = AlertMaker.GetInfoAlert("Se han encontrado "+task.getValue().size()+" resultados");
                alert.showAndWait();

            }
            else {

                alert = AlertMaker.GetErrorAlert("No se han encontrado ventas en este periodo de tiempo");
                alert.showAndWait();

            }
        });

        task.setOnFailed(e ->{

            task.getException().printStackTrace();

        });

        executor.execute(task);

    }

    private void getAllSalesByUser(short userId) {

        Task<ObservableList<String>> task = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws Exception {
                return FXCollections.observableArrayList(sdao.GetAllByUser(userId));
            }
        };

        task.setOnSucceeded(e -> {

            if(!task.getValue().isEmpty()) {

                cbSaleIds.setItems(task.getValue());
                cbSaleIds.setDisable(false);
                alert = AlertMaker.GetInfoAlert("Se han encontrado "+task.getValue().size()+" resultados");
                alert.showAndWait();

            }
            else {

                alert = AlertMaker.GetErrorAlert("No se han encontrado ventas realizadas por este usuario");
                alert.showAndWait();

            }
        });

        task.setOnFailed(e ->{

            task.getException().printStackTrace();

        });

        executor.execute(task);

    }

    private void getFullSaleById(int id){

        Task<FullSale> task = new Task<FullSale>() {
            @Override
            protected FullSale call() throws Exception {
                return sdao.GetFullSaleByID(id);
            }
        };

        task.setOnSucceeded(e -> {

            if (task.getValue() != null) {

                lblSaleId.setText("Folio de Venta: "+cbSaleIds.getSelectionModel().getSelectedItem());
                lblEmp.setText("Realizada por: "+task.getValue().getUName());
                lblDate.setText("Fecha de Venta: "+task.getValue().getSaleDate());
                lblIva.setText("$ IVA: "+task.getValue().getIVA());
                lblSub.setText("$ SubTotal: "+task.getValue().getSubTotal());
                lblTotal.setText("$ Total: "+task.getValue().getTotal());
                lvDets.setItems(FXCollections.observableArrayList(task.getValue().getDetails()));

            } else {

                alert = AlertMaker.GetErrorAlert("No se ha podido mostrar la información, intentelo nuevamente");
                alert.showAndWait();

            }

        });


        executor.execute(task);

    }

    private void getAllUsers(){

        Task<ObservableList<User>> tusers = new Task<ObservableList<User>>() {
            @Override
            protected ObservableList<User> call() throws Exception {
                UserDAO udao = new UserDAOImp(DbConnection.getConnection());
                return FXCollections.observableArrayList(udao.GetAll());
            }
        };

        tusers.setOnSucceeded(e -> {

            if(tusers.getValue() != null){

                cbUsers.setItems(tusers.getValue());

            }

        });

        executor.execute(tusers);

    }


}
