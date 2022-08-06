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
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.prefs.Preferences;

import static com.example.prpjectfx1.Main.OnlineUser;

public class PersonalPage {

    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView profileImage;
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


    public void theme(){
        borderPane.getStylesheets().add(getClass().getResource("/com/styles/" +
                (Setting.isLightMode ? "light" : "dark") + "Mode.css").toExternalForm());
    }

    public void setUser(String username) throws SQLException {

        User user = UserRepository.searchUser(username);
        assert user != null;
        usernameLabel.setText(user.getUserName());
        OnlineUser = user.getUserName();
        nameLabel.setText(user.getName());
        bioLabel.setText(user.getBio());
        numFollowersLabel.setText(UserRepository.numberOfFollowers(username));
        numFollowingLabel.setText(UserRepository.numberOfFollowing(username));
//        numPostLabel.setText(UserRepository.);
        try{
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(user.getPhoto())));
            profileImage.setImage(image);
            profileImage.setClip(new Circle(25,25,25));
        }catch (NullPointerException e){
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/user_icon.png")));
            profileImage.setImage(image);
        }

        Preferences userPreferences = Preferences.userNodeForPackage(PersonalPage.class);
        userPreferences.put("id", usernameLabel.getText());

    }

    @FXML
    private void searchButtonClick(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("follow.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Follow follow = loader.getController();
        follow.theme();
        Stage stage = Main.mainStage;
        Scene scene = new Scene(root);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
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

    @FXML
    protected void settingButtonClick() throws SQLException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("setting.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Setting setting = loader.getController();
        setting.setUserInSetting(usernameLabel.getText());
        setting.theme();
        Stage stage = Main.mainStage;
        Scene scene = new Scene(root);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void chatButtonClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/chat/Chats_View.fxml"));
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
    protected void homeButtonClick() {
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Chats_View.fxml"));
//        try {
//            Scene scene = new Scene(fxmlLoader.load(), 400, 500);
//            Stage stage  = Main.mainStage;
//            stage.setTitle("");
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @FXML
    protected void addPostButtonClick() {

    }

}
