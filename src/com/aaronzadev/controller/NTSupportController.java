package com.aaronzadev.controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;


import com.aaronzadev.model.dao.*;
import com.aaronzadev.model.implementation.*;
import com.aaronzadev.model.pojo.*;
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
import javafx.scene.input.KeyCode;


public class NTSupportController implements Initializable, EventHandler<ActionEvent> {

    @FXML JFXListView<OrderDetails> lvDets;

    @FXML private Label lblNames, lblApp, lblApm, lblStreet, lblNum, lblCol, lblPhoneTwo,
            lblDate, lblLastOrder;

    @FXML private TextField txtPhoneOne,txtSerial, txtCost;

    @FXML private JFXButton btnSearch, btnAdd, btnRemove, btnSaveOrder;

    @FXML private TextArea txtObs;

    @FXML private ComboBox<PriorityService> cbPriority;

    @FXML private ComboBox<TypeDevice> cbTypes;

    @FXML private ComboBox<BrandDevice> cbBrands;

    @FXML private ComboBox<TypeService> cbServices;

    @FXML private ComboBox<Device> cbDevices;

    private ObservableList<OrderDetails> orderDets;

    private Executor executor;
    private Alert alert;
    private Optional<ButtonType> option;
    private Customer cCustom;
    private OrderDAO ordao;
    private User user;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initiUi();

        user = SessionManager.getCurrentSession(null).getCurrentUser();
        executor = MExecutor.getExecutor();
        ordao = new OrderDAOImp(DbConnection.getConnection());

        getLastOrderId();

