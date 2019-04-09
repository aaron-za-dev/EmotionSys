package com.aaronzadev.util;

import com.aaronzadev.model.pojo.Refill;
import com.aaronzadev.model.pojo.SaleDetails;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ListCellRefill extends JFXListCell<Refill> {

    @FXML private GridPane gvRoot;

    @FXML private Label lblOperator, lblNum, lblRef, lblComm, lblCost, lblObs, lblUser, lblDate;

    private FXMLLoader loader;

    @Override
    protected void updateItem(Refill item, boolean empty) {

        super.updateItem(item, empty);

        if(empty || item == null) {

            setText(null);
            setGraphic(null);

        } else {

            if (loader == null) {

                try {
                    loader = new FXMLLoader(getClass().getResource("/com/aaronzadev/view/fxml/custom/ItemRefill.fxml"));
                    loader.setController(this);
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            prefWidthProperty().bind(getListView().widthProperty());

            lblOperator.setText("Operador: "+item.getOperatorName());
            lblNum.setText("N° Tel: "+item.getPhoneNum());
            lblRef.setText("Referencia: "+item.getReference());
            lblObs.setText("Observaciones: "+item.getObs());
            lblCost.setText("Monto: $"+item.getAmount());
            lblComm.setText("Comision: $"+item.getCommission());
            lblUser.setText("Realizada por: "+item.getNickname());
            lblDate.setText("Fecha: "+item.getRefillDate());

            setText(null);
            setGraphic(gvRoot);
        }

    }
}
