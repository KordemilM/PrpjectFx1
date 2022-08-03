package com.example.prpjectfx1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class ForgotPassword {

    @FXML
    Label lab;
    @FXML
    BorderPane parent;

    private boolean isLightMode = true;

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
        parent.getStylesheets().remove("/styles/darkMode.css");
        parent.getStylesheets().add("/styles/lightMode.css");
    }

    private void setDarkMode(){
        parent.getStylesheets().remove("/styles/lightMode.css");
        parent.getStylesheets().add("/styles/darkMode.css");
    }
}
