package com.example.prpjectfx1;

import com.example.prpjectfx1.entity.User;
import com.example.prpjectfx1.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.prefs.Preferences;

public class Setting {

    public static boolean isLightMode = true;

    @FXML
    private Accordion accordion;
    @FXML
    private TitledPane titledPane;
    @FXML
    private BorderPane borderPane;
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

    protected void setUserInSetting(String username) throws SQLException {

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
    }

    @FXML
    protected void accordionClick(){

    }

    @FXML
    protected void logOutClick() throws SQLException {
        UserRepository.deleteUser(usernameLabel.getText());
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
        Scene scene = new Scene(root);//Main.mainStage;
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void logInClick(){
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
        Scene scene = new Scene(root);//Main.mainStage;
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void changeThemeClick(){
        isLightMode = !isLightMode;
        if(isLightMode){
            setLightMode();
        }else {
            setDarkMode();
        }
    }

    private void setLightMode(){
        borderPane.getStylesheets().remove(getClass().getResource("/com/styles/darkMode.css").toExternalForm());
        borderPane.getStylesheets().add(getClass().getResource("/com/styles/lightMode.css").toExternalForm());
    }

    private void setDarkMode(){
        borderPane.getStylesheets().remove(getClass().getResource("/com/styles/lightMode.css").toExternalForm());
        borderPane.getStylesheets().add(getClass().getResource("/com/styles/darkMode.css").toExternalForm());
    }


    @FXML
    protected void backClick() throws SQLException {
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
