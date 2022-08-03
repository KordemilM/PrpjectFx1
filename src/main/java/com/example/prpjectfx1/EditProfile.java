package com.example.prpjectfx1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

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
    private Label okLabel;
    @FXML
    private Label oooo;
    @FXML
    private ImageView imageView;

    final FileChooser fileChooser = new FileChooser();

    protected void setProperty(String username) throws SQLException {
        User user = UserRepository.searchUser(username);
        usernameText.setText(user.getUserName());
        nameText.setText(user.getName());
        bioText.setText(user.getBio());
        if(user.getPhoto() != null){
            Image image = new Image(getClass().getResourceAsStream(user.getPhoto()));
            imageView.setImage(image);
        }else {
            Image image = new Image(getClass().getResourceAsStream("/image/profile.png"));
            imageView.setImage(image);
        }
//        /image/icon.png
    }

    @FXML
    protected void setMyPhoto(){
        fileChooser.setTitle("My pictures");
        File file = fileChooser.showOpenDialog(null);
        String address = file.getAbsolutePath();
        int i = address.lastIndexOf("\\");
        oooo.setText("/image/" + address.substring(i+1));
        // icon.png
    }

    @FXML
    protected void edit() throws SQLException {
        User user = new User();
        user.setUserName(usernameText.getText());
        user.setName(nameText.getText());
        user.setBio(bioText.getText());
        user.setPhoto(oooo.getText());
        if(account.isSelected()){
            user.setAccount(0);
        }else
            user.setAccount(1);

        UserRepository.updateProfile(user);
        okLabel.setText("edited");
    }

    @FXML
    public void back1ButtonClick() throws SQLException {
        Preferences userPreferences = Preferences.userNodeForPackage(PersonalPage.class);
        String id = userPreferences.get("id", "");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("personalPage.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PersonalPage personalPage = loader.getController();
        personalPage.setUser(id);
        Stage stage = Main.mainStage;   ///
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
