package com.aaronzadev.util;

import com.aaronzadev.controller.CashDialogController;
import com.aaronzadev.model.pojo.CashOperation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AlertMaker {

    private static final Image INFO_IMAGE = new Image("/com/aaronzadev/view/icons/info.png");
    private static final Image ERROR_IMAGE = new Image("/com/aaronzadev/view/icons/error.png");
    private static final Image WARN_IMAGE = new Image("/com/aaronzadev/view/icons/warning.png");
    private static final Image DONE_IMAGE = new Image("/com/aaronzadev/view/icons/done.png");
    
    private static final ImageView IV_INFO = new ImageView(INFO_IMAGE);
    private static final ImageView IV_ERROR = new ImageView(ERROR_IMAGE);
    private static final ImageView IV_WARN = new ImageView(WARN_IMAGE);
    private static final ImageView IV_DONE = new ImageView(DONE_IMAGE);
    
    private static final String CSS_PATH = "/com/aaronzadev/view/css/StyleAlert.css";

    private static final String CASH_DIALOG_PATH = "/com/aaronzadev/view/fxml/dialogs/CashDialogView.fxml";
    
    private static Alert alert;
    
    public static Alert GetInfoAlert (String content) {

        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UTILITY);
        alert.setGraphic(IV_INFO);
        alert.getDialogPane().getStylesheets().add(AlertMaker.class.getResource(CSS_PATH).toExternalForm());
        alert.setTitle("OKAY!");
        alert.setHeaderText("TAREA REALIZADA CORRECTAMENTE!");
        alert.setContentText(content);

        return alert;
    }

    public static Alert GetDoneAlert (String content) {

        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UTILITY);
        alert.setGraphic(IV_DONE);
        alert.getDialogPane().getStylesheets().add(AlertMaker.class.getResource(CSS_PATH).toExternalForm());
        alert.setTitle("HECHO!");
        alert.setHeaderText("TAREA REALIZADA CORRECTAMENTE!");
        alert.setContentText(content);

        return alert;
    }
    
    public static Alert GetErrorAlert (String content) {

        alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UTILITY);
        alert.setGraphic(IV_ERROR);
        alert.getDialogPane().getStylesheets().add(AlertMaker.class.getResource(CSS_PATH).toExternalForm());
        alert.setTitle("OOPSS!");
        alert.setHeaderText("HA OCURRIDO UN ERROR!");
        alert.setContentText(content);

        return alert;
    }
    
    public static Alert GetWarnAlert (String content) {
    
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UTILITY);
        alert.setGraphic(IV_WARN);
        alert.getDialogPane().getStylesheets().add(AlertMaker.class.getResource(CSS_PATH).toExternalForm());
        alert.setTitle("CUIDADO!");
        alert.setHeaderText("DESEA CONTINUAR?");
        alert.setContentText(content);
    
        return alert;
        
    }

    public static Stage GetCashDialog(CashOperation cshOp){

        final FXMLLoader loader = new FXMLLoader(AlertMaker.class.getResource(CASH_DIALOG_PATH));

        final Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Operacion en Caja");
        stage.centerOnScreen();
        stage.setResizable(false);

        try {
            final Parent root = loader.load();
            CashDialogController controller = loader.getController();
            controller.initValues(cshOp);
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stage;
    }
    
}
