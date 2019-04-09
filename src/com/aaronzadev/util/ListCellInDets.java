package com.aaronzadev.util;

import com.aaronzadev.model.pojo.InDetails;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ListCellInDets extends JFXListCell<InDetails> {

    @FXML private GridPane gvRoot;

    @FXML private Label lblProdName, lblBarcode, lblQty;


    private FXMLLoader loader;

    @Override
    protected void updateItem(InDetails item, boolean empty) {

        super.updateItem(item, empty);

        if(empty || item == null) {

            setText(null);
            setGraphic(null);

        } else {

            if (loader == null) {

                loader = new FXMLLoader(getClass().getResource("/com/aaronzadev/view/fxml/custom/ItemInDet.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            prefWidthProperty().bind(getListView().widthProperty());

            lblProdName.setText(item.getProdName());
            lblBarcode.setText("Codigo de barras: "+item.getBarcode());
            lblQty.setText("Cantidad entrante "+item.getQuantityIn());

            setText(null);
            setGraphic(gvRoot);
        }

    }
}
