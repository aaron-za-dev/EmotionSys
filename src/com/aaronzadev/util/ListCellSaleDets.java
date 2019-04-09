package com.aaronzadev.util;

import com.aaronzadev.model.pojo.SaleDetails;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ListCellSaleDets extends JFXListCell<SaleDetails> {

    @FXML private GridPane gvRoot;

    //@FXML private AnchorPane apRoot;

    @FXML private Label lblProd, lblQty, lblBarcode, lblPrice;

    private FXMLLoader loader;

    @Override
    protected void updateItem(SaleDetails item, boolean empty) {

        super.updateItem(item, empty);

        if(empty || item == null) {

            setText(null);
            setGraphic(null);

        } else {

            if (loader == null) {

                try {
                    loader = new FXMLLoader(getClass().getResource("/com/aaronzadev/view/fxml/custom/ItemSaleDets.fxml"));
                    loader.setController(this);
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            prefWidthProperty().bind(getListView().widthProperty());


            lblProd.setText(item.getProdName());
            lblBarcode.setText("Codigo de Producto: "+item.getBarcode());
            lblQty.setText("Cantidad: "+item.getQuantity());
            lblPrice.setText("$"+item.getPriceOut());

            setText(null);
            setGraphic(gvRoot);
        }

    }
}
