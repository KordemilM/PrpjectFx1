package com.example.prpjectfx1;

import com.example.prpjectfx1.repository.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

public class LogIn {

    @FXML
    private AnchorPane pane;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label forgotLabel;
    @FXML
    private Label signupLabel;


    protected void theme(){
        pane.getStylesheets().add(getClass().getResource("/com/styles/" +
                (Setting.isLightMode ? "light" : "dark") + "Mode.css").toExternalForm());
        if(!Setting.isLightMode){
            forgotLabel.setTextFill(Paint.valueOf("#ffd9ec"));
            signupLabel.setTextFill(Paint.valueOf("#ffd9ec"));
        }
    }

    @FXML
    protected void SignUpClick(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signUp.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SignUp signUp = loader.getController();
        signUp.theme();
        Stage stage = Main.mainStage;
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void submitClick(ActionEvent event) throws SQLException {
        usernameLabel.setText("");
        passwordLabel.setText("");
        int i = 0;
        if(!UserRepository.searchUserByUsername(usernameField.getText())){
            i++;
            Preferences userPreferences = Preferences.userNodeForPackage(LogIn.class);
            userPreferences.put("id", usernameField.getText());
        }else {
            usernameLabel.setText("no user exists with this username");
            usernameField.setText("");
        }
        if(UserRepository.searchUserByUsernameAndPassword(usernameField.getText(),passwordField.getText())){
            i++;
        }else {
            passwordLabel.setText("Your password is not correct");
            passwordField.setText("");
        }
        if(i==2){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("personalPage.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            PersonalPage personalPage = loader.getController();
            personalPage.setUser(usernameField.getText());
            personalPage.theme();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    protected void forgotPasswordClick(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("forgotPassword.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ForgotPassword forgotPassword = loader.getController();
        forgotPassword.theme();
        Stage stage = Main.mainStage;
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
