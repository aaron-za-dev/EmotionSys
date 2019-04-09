package com.aaronzadev.controller;

import com.aaronzadev.model.dao.OrderDAO;
import com.aaronzadev.model.dao.TypeServDAO;
import com.aaronzadev.model.implementation.OrderDAOImp;
import com.aaronzadev.model.implementation.TypeServDAOImp;
import com.aaronzadev.model.pojo.*;
import com.aaronzadev.util.AlertMaker;
import com.aaronzadev.util.CBoxManager;
import com.aaronzadev.util.DbConnection;
import com.aaronzadev.util.MExecutor;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;

public class TechPanelController implements Initializable, EventHandler<ActionEvent> {

    @FXML
    private JFXListView<OrderDetails> lvDevices;

    @FXML
    private ComboBox<PriorityService> cbPriorities;

    @FXML
    private ComboBox<TypeService> cbServices;

    @FXML
    private ComboBox<RevStatus> cbStatusRev;

    @FXML
    private ComboBox<FixStatus> cbStatusFix;

    @FXML
    private JFXButton btnOne, btnTwo, btnUpdate;

    @FXML
    private TextArea txtObsFix;

    @FXML
    private Label lblDev, lblSerial, lblServ, lblInObs, lblOut, lblCost, lblDateOut;

    private OrderDAO odao;
    private OrderDetails curdets;
    private Executor exec;
    private Alert alert;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        exec = MExecutor.getExecutor();

        prepareUi();

        odao = new OrderDAOImp(DbConnection.getConnection());

        //TODO agregar prioridad a los servicios tecnicos en la bd y en la ui
        //TODO realizar el panel del tecnico y agregar busqueda por prioridad y por servicio

    }

    private void prepareUi() {

        btnOne.setOnAction(this);
        btnTwo.setOnAction(this);
        btnUpdate.setOnAction(this);

        //TODO create the CustolListCell lvDevices.setCellFactory(cell -> new ListCellOne());

        lvDevices.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {

                curdets = newValue;

                lblDev.setText(newValue.getFullName());
                lblSerial.setText(newValue.getNoSerie());
                lblInObs.setText(newValue.getInObs());
                lblServ.setText(newValue.getServiceName());
                cbStatusRev.getSelectionModel().select((newValue.getRevStatus() == 0) ? 0 : newValue.getRevStatus());
                cbStatusFix.getSelectionModel().select((newValue.isFixStatus()) ? 1 : 0);
                txtObsFix.setText((newValue.getFixObs() == null) ? "" : newValue.getFixObs());
                lblOut.setText((newValue.isDelivered()) ? "Entregado" : "No Entregado");
                lblCost.setText(String.valueOf(newValue.getPrice()));
                lblDateOut.setText((newValue.getDateDeliv() == null) ? "N/A" : newValue.getDateDeliv());


            }
        });

        fillCombos();

    }

    private void fillCombos() {

        cbPriorities.setItems(CBoxManager.getPriorityServices());
        cbStatusFix.setItems(CBoxManager.getFixstatus());
        cbStatusRev.setItems(CBoxManager.getRevStatus());

        fillComboServices();

    }

    @Override
    public void handle(ActionEvent event) {

        if(event.getSource() == btnOne){

            if(cbPriorities.getSelectionModel().getSelectedIndex() >= 0){

                searchDevicesByPriority(cbPriorities.getSelectionModel().getSelectedItem().getIDPriority());

            }
            else {

                alert = AlertMaker.GetErrorAlert("Debe de seleccionar la prioridad de la lista desplegable");
                alert.showAndWait();

            }

        }
        else if(event.getSource() == btnTwo){

            if(cbServices.getSelectionModel().getSelectedIndex() >= 0){

                searchDevicesByService(cbServices.getSelectionModel().getSelectedItem().getServiceId());

            }
            else {

                alert = AlertMaker.GetErrorAlert("Debe de seleccionar el tipo de servicio de la lista desplegable");
                alert.showAndWait();

            }

        }
        else if (event.getSource() == btnUpdate) {

            if(curdets != null){

                if(!txtObsFix.getText().isEmpty() && cbStatusRev.getSelectionModel().getSelectedIndex() >= 0
                        && cbStatusFix.getSelectionModel().getSelectedIndex() >= 0){

                    curdets.setFixObs(txtObsFix.getText());
                    curdets.setRevStatus(cbStatusRev.getSelectionModel().getSelectedItem().getRevId());
                    curdets.setFixStatus(cbStatusFix.getSelectionModel().getSelectedItem().getFixStatusId() == 1);

                    updateOrderDetails(curdets);

                }
                else {

                    System.out.println("Debe de completar toda la infomacion requerida! :D");

                }

            }
            else {

                System.out.println("Ensure the data is complete! :D");
            }

        }

    }

    private void searchDevicesByPriority(short prioId){

        Task<ObservableList<OrderDetails>> tdets = new Task<ObservableList<OrderDetails>>() {
            @Override
            protected ObservableList<OrderDetails> call() throws Exception {
                return FXCollections.observableArrayList(odao.GetAllDetsByPriority(prioId));
            }
        };

        tdets.setOnSucceeded(event -> {

            if(tdets.getValue().size() > 0){

                lvDevices.setItems(tdets.getValue());
                //TODO verfificar agregar alert para mostrar los dispositivos encontrados

            }

        });

        exec.execute(tdets);

    }

    private void searchDevicesByService(short servId){

        Task<ObservableList<OrderDetails>> tdets = new Task<ObservableList<OrderDetails>>() {
            @Override
            protected ObservableList<OrderDetails> call() throws Exception {
                return FXCollections.observableArrayList(odao.GetAllDetsByService(servId));
            }
        };

        tdets.setOnSucceeded(event -> {

            if(tdets.getValue().size() > 0){

                lvDevices.setItems(tdets.getValue());
                //TODO verfificar agregar alert para mostrar los dispositivos encontrados

            }

        });

        exec.execute(tdets);

    }

    private void updateOrderDetails(OrderDetails odets){

        Task<Integer> tupdate = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return odao.UpdateDetailsByTec(odets);
            }
        };

        tupdate.setOnSucceeded(e ->{

            if(tupdate.getValue() > 0) {

                System.out.printf("El estado de reparacion del dispositivo se ha actaulizado correctamente");
                //searchDevices();
                cleanFields();


            }
            else {

                System.out.println("Ha ocurrido algun error");

            }


        });

        exec.execute(tupdate);

    }

    private void fillComboServices(){

        Task<ObservableList<TypeService>> tservices = new Task<ObservableList<TypeService>>() {
            @Override
            protected ObservableList<TypeService> call() throws Exception {
                TypeServDAO servdao = new TypeServDAOImp(DbConnection.getConnection());
                return FXCollections.observableArrayList(servdao.GetAll());
            }
        };

        tservices.setOnSucceeded(e -> {

            if(!tservices.getValue().isEmpty()){

                cbServices.setItems(tservices.getValue());
            }
            /*else {

                alert = AlertMaker.GetErrorAlert("No se han encontrado servicios");
                alert.showAndWait();

            }*/

        });

        exec.execute(tservices);

    }

    private void cleanFields(){

        curdets = null;

        lblDev.setText("");
        lblSerial.setText("");
        lblInObs.setText("");
        lblServ.setText("");
        cbStatusRev.getSelectionModel().clearSelection();
        cbStatusFix.getSelectionModel().clearSelection();
        txtObsFix.clear();
        lblOut.setText("");
        lblCost.setText("");
        lblDateOut.setText("");

    }
}
