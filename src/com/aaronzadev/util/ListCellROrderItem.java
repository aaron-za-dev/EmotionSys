package com.aaronzadev.util;

import com.aaronzadev.model.pojo.OrderDetails;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ListCellROrderItem extends ListCell<OrderDetails> {

    //@FXML private Label lblService, lblDevice, lblSerial, lblCost;

    @FXML private GridPane gvRoot;

    @FXML private Label lblDevice, lblSerial, lblService, lblCost,  lblInObs, lblStRev, lblStFix, lblFixObs;

    private FXMLLoader loader;

    @Override
    protected void updateItem(OrderDetails item, boolean empty) {

        super.updateItem(item, empty);

        if(empty || item == null) {

            setText(null);
            setGraphic(null);

        } else {

            if (loader == null) {

                loader = new FXMLLoader(getClass().getResource("/com/aaronzadev/view/fxml/custom/ItemROrder.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            prefWidthProperty().bind(getListView().widthProperty());

            lblDevice.setText("Dispositivo: "+item.getFullName());
            lblSerial.setText("N° de Serie: "+item.getNoSerie());
            lblInObs.setText("Obs. Entrada: "+item.getInObs());
            lblService.setText("Tipo de Servicio: "+item.getServiceName());
            lblStRev.setText("Estado Revision: ".concat((item.getRevStatus() == 0) ? "Sin Revisar": (item.getRevStatus() == 1) ? "En Revision":"Revisado"));
            lblStFix.setText("Estado Reparacion: ".concat((item.isFixStatus()) ? "Reparado":"No Reparado"));
            lblFixObs.setText("Obs. Reparacion: ".concat((item.getFixObs() == null) ? "Sin Incidencias":item.getFixObs()));
            lblCost.setText("Costo Servicio: $"+item.getPrice());

            /*blService.setText("Tipo de Servicio: "+item.getServiceName());
            lblDevice.setText("Dispositivo: "+item.getFullName());
            lblObs.setText("Observaciones: "+item.getInObs());
            lblCost.setText("Costo Servicio: $"+item.getPrice());
            lblSerial.setText("Nº de Serie: "+item.getNoSerie());*/

            setText(null);
            setGraphic(gvRoot);

        }

    }
}
