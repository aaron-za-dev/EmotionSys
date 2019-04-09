package com.aaronzadev.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;


import com.aaronzadev.model.dao.OrderDAO;
import com.aaronzadev.model.implementation.OrderDAOImp;
import com.aaronzadev.model.pojo.Customer;
import com.aaronzadev.model.pojo.CustomerOrders;
import com.aaronzadev.model.pojo.Order;
import com.aaronzadev.model.pojo.OrderDetails;
import com.aaronzadev.util.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;


public class RTSupportController implements Initializable, EventHandler<ActionEvent> {

    @FXML private Label lblNames, lblApp, lblApm, lblStreet, lblNum, lblCol, lblQtyDev,
            lblTotalAm, lblPhoneTwo, lblCp, lblDate;

    @FXML private TextField txtPhone;

    @FXML private ComboBox<String> cbOrders;

    @FXML private JFXListView<OrderDetails> lvDets;

    @FXML private JFXButton btnUpOrder, btnShowDets;
    private Executor executor;
    private Alert alert;
    private Customer cCustom;
    private OrderDAO ordao;

    //TODO verificar el cobro al realizar la entrega del dispositivo

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initUI();

        ordao = new OrderDAOImp(DbConnection.getConnection());
        executor = MExecutor.getExecutor();

    }

    private void initUI() {

        btnShowDets.setOnAction(this);
        btnUpOrder.setOnAction(this);

        DataValidator.setIntegerValidator(txtPhone);

        lvDets.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        lvDets.setOnKeyPressed(e -> {

            if(e.getCode().equals(KeyCode.ESCAPE)) {

                lvDets.getSelectionModel().clearSelection();
            }

        });

        txtPhone.setOnKeyPressed(e ->{

            if(e.getCode().equals(KeyCode.ENTER)){

                if(!txtPhone.getText().isEmpty()){

                    searchByPhoneNum(txtPhone.getText());

                }
                else {

                    alert = AlertMaker.GetErrorAlert("Asegurese de introducir Telefono principal del cliente");
                    alert.showAndWait();

                }
            }

        });

        lvDets.setCellFactory(cell -> new ListCellROrderItem());

    }

    @Override
    public void handle(ActionEvent event) {

        if(event.getSource() == btnShowDets){

            if(cbOrders.getSelectionModel().getSelectedIndex() >= 0){

               showOrderDetails(Integer.parseInt(cbOrders.getSelectionModel().getSelectedItem()));

            }
            else{

                // TODO checar else

            }

        }
        else if(event.getSource() == btnUpOrder){

            if(!lvDets.getSelectionModel().getSelectedItems().isEmpty()){

                //updateDevicesInOrder(lvDets.getSelectionModel().getSelectedItems());
                float total = 0f;

                for (OrderDetails odets : lvDets.getSelectionModel().getSelectedItems()){

                    if(odets.isFixStatus()){

                        total += odets.getPrice();

                    }

                    else {

                        if(odets.getRevStatus() == 2){

                            TextInputDialog inputDialog = new TextInputDialog("0.0");
                            inputDialog.getDialogPane().lookupButton(ButtonType.CANCEL).setDisable(true);
                            inputDialog.setHeaderText("Se han detectado dispositivos sin reparar!");
                            inputDialog.setContentText("Introduzca el costo de la revision por dispositivo");
                            inputDialog.showAndWait();

                            total += Float.parseFloat(inputDialog.getEditor().getText());

                        }

                    }
                }

                System.out.println("Total de la Order: "+total);

                //TODO seguir con la verificacion del cobro de los dispositivos que no fueron reparados
                //

            }
            else {

                alert = AlertMaker.GetErrorAlert("Debe seleccionar los dispositivos que desea entregar");
                alert.showAndWait();
            }

        }
    }

    private void showOrderDetails(int orderId) {

        Task<Order> odets = new Task<Order>() {
            @Override
            protected Order call() throws Exception {
                return ordao.GetDetailsByOrderId(orderId);
            }
        };

        odets.setOnSucceeded(e ->{

            if(odets.getValue() != null){

                lblDate.setText("Fecha de Solicitud: "+odets.getValue().getOrderDate());
                lvDets.setItems(FXCollections.observableArrayList(odets.getValue().getDetails()));

            }

        });

        executor.execute(odets);

    }



    private void searchByPhoneNum(String phone) {

        Task<CustomerOrders> tcustom = new Task<CustomerOrders>() {
            @Override
            protected CustomerOrders call() throws Exception {
                return ordao.GetOrdersByCustomer(phone);
            }
        };

        tcustom.setOnSucceeded(e ->{

            if (tcustom.getValue() != null) {

                cCustom = tcustom.getValue().getCustomer();

                lblNames.setText(cCustom.getCustFName());
                lblApp.setText(cCustom.getCustLPName());
                lblApm.setText(cCustom.getCustLMName());
                lblStreet.setText(cCustom.getStreet());
                lblNum.setText(String.valueOf(cCustom.getNum()));
                lblCol.setText(cCustom.getColony());
                lblPhoneTwo.setText(cCustom.getSPhonNum());

                cbOrders.setItems(FXCollections.observableArrayList(tcustom.getValue().getOrders()));

                alert = AlertMaker.GetInfoAlert("Se han encontrado "+tcustom.getValue().getOrders().size()+" resultados");
                alert.showAndWait();


            } else {

                clearFields();
                alert = AlertMaker.GetErrorAlert("El cliente con el N° telefonico "+phone+" no tiene Ordenes de Servicio disponibles");
                alert.showAndWait();

            }

        });

        executor.execute(tcustom);

    }

    private void updateDevicesInOrder(List<OrderDetails> dets) {

        Task<Integer> tUpdate = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return ordao.UpdateDetailsBySeller(dets);
            }
        };

        tUpdate.setOnSucceeded(e ->{

            if(tUpdate.getValue() > 0){

               clearFields();

               alert = AlertMaker.GetDoneAlert("La Orden de Servicio se ha actualizado correctamente!");
               alert.showAndWait();

            }

        });

        executor.execute(tUpdate);

    }


    private void clearFields(){

        cCustom = null;

        lvDets.getItems().clear();

        txtPhone.clear();
        lblNames.setText("");
        lblApp.setText("");
        lblApm.setText("");
        lblStreet.setText("");
        lblNum.setText("");
        lblCol.setText("");
        lblPhoneTwo.setText("");
        cbOrders.getItems().clear();

    }



}
