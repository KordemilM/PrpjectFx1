package com.example.prpjectfx1;

import com.example.prpjectfx1.entity.User;
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

public class SignUp {

    @FXML
    private AnchorPane pane;
    @FXML
    private PasswordField passwordText;
    @FXML
    private PasswordField repeatPasswordText;
    @FXML
    private TextField usernameText;
    @FXML
    private TextField emailText;
    @FXML
    private TextField accountText;
    @FXML
    private TextField securityText;
    @FXML
    private Label emailLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label repeatPasswordLabel;
    @FXML
    private Label accountLabel;
    @FXML
    private Label securityLabel;
    @FXML
    private Label submitLabel;
    @FXML
    private Label businessLabel;
    @FXML
    private Label questionLabel1;
    @FXML
    private Label questionLabel2;

    protected void theme(){
        pane.getStylesheets().add(getClass().getResource("/com/styles/" +
                (Setting.isLightMode ? "light" : "dark") + "Mode.css").toExternalForm());
        if(!Setting.isLightMode){
            businessLabel.setTextFill(Paint.valueOf("#ffd9ec"));
            questionLabel1.setTextFill(Paint.valueOf("#ffd9ec"));
            questionLabel2.setTextFill(Paint.valueOf("#ffd9ec"));
        }
    }

    @FXML
    protected void submitButtonClick(ActionEvent event) throws SQLException, ClassNotFoundException {
        emailLabel.setText("");           usernameLabel.setText("");   passwordLabel.setText("");
        repeatPasswordLabel.setText("");  accountLabel.setText("");    securityLabel.setText("");

        int i = 0;
        User user = new User();

        if(emailText.getText().matches("\\S+@(\\w+)\\.com")){
            user.setEmail(emailText.getText());
            i++;
        }else {
            emailText.setText("");
            emailLabel.setText("your email is not valid!");
        }
        if(UserRepository.searchUserByUsername(usernameText.getText())){
            user.setUserName(usernameText.getText());
            i++;
        }else {
            usernameText.setText("");
            usernameLabel.setText("a user exists with this username!");
        }

        if(passwordText.getText().length()>=8){
            user.setPassword(passwordText.getText());
            i++;
        }else {
            passwordText.setText("");
            passwordLabel.setText("the length of the password must be at least 8!");
        }
        if(repeatPasswordText.getText().equals(passwordText.getText())){
            i++;
        }else {
            repeatPasswordText.setText("");
            repeatPasswordLabel.setText("try again");
        }
        if(accountText.getText().equalsIgnoreCase("yes") ||
                accountText.getText().equalsIgnoreCase("no")){
            i++;
            if(accountText.getText().equalsIgnoreCase("yes"))
                user.setAccount(0);
            else
                user.setAccount(1);
        }else {
            accountText.setText("");
            accountLabel.setText("answer with yes or no");
        }
        if(!securityText.getText().equals("")){
            user.setSecurityResponse(securityText.getText());
            i++;
        }else securityLabel.setText("!!!");

        if(i==6){

            //submitLabel.setText("sign up was successful:))");
            //time

            UserRepository.addUser(user);
            Main.OnlineUser = usernameLabel.getText();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("personalPage.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            PersonalPage personalPage = loader.getController();
            personalPage.setUser(usernameText.getText());
            personalPage.theme();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    protected void backButtonClick(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("logIn.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogIn logIn = loader.getController();
        logIn.theme();
        Stage stage = Main.mainStage;
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
