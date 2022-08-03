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
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

public class LogIn {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;

    @FXML
    protected void SignUpClick(){
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("signUp.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 400, 500);
            Stage stage  = Main.mainStage;
            stage.setTitle("Sign up");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if(UserRepository.searchPassword(usernameField.getText(),passwordField.getText())){
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
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    protected void forgotPasswordClick(){
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("forgotPassword.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 400, 500);
            Stage stage  = Main.mainStage;
            stage.setTitle("forgot Password");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
