package com.example.prpjectfx1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloController {

    public static boolean isLightMode = true;

    @FXML
    private AnchorPane pane;


    @FXML
    public void changeMode(){
        isLightMode = !isLightMode;
        if(isLightMode){
            setLightMode();
        }else {
            setDarkMode();
        }
    }

    private void setLightMode(){
        pane.getStylesheets().remove(getClass().getResource("/com/styles/darkMode.css").toExternalForm());
        pane.getStylesheets().add(getClass().getResource("/com/styles/lightMode.css").toExternalForm());
    }

    private void setDarkMode(){
        pane.getStylesheets().remove(getClass().getResource("/com/styles/lightMode.css").toExternalForm());
        pane.getStylesheets().add(getClass().getResource("/com/styles/darkMode.css").toExternalForm());
    }

    @FXML
    protected void onStartButtonClick(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("logIn.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogIn logIn = loader.getController();
        logIn.theme();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);//Main.mainStage;
        stage.setScene(scene);
        stage.show();
    }

}