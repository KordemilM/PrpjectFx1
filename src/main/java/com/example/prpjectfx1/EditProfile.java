package com.example.prpjectfx1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class EditProfile {

    @FXML
    private TextField usernameText;
    @FXML
    private TextField nameText;
    @FXML
    private TextField bioText;
    @FXML
    private CheckBox account;
    @FXML
    Label okLabel;

    protected void hmm2(String username) throws SQLException {
        User user = UserRepository.searchUser(username);
        usernameText.setText(user.getUserName());
        nameText.setText(user.getName());
        bioText.setText(user.getBio());
    }

    @FXML
    protected void edit() throws SQLException {
        User user = new User();
        user.setUserName(usernameText.getText());
        user.setName(nameText.getText());
        user.setBio(bioText.getText());
        if(account.isSelected()){
            user.setAccount(0);
        }else
            user.setAccount(1);

        UserRepository.updateProfile(user);
        okLabel.setText("edited");
    }

    @FXML
    protected void backButtonClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("personalPage.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 400, 500);
            Stage stage = Main.mainStage;
            stage.setTitle("");
            stage.setScene(scene);
            stage.show();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

}
