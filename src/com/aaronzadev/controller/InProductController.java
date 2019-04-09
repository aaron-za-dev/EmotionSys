package com.aaronzadev.controller;

import com.aaronzadev.model.dao.InProductsDAO;
import com.aaronzadev.model.dao.ProductDAO;
import com.aaronzadev.model.implementation.InProductsDAOImp;
import com.aaronzadev.model.implementation.ProductDAOImp;
import com.aaronzadev.model.pojo.InDetails;
import com.aaronzadev.model.pojo.InProduct;
import com.aaronzadev.model.pojo.Product;
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

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;

public class InProductController implements Initializable, EventHandler<ActionEvent> {

    @FXML private TextField txtBarcode, txtProvider;

    @FXML private TextArea txtObs;

    @FXML private ComboBox<Integer> cbProdIn;

    @FXML private Label lblStock, lblDesc, lblPrice, lblName, lblDate, lblLastIn;

    @FXML private JFXListView<InDetails> lvProdsIn;

    @FXML private JFXButton btnRemove, btnSave, btnAddProd;

    private ObservableList<InDetails> inDetails;

    private ProductDAO proddao;
    private InProductsDAO indao;
    private Product cProd;
    private Executor executor;
    private Alert alert;
    private Optional<ButtonType> option;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initUI();

        executor = MExecutor.getExecutor();
        proddao = new ProductDAOImp(DbConnection.getConnection());
        indao = new InProductsDAOImp(DbConnection.getConnection());

        getLastInId();

    }

    private void initUI(){

        btnAddProd.setOnAction(this);
        btnRemove.setOnAction(this);
        btnSave.setOnAction(this);

        DataValidator.setMaxSizeValidator(txtBarcode, 20);
        DataValidator.setMaxSizeValidator(txtProvider, 40);
        DataValidator.setMaxSizeValidator(txtObs, 150);

        lvProdsIn.setCellFactory(cell -> new ListCellInDets());
        lvProdsIn.setExpanded(true);

        txtBarcode.setOnKeyPressed( e -> {

            if (e.getCode().equals(KeyCode.ENTER)) {

                if (!txtBarcode.getText().isEmpty()) {

                    searchByBarcode(txtBarcode.getText());

                } else {

                    alert = AlertMaker.GetErrorAlert("Asegurese de introducir el codigo de barras del producto");
                    alert.showAndWait();

                }
            }

        });

        inDetails = FXCollections.observableArrayList();
        lvProdsIn.setItems(inDetails);

        lblDate.setText(DateManager.getcurrentDate());

        cbProdIn.setItems(CBoxManager.getNums());

    }

    @Override
    public void handle(ActionEvent event) {

        if(event.getSource() == btnAddProd){

            if (cProd != null && cbProdIn.getSelectionModel().getSelectedIndex() >= 0) {

                int prodIn = cbProdIn.getSelectionModel().getSelectedItem();

                InDetails in = new InDetails();
                in.setInId(Integer.parseInt(lblLastIn.getText()));
                in.setProductId(cProd.getProductID());
                in.setQuantityIn(prodIn);
                in.setProdName(cProd.getName());
                in.setBarcode(cProd.getBarCode());

                inDetails.add(in);

                cleanProduct();

            }
            else {

                alert = AlertMaker.GetErrorAlert("Asegurese de introducir el codigo de barras correspondiente" +
                        " y la cantidad de entrada");
                alert.showAndWait();

            }

        }
        else if(event.getSource() == btnRemove){

            if(lvProdsIn.getSelectionModel().getSelectedItem() != null){

                inDetails.remove(lvProdsIn.getSelectionModel().getSelectedItem());

            }
            else {

                alert = AlertMaker.GetErrorAlert("Seleccione el producto de la lista que quiera remover");
                alert.showAndWait();
            }

        }
        else if(event.getSource() == btnSave){

            if (!lvProdsIn.getItems().isEmpty() && !txtProvider.getText().isEmpty()){

                InProduct inp = new InProduct();
                inp.setProviderName(txtProvider.getText());
                inp.setInObs(txtObs.getText());
                inp.setInDetails(lvProdsIn.getItems());

                alert = AlertMaker.GetWarnAlert("Una vez realizado el registro, la informacion no podra modificarse, ¿Desea continuar con el registro?");
                option = alert.showAndWait();

                if(option.get() == ButtonType.OK){

                    addInProduct(inp);

                }


            }
            else {

                alert = AlertMaker.GetErrorAlert("Debe completar la informacion requerida para guardar la entrada de producto");
                alert.showAndWait();
            }


        }

    }

    private void addInProduct (InProduct in) {

        Task<Integer> tAddIn = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return indao.Insert(in);
            }
        };

        tAddIn.setOnSucceeded( e -> {

            if (tAddIn.getValue() > 0) {

                getLastInId();

                lvProdsIn.getItems().clear();

                cleanFields();

                alert = AlertMaker.GetInfoAlert("La Entrada de Producto se ha registrado correctamente!");
                alert.showAndWait();

            }

        });

        executor.execute(tAddIn);

    }

    private void getLastInId(){

        Task<Integer> tlastInId = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return indao.GetLastInId();
            }
        };

        tlastInId.setOnSucceeded(e ->{
            lblLastIn.setText(String.format("%010d", tlastInId.getValue()));
        });

        executor.execute(tlastInId);

    }

    private void searchByBarcode (String barcode) {

        Task<Product> tproduct = new Task<Product>() {
            @Override
            protected Product call() throws Exception {
                return proddao.GetProductByBarCode(barcode);
            }
        };

        tproduct.setOnSucceeded(e ->{

            if (tproduct.getValue() != null) {

                cProd = tproduct.getValue();

                lblName.setText(tproduct.getValue().getName());
                lblPrice.setText(String.valueOf(tproduct.getValue().getPrice()));
                lblDesc.setText(tproduct.getValue().getDesc());
                lblStock.setText(String.valueOf(tproduct.getValue().getStock()));
                lblPrice.setText(String.valueOf(tproduct.getValue().getPrice()));

            } else {

                cleanProduct();
                alert = AlertMaker.GetErrorAlert("No se ha encontrado el producto " + barcode);
                alert.showAndWait();

            }

        });

        executor.execute(tproduct);
    }

    private void cleanProduct(){

        cProd = null;
        lblName.setText("");
        txtBarcode.clear();
        lblDesc.setText("");
        cbProdIn.getSelectionModel().clearSelection();
        lblPrice.setText("");
        lblStock.setText("");

    }

    private void cleanFields(){

        txtProvider.clear();
        txtObs.clear();

    }
}
