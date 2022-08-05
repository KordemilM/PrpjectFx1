package com.example.prpjectfx1;

import com.example.prpjectfx1.entity.User;
import com.example.prpjectfx1.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class Follow {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField searchText;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label numFollowersLabel;
    @FXML
    private Label numFollowingLabel;
    @FXML
    private Label numPostLabel;
    @FXML
    private Label bioLabel;
    @FXML
    private Label noUserLabel;
    @FXML
    private Label followingLabel;
    @FXML
    private Label followerLabel;
    @FXML
    private Label postLabel;
    @FXML
    private Button followButton;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button button7;

    protected void theme(){
        borderPane.getStylesheets().add(getClass().getResource("/com/styles/" +
                (Setting.isLightMode ? "light" : "dark") + "Mode.css").toExternalForm());
    }

    @FXML
    protected void searchFollower() throws SQLException {
        noUserLabel.setText("");
        usernameLabel.setText("");
        nameLabel.setText("");
        bioLabel.setText("");
        numFollowersLabel.setText("");
        numFollowingLabel.setText("");
        followerLabel.setText("");
        followingLabel.setText("");
        postLabel.setText("");

//        numPostLabel.setText("");

        if(!UserRepository.searchUserByUsername(searchText.getText()) && !searchText.getText().equals("")){
            User user = UserRepository.searchUser(searchText.getText());
            usernameLabel.setText(user.getUserName());
            nameLabel.setText(user.getName());
            bioLabel.setText(user.getBio());
            numFollowersLabel.setText(UserRepository.numberOfFollowers(searchText.getText()));
            numFollowingLabel.setText(UserRepository.numberOfFollowing(searchText.getText()));
            followerLabel.setText("Followers");
            followingLabel.setText("Following");
            postLabel.setText("Post");
            followButton.setVisible(true);

//            numPostLabel.setText();
        }else {
            noUserLabel.setText("no user exists with this Id");
        }
    }

    @FXML
    protected void followButtonClick() throws SQLException {
        noUserLabel.setText("");
        Preferences userPreferences = Preferences.userNodeForPackage(PersonalPage.class);
        String id = userPreferences.get("id", "");

        if(UserRepository.findFollowedOrNO(id,searchText.getText())){
            UserRepository.addFollowing(id,searchText.getText());
            noUserLabel.setText("Now you follow "+ searchText.getText());
        } else
            noUserLabel.setText("you used to follow " + searchText.getText());
        followButton.setVisible(false);
    }

    @FXML
    protected void PersonalPageClick() throws SQLException {
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

    @FXML
    protected void userSuggestionClick() throws SQLException {
        Preferences userPreferences = Preferences.userNodeForPackage(PersonalPage.class);
        String id = userPreferences.get("id", "");
        ArrayList<String> users = UserRepository.userSuggestion(id);
        if(users.size()>0){
            scrollPane.setVisible(true);
            button1.setText(users.get(0));  button2.setText(users.get(1));  button3.setText(users.get(2));
            button4.setText(users.get(3));  button5.setText(users.get(4));  button6.setText(users.get(5));
            button7.setText(users.get(6));
        }
    }

    @FXML
    protected void button1Click(){
        if(!button1.getText().equals("")){
            searchText.setText(button1.getText());
        }
    }
    @FXML
    protected void button2Click(){
        if(!button2.getText().equals("")){
            searchText.setText(button2.getText());
        }
    }
    @FXML
    protected void button3Click(){
        if(!button3.getText().equals("")){
            searchText.setText(button3.getText());
        }
    }
}
