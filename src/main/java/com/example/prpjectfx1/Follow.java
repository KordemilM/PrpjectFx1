package com.example.prpjectfx1;

import com.example.prpjectfx1.entity.User;
import com.example.prpjectfx1.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.util.prefs.Preferences;

public class Follow {

    @FXML
    private TextField searchText;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label numFollowerLabel;
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
    private Label hmmm;
    @FXML
    private Button followButton;


    @FXML
    protected void searchFollower() throws SQLException {
        usernameLabel.setText("");
        nameLabel.setText("");
        bioLabel.setText("");
        numFollowerLabel.setText("");
        numFollowingLabel.setText("");
        followButton.setVisible(false);
        followerLabel.setText("");
        followingLabel.setText("");
        postLabel.setText("");

//        numPostLabel.setText("");

        if(!UserRepository.searchUserByUsername(searchText.getText())){
            User user = new User();
            user = UserRepository.searchUser(searchText.getText());
            usernameLabel.setText(user.getUserName());
            nameLabel.setText(user.getName());
            bioLabel.setText(user.getBio());
            numFollowerLabel.setText(UserRepository.numberOfFollowers(searchText.getText()));
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
        Preferences userPreferences = Preferences.userNodeForPackage(PersonalPage.class);
        String id = userPreferences.get("id", "");

        if(UserRepository.findFollow(id,usernameLabel.getText())){
            UserRepository.addFollower(id,usernameLabel.getText());
            hmmm.setText("Now you follow "+ usernameLabel.getText());
        } else
            hmmm.setText("you used to follow " + usernameLabel.getText());
    }
}
