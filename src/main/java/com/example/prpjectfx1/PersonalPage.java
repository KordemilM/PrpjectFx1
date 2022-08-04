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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.prefs.Preferences;

public class PersonalPage {

    @FXML
    BorderPane borderPane;
    @FXML
    private ImageView profileImage;
    @FXML
    private ImageView settingImage;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label bioLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label numPostLabel;
    @FXML
    private Label numFollowingLabel;
    @FXML
    private Label numFollowersLabel;


    protected void theme(){
        borderPane.getStylesheets().add(getClass().getResource("/com/styles/" +
                (HelloController.isLightMode ? "light" : "dark") + "Mode.css").toExternalForm());
    }


    protected void setUser(String username) throws SQLException {

        User user = UserRepository.searchUser(username);
        usernameLabel.setText(user.getUserName());
        nameLabel.setText(user.getName());
        bioLabel.setText(user.getBio());
        numFollowersLabel.setText(UserRepository.numberOfFollowers(username));
        numFollowingLabel.setText(UserRepository.numberOfFollowing(username));
//        numPostLabel.setText(UserRepository.);
        try{
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(user.getPhoto())));
            profileImage.setImage(image);
        }catch (NullPointerException e){
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/user_icon.png")));
            profileImage.setImage(image);
        }

        Preferences userPreferences = Preferences.userNodeForPackage(PersonalPage.class);
        userPreferences.put("id", usernameLabel.getText());


    }

    @FXML
    private void searchButtonClick(){
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("follow.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 400, 500);
            Stage stage  = Main.mainStage;
            stage.setTitle("");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void editButtonClick(ActionEvent event) throws SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("editProfile.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        EditProfile editProfile = loader.getController();
        editProfile.setProperty(usernameLabel.getText());
        editProfile.theme();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    protected void settingButtonClick(){

    }
}
