package com.example.prpjectfx1;

import com.example.prpjectfx1.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class ForgotPassword {

    @FXML
    private AnchorPane pane;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField question;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label questionLabel;
    @FXML
    private Label question1;
    @FXML
    private Label question2;
    @FXML
    private Button submit;
    @FXML
    private Button login;


    protected void theme(){
        pane.getStylesheets().add(getClass().getResource("/com/styles/" +
                (Setting.isLightMode ? "light" : "dark") + "Mode.css").toExternalForm());
        if(!Setting.isLightMode){
            question1.setTextFill(Paint.valueOf("#ffd9ec"));
            question2.setTextFill(Paint.valueOf("#ffd9ec"));
        }
    }

    @FXML
    protected void submitClick() throws SQLException {
        usernameLabel.setText("");
        questionLabel.setText("");
        int i = 0;
        if(UserRepository.searchUserByUsername(username.getText())){
            i++;
        }else {
            username.setText("");
            usernameLabel.setText("no user exists with this username");
        }
        if(UserRepository.searchUserByUsernameAndQuestion(username.getText(),question.getText())){
            i++;
        }else {
            question.setText("");
            questionLabel.setText("your answer is not correct");
        }

        if(i==2){
            submit.setVisible(false);     login.setVisible(true);
            username.setDisable(false);   question.setDisable(false);
            question1.setDisable(false);  question2.setDisable(false);
        }
    }

    @FXML
    protected void loginClick() throws SQLException {
        passwordLabel.setText("");
        if(password.getText().length()>=8){
            UserRepository.updatePassword(username.getText(),password.getText());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("personalPage.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            PersonalPage personalPage = loader.getController();
            personalPage.setUser(username.getText());
            personalPage.theme();
            Stage stage = Main.mainStage;
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }else {
            password.setText("");
            passwordLabel.setText("the length of the password must be at least 8!");
        }
    }

}