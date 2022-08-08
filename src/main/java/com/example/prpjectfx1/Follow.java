package com.example.prpjectfx1;

import com.example.prpjectfx1.Holder.IntegerHolder;
import com.example.prpjectfx1.Holder.PostHolder;
import com.example.prpjectfx1.Holder.PostsHolder;
import com.example.prpjectfx1.Holder.UserHolder;
import com.example.prpjectfx1.Messanger.Chats_View_Controller;
import com.example.prpjectfx1.Post.*;
import com.example.prpjectfx1.entity.User;
import com.example.prpjectfx1.repository.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
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
    @FXML
    private ImageView profileImage;


    public void theme(){
        borderPane.getStylesheets().add(getClass().getResource("/com/styles/" +
                (Setting.isLightMode ? "light" : "dark") + "Mode.css").toExternalForm());
    }

    @FXML
    protected void searchFollower() throws SQLException, ClassNotFoundException {
        noUserLabel.setText("");
        usernameLabel.setText("");
        nameLabel.setText("");
        bioLabel.setText("");
        numFollowersLabel.setText("");
        numFollowingLabel.setText("");
        followerLabel.setText("");
        followingLabel.setText("");
        postLabel.setText("");

        numPostLabel.setText("");

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
            try{
                profileImage.setImage(new Image(user.getPhoto()));
                profileImage.setClip(new Circle(25,25,25));
            }catch (NullPointerException e){
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/user_icon.png")));
                profileImage.setImage(image);
            }

            numPostLabel.setText(Integer.toString(AppContext.getPostComRepos().getNumberOfPosts(user.getUserName(),AppContext.getConnection())));
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
            if(UserRepository.addFollowing(id,searchText.getText())){
                noUserLabel.setText("Now you follow "+ searchText.getText());
            }else {
                noUserLabel.setText("you can't follow yourself!");
            }
        } else
            noUserLabel.setText("you used to follow " + searchText.getText());
        followButton.setVisible(false);
    }

    @FXML
    protected void PersonalPageClick() throws SQLException, ClassNotFoundException {
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
        if(users.size()>0) {
            scrollPane.setVisible(true);
            button1.setText(users.get(0));
        }
        if(users.size()==2){
            button2.setText(users.get(1));
        }
        if(users.size()==3){
            button2.setText(users.get(2));
        }
        if(users.size()==3){
            button2.setText(users.get(3));
        }
        if(users.size()==3){
            button2.setText(users.get(4));
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

    @FXML
    public void toExplore() throws SQLException, ClassNotFoundException {
        Preferences userPreferences = Preferences.userNodeForPackage(PersonalPage.class);
        String id = userPreferences.get("id", "");
        User user = UserRepository.searchUser(id);

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(PostMainController.class.getResource("/Post/Explore/ShowAd.fxml")));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ShowAdController controller = loader.getController();
        UserHolder holder = UserHolder.getINSTANCE();
        holder.setUser(user);
        controller.initializeUser();
        PostHolder postHolder = PostHolder.getINSTANCE();
        postHolder.setPostCom(AppContext.getPostComRepos().getRandomAdsPost(user.getUserName(), AppContext.getConnection()));
        controller.initializePost();
        controller.main();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Ad");
        stage.show();

//        try {
//            FXMLLoader loader2 = new FXMLLoader(Objects.requireNonNull(PostMainController.class.getResource("/Post/Explore/Explore.fxml")));
//            Parent root2 = loader2.load();
//            IntegerHolder holder2 = IntegerHolder.getINSTANCE();
//            holder2.setNum(1);
//            ExploreController controller2 = loader2.getController();
//            controller2.initializePageNumber();
//            controller2.initializeUser();
//            PostsHolder postsHolder = PostsHolder.getInstance();
//            postsHolder.setPosts(AppContext.getPostComRepos().getAllPosts(user.getUserName(), AppContext.getConnection()));
//            controller2.initializePost();
//            controller2.main();
//            Scene scene2 = new Scene(root2);
//            Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage2.setScene(scene2);
//            stage2.show();
//        }
//        catch (Exception ignored) {}

    }

    public void toAddPost() throws IOException, SQLException {   ///OK
        Preferences userPreferences = Preferences.userNodeForPackage(PersonalPage.class);
        String id = userPreferences.get("id", "");
        User user = UserRepository.searchUser(id);

        FXMLLoader loader =  new FXMLLoader(Objects.requireNonNull(PostMainController.class.getResource("/Post/AddPost/AddPost.fxml")));
        Parent root = loader.load();
        AddPostController controller = loader.getController();
        UserHolder holder = UserHolder.getINSTANCE();
        holder.setUser(user);
        controller.initializeUser();
        controller.theme();
        Scene scene = new Scene(root);
        Stage stage = Main.mainStage;
        stage.setScene(scene);
        stage.show();
    }

    public void toHomePage(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        Preferences userPreferences = Preferences.userNodeForPackage(PersonalPage.class);
        String id = userPreferences.get("id", "");
        User user = UserRepository.searchUser(id);

        FXMLLoader loader =  new FXMLLoader(Objects.requireNonNull(PostMainController.class.getResource("Recent/Recent.fxml")));
        Parent root = loader.load();
        RecentController controller = loader.getController();
        RecentController.pageNumber = 1;
        PostsHolder postsHolder = PostsHolder.getInstance();
        postsHolder.setPosts(AppContext.getPostComRepos().getLast10Post(user.getUserName(), AppContext.getConnection()));
        controller.initializePost();
        UserHolder holder = UserHolder.getINSTANCE();
        holder.setUser(user);
        controller.initializeUser();
        controller.main();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void toChat() throws IOException {  //OK

        FXMLLoader loader = new FXMLLoader(Chats_View_Controller.class.getResource("/chat/Chats_View.fxml"));
        Stage stage = Main.mainStage;
        Scene scene = new Scene(loader.load());
        stage.setTitle("");
        stage.setScene(scene);
    }

    @FXML
    protected void homeButtonClick() throws IOException, SQLException, ClassNotFoundException {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(PostMainController.class.getResource("/Post/Recent/Recent.fxml")));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            RecentController controller = loader.getController();
            RecentController.pageNumber = 1;
            PostsHolder postsHolder = PostsHolder.getInstance();
            postsHolder.setPosts(AppContext.getPostComRepos().getLast10Post(UserHolder.getINSTANCE().getUser().getUserName(), AppContext.getConnection()));
            controller.initializePost();
            controller.initializeUser();
            controller.main();
            Stage stage = Main.mainStage;
            Scene scene = new Scene(root);
            stage.setTitle("");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception ignored){}
    }
}
