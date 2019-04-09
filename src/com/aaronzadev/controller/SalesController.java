package com.aaronzadev.controller;

import com.aaronzadev.model.dao.ProductDAO;
import com.aaronzadev.model.dao.SaleDAO;
import com.aaronzadev.model.implementation.ProductDAOImp;
import com.aaronzadev.model.implementation.SaleDAOImp;
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
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;

public class SalesController implements Initializable, EventHandler<ActionEvent> {

    @FXML private TextField txtBarcode, txtPriceOut;

    @FXML private Label lblProdName, lblDesc, lblPrice, lblStock;

    @FXML private ComboBox<Integer> cbProdOut;

    @FXML private JFXButton btnAddProd, btnRemove, btnAddSale;

    @FXML private JFXListView<SaleDetails> lvDetails;
    
    @FXML private Label lblDate, lblArticles, lblIva, lblSubtotal, lblTotal, lblLastSale;
    
    private ObservableList<SaleDetails> prodsInCar;
    
    private ProductDAO proddao;
    private SaleDAO sdao;
    private Product cProd;
    private User user;
    private Executor executor;    
    private Alert alert;
    private Optional<ButtonType> option;

    //Todo descontar stock una vez el producto este agregado..
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initUi();

        fillCombo();

        user = SessionManager.getCurrentSession(null).getCurrentUser();
        
        executor = MExecutor.getExecutor();
        proddao = new ProductDAOImp(DbConnection.getConnection());
        sdao = new SaleDAOImp(DbConnection.getConnection());
        
