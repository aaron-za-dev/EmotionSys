package com.aaronzadev.util;

import com.aaronzadev.model.pojo.OrderDetails;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ListCellNOrderItem extends JFXListCell<OrderDetails> {

    @FXML private Label lblService, lblDevice, lblSerial, lblCost, lblObs;

    @FXML private GridPane gvRoot;

    private FXMLLoader loader;

    @Override
    protected void updateItem(OrderDetails item, boolean empty) {

        super.updateItem(item, empty);

        if(empty || item == null) {

            setText(null);
            setGraphic(null);

        } else {

            if (loader == null) {

                try {
                    loader = new FXMLLoader(getClass().getResource("/com/aaronzadev/view/fxml/custom/ItemNOrder.fxml"));
                    loader.setController(this);
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            prefWidthProperty().bind(getListView().widthProperty());

            lblService.setText("Tipo de Servicio: "+item.getServiceName());
            lblDevice.setText("Dispositivo: "+item.getFullName());
            lblObs.setText("Observaciones: "+item.getInObs());
            lblCost.setText("Costo Servicio: $"+item.getPrice());
            lblSerial.setText("Nº de Serie: "+item.getNoSerie());

            setText(null);
            setGraphic(gvRoot);
        }

    }
}
