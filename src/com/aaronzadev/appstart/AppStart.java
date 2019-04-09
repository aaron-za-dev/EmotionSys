package com.aaronzadev.appstart;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class AppStart extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/com/aaronzadev/view/fxml/LoginView.fxml"));
        primaryStage.setTitle("EmotionSys 1.0 - Iniciar Sesión");
        primaryStage.getIcons().add(new Image("/com/aaronzadev/view/icons/icon64.png"));
        primaryStage.getIcons().add(new Image("/com/aaronzadev/view/icons/icon16.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }
}