        fillCombos();

    }

    private void initiUi() {

        btnSearch.setOnAction(this);
        btnAdd.setOnAction(this);
        btnRemove.setOnAction(this);
        btnSaveOrder.setOnAction(this);

        DataValidator.setMaxSizeValidator(txtSerial, 20);
        DataValidator.setIntegerValidator(txtPhoneOne);
        DataValidator.setFloatValidator(txtCost);
        DataValidator.setMaxSizeValidator(txtObs, 150);

        txtPhoneOne.setOnKeyPressed( e -> {

            if (e.getCode().equals(KeyCode.ENTER)) {

                if (!txtPhoneOne.getText().isEmpty()) {

                    searchByPhoneNum(txtPhoneOne.getText());

                } else {

                    alert = AlertMaker.GetErrorAlert("Introduzca el número telefonico principal del cliente a buscar");
                    alert.showAndWait();

                }
            }

        });

        lvDets.setCellFactory(cell -> new ListCellNOrderItem());

        orderDets = FXCollections.observableArrayList();
        lvDets.setItems(orderDets);

        lblDate.setText(DateManager.getcurrentDate());

        cbPriority.setItems(CBoxManager.getPriorityServices());

        //TODO Checar creo que ya lo hize (corregir el insert del detalle de la orden incluyendo la prioridad)

    }

    @Override
    public void handle(ActionEvent event) {

        if(event.getSource() == btnSearch){

            if(cbTypes.getSelectionModel().getSelectedIndex() >= 0 && cbBrands.getSelectionModel().getSelectedIndex() >= 0){

                getDevices(cbTypes.getSelectionModel().getSelectedItem().getTypeId(), cbBrands.getSelectionModel().getSelectedItem().getBrandId());

            }
            else{

                alert = AlertMaker.GetErrorAlert("Debe seleccionar un Tipo y Marca de dispositivo de las listas desplegables");
                alert.showAndWait();

            }

        }
        else if (event.getSource() == btnAdd){

            if (cbTypes.getSelectionModel().getSelectedIndex() >= 0 && cbBrands.getSelectionModel().getSelectedIndex() >= 0 &&
                    cbDevices.getSelectionModel().getSelectedIndex() >= 0 && cbServices.getSelectionModel().getSelectedIndex() >= 0 &&
                    !txtSerial.getText().isEmpty() && !txtObs.getText().isEmpty() && cbPriority.getSelectionModel().getSelectedIndex() >= 0
                    && !txtCost.getText().isEmpty()) {

                OrderDetails odets = new OrderDetails();

                String type = cbTypes.getSelectionModel().getSelectedItem().getTypeName();
                String brand = cbBrands.getSelectionModel().getSelectedItem().getBrandName();
                String model = cbDevices.getSelectionModel().getSelectedItem().getDeviceModel();

                odets.setOrderId(Integer.parseInt(lblLastOrder.getText()));
                odets.setFullName(type+" "+brand+" "+model);
                odets.setDeviceId(cbDevices.getSelectionModel().getSelectedItem().getDeviceId());
                odets.setServiceId(cbServices.getSelectionModel().getSelectedItem().getServiceId());
                odets.setServiceName(cbServices.getSelectionModel().getSelectedItem().getServiceName());
                odets.setNoSerie(txtSerial.getText());
                odets.setInObs(txtObs.getText());
                odets.setPriorityService(cbPriority.getSelectionModel().getSelectedItem().getIDPriority());
                odets.setPrice(Float.parseFloat(txtCost.getText()));

                orderDets.add(odets);

                clearDevice();

            } else {

                alert = AlertMaker.GetErrorAlert("Asegurese de intoducir la informacion requerida del dispositivo");
                alert.showAndWait();

            }

        }
        else if (event.getSource() == btnRemove){

            if(lvDets.getSelectionModel().getSelectedItem() != null){

                orderDets.remove(lvDets.getSelectionModel().getSelectedItem());

            }
            else{

                alert = AlertMaker.GetErrorAlert("Seleccione el dispositivo que quiera eliminar de la orden de servicio");
                alert.showAndWait();
            }

        }
        else if(event.getSource() == btnSaveOrder){

            if(!lvDets.getItems().isEmpty() && cCustom != null){

                int orderId = Integer.parseInt(lblLastOrder.getText());
                List<OrderDetails> dets = lvDets.getItems();

                Order order = new Order();
                order.setOrderId(orderId);
                order.setCustomId(cCustom.getCustId());
                order.setUserId(user.getUserID());
                order.setDetails(dets);

                alert = AlertMaker.GetWarnAlert("Esta seguro de registrar esta Orden de Servicio?, posiblemente esta no pueda"
                        + "ser modificada despues, desea continuar?");
                option = alert.showAndWait();

                if(option.get() == ButtonType.OK){

                    addOrder(order);

                }

            }
            else{

                alert = AlertMaker.GetErrorAlert("Debe agregar completar la informacion requerida para realizar la Orden de Servicio");
                alert.showAndWait();

            }

        }

    }

    private void fillCombos (){


        Task<ObservableList<BrandDevice>> tbrands = new Task<ObservableList<BrandDevice>>() {
            @Override
            protected ObservableList<BrandDevice> call() throws Exception {
                BrandDevDAO brdao = new BrandDevDAOImp(DbConnection.getConnection());
                return FXCollections.observableArrayList(brdao.GetAll());
            }
        };

        tbrands.setOnSucceeded(e -> {

            if(!tbrands.getValue().isEmpty()){

                cbBrands.setItems(tbrands.getValue());
            }
            else {

                alert = AlertMaker.GetErrorAlert("No se han encontrado marcas de dispositivos");
                alert.showAndWait();

            }

        });

        Task<ObservableList<TypeDevice>> ttypes = new Task<ObservableList<TypeDevice>>() {
            @Override
            protected ObservableList<TypeDevice> call() throws Exception {
                TypeDevDAO tdao = new TypeDevDAOImp(DbConnection.getConnection());
                return FXCollections.observableArrayList(tdao.GetAll());
            }
        };

        ttypes.setOnSucceeded(e -> {

            if(!ttypes.getValue().isEmpty()){

                cbTypes.setItems(ttypes.getValue());
            }
            else {

                cbDevices.getItems().clear();
                alert = AlertMaker.GetErrorAlert("No se han encontrado tipos de dispositivos");
                alert.showAndWait();

            }

        });


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
            else {

                // TODO verificar eliminar else
                alert = AlertMaker.GetErrorAlert("No se han encontrado servicios");
                alert.showAndWait();

            }

        });


        executor.execute(tbrands);

        executor.execute(ttypes);

        executor.execute(tservices);

    }

    private void getDevices (short type, short brand) {

        Task<ObservableList<Device>> tdevices = new Task<ObservableList<Device>>() {
            @Override
            protected ObservableList<Device> call() throws Exception {
                DeviceDAO devdao = new DeviceDAOImp(DbConnection.getConnection());
                return FXCollections.observableArrayList(devdao.getAllByBrandAndType(type, brand));
            }
        };

        tdevices.setOnSucceeded(e -> {

            if(!tdevices.getValue().isEmpty()){

                cbDevices.setItems(tdevices.getValue());
                alert = AlertMaker.GetInfoAlert("Se han encontrado "+tdevices.getValue().size()+" modelo (s)");
                alert.showAndWait();

            }
            else {

                cbTypes.getSelectionModel().clearSelection();
                cbBrands.getSelectionModel().clearSelection();
                alert = AlertMaker.GetErrorAlert("No se han encontrado modelos de este tipo y marca");
                alert.showAndWait();

            }

        });

        executor.execute(tdevices);


    }

    private void searchByPhoneNum(String phone) {

        Task<Customer> tcustom = new Task<Customer>() {
            @Override
            protected Customer call() throws Exception {
                CustomerDAO custdao = new CustomerDAOImp(DbConnection.getConnection());
                return custdao.GetByPhone(phone);
            }
        };

        tcustom.setOnSucceeded(e ->{

            if (tcustom.getValue() != null) {

                cCustom = tcustom.getValue();

                lblNames.setText(cCustom.getCustFName());
                lblApp.setText(cCustom.getCustLPName());
                lblApm.setText(cCustom.getCustLMName());
                lblStreet.setText(cCustom.getStreet());
                lblNum.setText(String.valueOf(cCustom.getNum()));
                lblCol.setText(cCustom.getColony());
                lblPhoneTwo.setText(cCustom.getSPhonNum());

            } else {

                clearCustomer();
                alert = AlertMaker.GetErrorAlert("No se ha encontrado el cliente con N° Telefonico " + phone);
                alert.showAndWait();

            }

        });

        executor.execute(tcustom);

    }

    private void addOrder (Order order) {

        Task<Integer> torder = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return ordao.Insert(order);
            }
        };

        torder.setOnFailed(e ->{

            torder.getException().printStackTrace();

        });

        torder.setOnSucceeded( e -> {

            if (torder.getValue() > 0) {

                lvDets.getItems().clear();
                clearCustomer();

                getLastOrderId();

                alert = AlertMaker.GetInfoAlert("La orden de servicio se ha registrado Correctamente!");
                alert.showAndWait();

            }

        });

        executor.execute(torder);

    }

    private void getLastOrderId(){

        Task<Integer> tlastSaleId = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return ordao.GetLastOrderID();
            }
        };

        tlastSaleId.setOnSucceeded(e ->{
            lblLastOrder.setText(String.format("%010d", tlastSaleId.getValue()));
        });

        executor.execute(tlastSaleId);

    }

    private void clearCustomer() {

        cCustom = null;
        txtPhoneOne.clear();
        lblNames.setText("");
        lblApp.setText("");
        lblApm.setText("");
        lblStreet.setText("");
        lblNum.setText("");
        lblCol.setText("");
        lblPhoneTwo.setText("");

    }

    private void clearDevice (){

        cbTypes.getSelectionModel().clearSelection();
        cbBrands.getSelectionModel().clearSelection();
        cbDevices.getSelectionModel().clearSelection();
        cbServices.getSelectionModel().clearSelection();
        cbPriority.getSelectionModel().clearSelection();
        txtSerial.clear();
        txtObs.clear();
        txtCost.clear();

    }

}