        getLastSaleId();
        
    }
    
    private void initUi() {

        btnAddProd.setOnAction(this);
        btnRemove.setOnAction(this);
        btnAddSale.setOnAction(this);

        DataValidator.setMaxSizeValidator(txtBarcode, 20);
        DataValidator.setFloatValidator(txtPriceOut);

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

        lvDetails.setCellFactory(cell -> new ListCellSaleDets());

        prodsInCar = FXCollections.observableArrayList();
        lvDetails.setItems(prodsInCar);

        lblDate.setText(DateManager.getcurrentDate());
        
    }

    private void fillCombo (){

        cbProdOut.setItems(CBoxManager.getNums());

    }
    
    @Override
    public void handle(ActionEvent event) {
        
        if (event.getSource() == btnAddProd) {
            
            if (cProd != null && cbProdOut.getSelectionModel().getSelectedIndex() >= 0) {

                int prodOut = cbProdOut.getSelectionModel().getSelectedItem();

                if (prodOut <= cProd.getStock()) {

                    SaleDetails sdets = new SaleDetails();
                    sdets.setSaleID(Integer.parseInt(lblLastSale.getText()));
                    sdets.setProductID(cProd.getProductID());
                    sdets.setBarcode(cProd.getBarCode());
                    sdets.setProdName(cProd.getName());
                    sdets.setQuantity((short) prodOut);
                    sdets.setPriceOut(Float.parseFloat(txtPriceOut.getText()));

                    prodsInCar.add(sdets);
                    
                    short arts = Short.parseShort(lblArticles.getText());
                    arts += prodOut;

                    lblArticles.setText(String.valueOf(arts));
                    
                    calculateTotals(Float.parseFloat(txtPriceOut.getText()) * prodOut);
                    
                    cleanFields();

                } 
                else {

                    alert = AlertMaker.GetErrorAlert("No cuenta con suficiente stock para realizar la venta!");
                    alert.showAndWait();

                }

            }
            else {
            
                alert = AlertMaker.GetErrorAlert("Asegurese de introducir el codigo de barras correspondiente" +
                        " y la cantidad a vender");
                alert.showAndWait();
            
            }        
        }
        else if (event.getSource() == btnRemove){
            
            if(lvDetails.getSelectionModel().getSelectedItem() != null){
                
                SaleDetails scur = lvDetails.getSelectionModel().getSelectedItem();
                prodsInCar.remove(scur);

                short arts = Short.parseShort(lblArticles.getText());
                arts -= scur.getQuantity();

                lblArticles.setText(String.valueOf(arts));

                calculateTotals(-scur.getPriceOut() * scur.getQuantity());
            
            }
            else {
                
                alert = AlertMaker.GetErrorAlert("Seleccione el producto que quiera eliminar de la venta");
                alert.showAndWait();                            
            }      
        }
        else if (event.getSource() == btnAddSale) {
            
            if (!lvDetails.getItems().isEmpty()){

                Sale sale = new Sale();
                sale.setSaleID(Integer.parseInt(lblLastSale.getText()));
                sale.setUserID(user.getUserID());
                sale.setTax(Float.parseFloat(lblIva.getText()));
                sale.setSubTotal(Float.parseFloat(lblSubtotal.getText()));
                sale.setTotal(Float.parseFloat(lblTotal.getText()));
                sale.setDets(prodsInCar);
                
                alert = AlertMaker.GetWarnAlert("Esta seguro de registrar esta venta?, posiblemente esta no pueda"
                        + "ser modificada despues, desea continuar?");                
                option = alert.showAndWait();
                
                if(option.get() == ButtonType.OK){
                
                    addSale(sale);
                    
                }              
            
            
            }
            else {
                
                alert = AlertMaker.GetErrorAlert("Debe agregar algun producto para realizar la venta");
                alert.showAndWait();           
            }        
        }
        
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
                
                lblProdName.setText(tproduct.getValue().getName());
                lblPrice.setText(String.valueOf(tproduct.getValue().getPrice()));
                lblDesc.setText(tproduct.getValue().getDesc());
                lblStock.setText(String.valueOf(tproduct.getValue().getStock()));
                txtPriceOut.setText(String.valueOf(tproduct.getValue().getPrice()));

            } else {

                cleanFields();
                alert = AlertMaker.GetErrorAlert("No se ha encontrado el producto " + barcode);
                alert.showAndWait();

            }
                    
        });
        
        executor.execute(tproduct);
    
    }

    private void addSale (Sale sale) {
        
        Task<Integer> tsale = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return sdao.Insert(sale);
            }
        };
        
        tsale.setOnSucceeded( e -> {
            
            if (tsale.getValue() > 0) {

                CashOperation op = new CashOperation();
                op.setReference(lblLastSale.getText());
                op.setAmount(Float.parseFloat(lblTotal.getText()));
                op.setUserId(SessionManager.getCurrentSession(null).getCurrentUser().getUserID());
                op.setTypeOperation((short) 1); // Two is the index in cb for sales operation

                cleanTotals();

                lvDetails.getItems().clear();

                getLastSaleId();

                final Stage s = AlertMaker.GetCashDialog(op);
                s.showAndWait();

            }       
        
        });
        
        executor.execute(tsale);  
    
    }
    
    private void getLastSaleId(){
        
        Task<Integer> tlastSaleId = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return sdao.GetLastSaleID();
            }
        };
        
        tlastSaleId.setOnSucceeded(e ->{
            lblLastSale.setText(String.format("%010d", tlastSaleId.getValue()));
        });
    
        executor.execute(tlastSaleId);
    
    }

    private void calculateTotals (float a) {

        // TODO Almacenar en un archivo de propiedades
        float iva = 0.16f;
        
        float cIva = Float.parseFloat(lblIva.getText());
        float cSubtotal = Float.parseFloat(lblSubtotal.getText());
        float cTotal = Float.parseFloat(lblTotal.getText());
        
        float lIva = cIva + (a*iva);
        float lSubTotal = cSubtotal + (a - (a*iva));        
        float lTotal = a+cTotal;        
        
        lblIva.setText(String.valueOf(lIva));
        lblSubtotal.setText(String.valueOf(lSubTotal));
        lblTotal.setText(String.valueOf(lTotal));
    
    }
    
    
    private void cleanFields(){
        
        cProd = null;
        lblProdName.setText("");
        txtBarcode.clear();
        lblDesc.setText("");
        cbProdOut.getSelectionModel().clearSelection();
        lblPrice.setText("");
        lblStock.setText("");
        txtPriceOut.clear();
        
    }

    private void cleanTotals(){

        lblArticles.setText("0");
        lblIva.setText("0");
        lblSubtotal.setText("0");
        lblTotal.setText("0");


    }

    
    
}
