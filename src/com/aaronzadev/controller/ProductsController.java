package com.aaronzadev.controller;


import com.aaronzadev.model.dao.ProductCategoryDAO;
import com.aaronzadev.model.dao.ProductDAO;
import com.aaronzadev.model.implementation.ProductCategoryDAOImp;
import com.aaronzadev.model.implementation.ProductDAOImp;
import com.aaronzadev.model.pojo.Product;
import com.aaronzadev.model.pojo.ProductCategory;
import com.aaronzadev.util.AlertMaker;
import com.aaronzadev.util.DataValidator;
import com.aaronzadev.util.DbConnection;
import com.aaronzadev.util.MExecutor;
import com.jfoenix.controls.JFXButton;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class ProductsController implements Initializable, EventHandler<ActionEvent> {

    @FXML private ListView<ProductCategory> lvCategories;

    @FXML private ListView<Product> lvProducts;

    @FXML private ComboBox<ProductCategory> cbCategory;

    @FXML private ImageView ivBarcode;

    @FXML private JFXButton btnSearch, btnAdd, btnUpdate, btnClean, btnPrint;

    @FXML private TextField txtBarcode, txtName, txtPrice, txtStock;

    @FXML private TextArea txtDesc;
    
    private Executor executor;    
    private Alert alert;    
    private short idCat;
    private String prefix;    
    private String barcode;  
    private int lastProdID;
    private ProductDAO proddao;
    private Product cProd;
    private Optional<ButtonType> option;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initUi();
        
        executor = MExecutor.getExecutor();
        
        proddao = new ProductDAOImp(DbConnection.getConnection());
        
        getLastID();
        
        fillCombo();
    }

    private void initUi(){

        btnAdd.setOnAction(this);
        btnSearch.setOnAction(this);
        btnClean.setOnAction(this);
        btnUpdate.setOnAction(this);
        btnPrint.setOnAction(this);

        DataValidator.setMaxSizeValidator(txtName, 50);
        DataValidator.setMaxSizeValidator(txtBarcode, 25);
        DataValidator.setFloatValidator(txtPrice);
        DataValidator.setIntegerValidator(txtStock);
        DataValidator.setMaxSizeValidator(txtDesc, 120);


        lvCategories.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue != null){

                getAllProdsByCat(newValue.getCategoryID());

            }

        });

        lvProducts.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue != null){

                cProd = newValue;

                setProduct(newValue);

            }

        });

    }

    @Override
    public void handle(ActionEvent event) {
     
        if(event.getSource() == btnAdd){
            
            if (cbCategory.getSelectionModel().getSelectedIndex() >= 0 && !txtName.getText().isEmpty() && !txtPrice.getText().isEmpty()
                    && !txtStock.getText().isEmpty()) {
                
                alert = AlertMaker.GetWarnAlert("Esta seguro de registrar este producto?");
                option = alert.showAndWait();
                
                if(option.get() == ButtonType.OK){

                    idCat = cbCategory.getSelectionModel().getSelectedItem().getCategoryID();
                    prefix = cbCategory.getSelectionModel().getSelectedItem().getPrefixCat();

                    barcode = prefix + "-" + String.format("%010d", lastProdID);

                    System.out.println(barcode);

                    Product p = new Product();
                    p.setCatID(idCat);
                    p.setBarCode(barcode);
                    p.setName(txtName.getText());
                    p.setDesc(txtDesc.getText());
                    p.setPrice(Float.parseFloat(txtPrice.getText()));
                    p.setStock(Short.parseShort(txtStock.getText()));
                
                    addProduct(p);                    
                } 

            } else {

                alert = AlertMaker.GetErrorAlert("Asegurese de introducir la informacion completa");
                alert.showAndWait();
            }
            
        }
        else if (event.getSource() == btnUpdate){
            
            if (cbCategory.getSelectionModel().getSelectedIndex() >= 0 && !txtName.getText().isEmpty() && !txtPrice.getText().isEmpty()
                    && !txtStock.getText().isEmpty() && cProd != null) {

                idCat = cbCategory.getSelectionModel().getSelectedItem().getCategoryID();
                prefix = cbCategory.getSelectionModel().getSelectedItem().getPrefixCat();

                barcode = prefix + "-" + String.format("%010d", cProd.getProductID());

                Product p = new Product();
                p.setProductID(cProd.getProductID());
                p.setCatID(idCat);
                p.setBarCode(barcode);
                p.setName(txtName.getText());
                p.setDesc(txtDesc.getText());
                p.setPrice(Float.parseFloat(txtPrice.getText()));
                p.setStock(Short.parseShort(txtStock.getText()));

                alert = AlertMaker.GetWarnAlert("Esta seguro que desea actualizar la informacion de este producto?");                
                option = alert.showAndWait();
                
                if(option.get() == ButtonType.OK){
                
                    updateProduct(p);
                    
                }     
                

            } else {

                alert = AlertMaker.GetErrorAlert("Asegurese de introducir la informacion completa");
                alert.showAndWait();
            }          
        
        }
        else if (event.getSource() == btnSearch){
            
            if (!txtBarcode.getText().equals("")) {
                
                searchByBarcode(txtBarcode.getText());
                
            } else {

                alert = AlertMaker.GetErrorAlert("Asegurese de introducir el codigo de barras correspondiente");
                alert.showAndWait();

            }
        
        }
        else if (event.getSource() == btnPrint){

            if (cProd != null) {
                
                saveBarcodePDF(txtBarcode.getText());
                
            } else {

                alert = AlertMaker.GetErrorAlert("Asegurese de introducir el codigo de barras correspondiente");
                alert.showAndWait();

            }
        
        }
        else if (event.getSource() == btnClean){
            
            cleanFields();
        
        }
    }
    
    
    private void fillCombo(){
        
        Task<ObservableList<ProductCategory>> tCats = new Task<ObservableList<ProductCategory>>() {
            @Override
            protected ObservableList<ProductCategory> call() throws Exception {
                ProductCategoryDAO cats = new ProductCategoryDAOImp(DbConnection.getConnection());
                return FXCollections.observableArrayList(cats.GetAll());
            }
        };
        
        tCats.setOnSucceeded(e -> {

            lvCategories.setItems(tCats.getValue());
            cbCategory.setItems(tCats.getValue());
        
        });       
        
        executor.execute(tCats); 
        
    
    }
    
    private void getLastID(){
        
        Task<Integer> tlastProdId = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return proddao.GetLastProductID();
            }
        };
        
        tlastProdId.setOnSucceeded(e ->{
             lastProdID = tlastProdId.getValue();
        });
    
        executor.execute(tlastProdId);
    
    }
    
    private void addProduct (Product p){
    
        Task<Integer> tinsert = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return proddao.Insert(p);
            }
        };
        
        tinsert.setOnSucceeded(e ->{
             if(tinsert.getValue() > 0){
               
               getLastID(); 
               
               cleanFields();
                 
               alert = AlertMaker.GetInfoAlert("Producto Agregado Correctamente!");
               alert.showAndWait();
           }
        });
    
        executor.execute(tinsert);   
    
    }
    
    
    private void updateProduct (Product p){
    
        Task<Integer> tupdate = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return proddao.Update(p);
            }
        };
        
        tupdate.setOnSucceeded(e -> {
            if (tupdate.getValue() > 0) {

                cleanFields();

                alert = AlertMaker.GetInfoAlert("El producto " + p.getBarCode() + " se ha actualizado correctamente");
                alert.showAndWait();
            }
        });
    
        executor.execute(tupdate);   
    
    }

    private void getAllProdsByCat(short categoryID) {

        Task<ObservableList<Product>> tprods = new Task<ObservableList<Product>>() {
            @Override
            protected ObservableList<Product> call() throws Exception {
                return FXCollections.observableArrayList(proddao.GetAllByCategoryID(categoryID));
            }
        };

        tprods.setOnSucceeded(e ->{

            if(tprods.getValue().size() > 0){

                lvProducts.setItems(tprods.getValue());
                alert = AlertMaker.GetInfoAlert("Se han encontrado "+tprods.getValue().size()+" productos en esta categoria.");
                alert.showAndWait();

            }
            else{

                alert = AlertMaker.GetInfoAlert("No se han encontrado productos en esta Categoria");
                alert.showAndWait();

            }

        });

        executor.execute(tprods);
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

                setProduct(tproduct.getValue());

            } else {

                txtBarcode.clear();
                alert = AlertMaker.GetErrorAlert("No se ha encontrado el producto " + barcode);
                alert.showAndWait();

            }
                    
        });
        
        executor.execute(tproduct);
    
    }
    
    private void generateBarcode (String barcode){
        
        Barcode128 bcode = new Barcode128();
        bcode.setCode(barcode);        
        
        java.awt.Image img = bcode.createAwtImage(Color.darkGray, Color.white);
        
        BufferedImage bfi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        bfi.getGraphics().drawImage(img, 0, 0, null);
        
        Image imgfx = SwingFXUtils.toFXImage(bfi, null);
        
        ivBarcode.setImage(imgfx);
        
    }
    
    private void saveBarcodePDF (String bacode) {

        FileChooser fch = new FileChooser();
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter("Achivo PDF (*.pdf)", "*.pdf");
        fch.getExtensionFilters().add(exFilter);

        File f = fch.showSaveDialog(null);

        if (f != null) {

            try {

                Document doc = new Document();
                PdfWriter pdf = PdfWriter.getInstance(doc, new FileOutputStream(f));
                PdfPTable table = new PdfPTable(5);

                doc.open();

                Barcode128 bcode = new Barcode128();
                bcode.setAltText(bacode);
                bcode.setCode(bacode);
                com.lowagie.text.Image img = bcode.createImageWithBarcode(pdf.getDirectContent(), Color.black, Color.black);

                for (int i = 0; i < 10; i++) {
                    table.addCell(img);
                }

                doc.add(table);

                doc.close();

                alert = AlertMaker.GetDoneAlert("El codigo de barras se ha guardado correctamente");
                alert.showAndWait();

            } catch (FileNotFoundException | DocumentException e) {
                //MyAlert.showAlertDone(Constants.SAVE_FILEFAILED);
                //TODO usar la alertexception
                e.printStackTrace();
            }

        }
        else {
            alert = AlertMaker.GetInfoAlert("No se ha seleccionado ninguna carpeta");
            alert.showAndWait();
        }

    }

    private void setProduct(Product pro)  {

        txtBarcode.setText(pro.getBarCode());
        txtBarcode.setEditable(false);
        txtName.setText(pro.getName());
        txtPrice.setText(String.valueOf(pro.getPrice()));
        txtDesc.setText(pro.getDesc());
        txtStock.setText(String.valueOf(pro.getStock()));
        // TODO txtStock.setText((pro.getStock()) == 0 ? "0": String.valueOf(pro.getStock()));
        // check regex expression for zero values on this field
        cbCategory.getSelectionModel().select(pro.getCatID() - 1);

        generateBarcode(pro.getBarCode());

    }
    
    private void cleanFields(){
        
        cProd = null;
        txtName.clear();
        txtBarcode.clear();
        txtBarcode.setEditable(true);
        txtName.setTooltip(null);
        txtDesc.clear();
        txtDesc.setTooltip(null);
        txtPrice.clear();
        txtStock.clear();
        ivBarcode.setImage(null);
        cbCategory.getSelectionModel().clearSelection();
        lvCategories.getSelectionModel().clearSelection();
        lvProducts.getItems().clear();
        
    }

    
    
}
