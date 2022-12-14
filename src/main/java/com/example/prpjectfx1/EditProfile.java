package com.example.prpjectfx1;

import com.example.prpjectfx1.entity.User;
import com.example.prpjectfx1.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.prefs.Preferences;

public class EditProfile {

    @FXML
    private AnchorPane pane;
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


    protected void theme(){
        pane.getStylesheets().add(getClass().getResource("/com/styles/" +
                (Setting.isLightMode ? "light" : "dark") + "Mode.css").toExternalForm());
    }

    protected void setProperty(String username) throws SQLException {
        User user = UserRepository.searchUser(username);
        usernameText.setText(user.getUserName());
        nameText.setText(user.getName());
        bioText.setText(user.getBio());
        try{
            //Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(user.getPhoto())));
            imageView.setImage(new Image(user.getPhoto()));
            imageView.setClip(new Circle(30,30,30));
        }catch (NullPointerException e){
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/user_icon.png")));
            imageView.setImage(image);
        }
    }

    @FXML
    protected void setMyPhoto() throws MalformedURLException {
        fileChooser.setTitle("My pictures");
        File file = fileChooser.showOpenDialog(null);
//       String address = file.getAbsolutePath();
//       int i = address.lastIndexOf("\\");
//       oooo.setText("/image/" + address.substring(i+1)); // "/image/" + icon.png
        String address = file.toURI().toURL().toString();
        oooo.setText(address);
    }

    @FXML
    protected void edit() throws SQLException {
        User user = new User();
        user.setUserName(usernameText.getText());
        user.setName(nameText.getText());
        user.setBio(bioText.getText());
        if(oooo.getText().equals("")){
            oooo.setText("file:C:/Users/Classic/Documents/Java/Prpject/PrpjectFx1/src/main/resources/image/user_icon.png");
        }
        user.setPhoto(oooo.getText());
        if(account.isSelected()){
            user.setAccount(0);
        }else
            user.setAccount(1);

        UserRepository.updateProfile(user);
        okLabel.setText("edited");
    }

    @FXML
    protected void back1ButtonClick() throws SQLException, ClassNotFoundException {
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
        personalPage.theme();
        Stage stage = Main.mainStage;
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
