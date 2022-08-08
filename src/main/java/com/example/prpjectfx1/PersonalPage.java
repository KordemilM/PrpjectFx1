package com.example.prpjectfx1;

import com.example.prpjectfx1.Holder.PostsHolder;
import com.example.prpjectfx1.Holder.UserHolder;
import com.example.prpjectfx1.Messanger.Chats_View_Controller;
import com.example.prpjectfx1.Post.*;
import com.example.prpjectfx1.entity.PostCom;
import com.example.prpjectfx1.entity.User;
import com.example.prpjectfx1.repository.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static com.example.prpjectfx1.Main.OnlineUser;

public class PersonalPage implements Initializable {

    public static String id;

    public static void setIsFromChat(boolean isFromChat) {
        PersonalPage.isFromChat = isFromChat;
    }

    public static boolean isFromChat = false;
    public static void setId(String id) {
        PersonalPage.id = id;
    }

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
    @FXML
    private Label subjectLastPost;
    @FXML
    private Label contentLastPost;
    @FXML
    private ImageView imageLastPost;
    @FXML
    private Label dateLastPost;
    @FXML
    private Label likesLastPost;
    @FXML
    private Label viewsLastPost;


    public void theme(){
        borderPane.getStylesheets().add(getClass().getResource("/com/styles/" +
                (Setting.isLightMode ? "light" : "dark") + "Mode.css").toExternalForm());
    }

    public void setUser(String username) throws SQLException, ClassNotFoundException {

        User user = UserRepository.searchUser(username);
        Chats_View_Controller.user = user;
        assert user != null;
        usernameLabel.setText(user.getUserName());
        OnlineUser = user.getUserName();
        nameLabel.setText(user.getName());
        bioLabel.setText(user.getBio());
        numFollowersLabel.setText(UserRepository.numberOfFollowers(username));
        numFollowingLabel.setText(UserRepository.numberOfFollowing(username));
        numPostLabel.setText(Integer.toString(AppContext.getPostComRepos().getNumberOfPosts(username,AppContext.getConnection())));
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

        try {
            PostCom lastPost = AppContext.getPostComRepos().getLastPost(username,AppContext.getConnection());
            subjectLastPost.setText(lastPost.getSubject());
            contentLastPost.setText(lastPost.getContent());
            imageLastPost.setImage(new Image(lastPost.getImage()));
            dateLastPost.setText(new SimpleDateFormat("yyyy-MM-dd").format(lastPost.getDate()));
            likesLastPost.setText(String.valueOf(lastPost.getLikes()));
            viewsLastPost.setText(String.valueOf(lastPost.getViews()));
        }
        catch(Exception ignored){}
        UserHolder userHolder = UserHolder.getINSTANCE();
        userHolder.setUser(user);

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
    protected void chatButtonClick() throws IOException {

        FXMLLoader loader = new FXMLLoader(Chats_View_Controller.class.getResource("/chat/Chats_View.fxml"));
        Stage stage = Main.mainStage;
        Scene scene = new Scene(loader.load());
        stage.setTitle("");
        stage.setScene(scene);
    } ///ok

    @FXML
    protected void homeButtonClick() throws IOException, SQLException, ClassNotFoundException {
//        FXMLLoader loader =  new FXMLLoader(Objects.requireNonNull(PostMainController.class.getResource("/Post/Recent/Recent.fxml")));
//        Parent root = loader.load();
//        RecentController controller = loader.getController();
//        RecentController.pageNumber = 1;
//        PostsHolder postsHolder = PostsHolder.getInstance();
//        postsHolder.setPosts(AppContext.getPostComRepos().getLast10Post(UserHolder.getINSTANCE().getUser().getUserName(),AppContext.getConnection()));
//        controller.initializePost();
//        controller.initializeUser();
//        controller.main();
//        Scene scene = new Scene(root);
//        Stage stage = Main.mainStage;
//        stage.setScene(scene);
//        stage.show();

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
        postsHolder.setPosts(AppContext.getPostComRepos().getLast10Post(UserHolder.getINSTANCE().getUser().getUserName(),AppContext.getConnection()));
        controller.initializePost();
        controller.initializeUser();
        controller.main();
        Stage stage = Main.mainStage;
        Scene scene = new Scene(root);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void addPostButtonClick(){  ///OK
//        FXMLLoader loader =  new FXMLLoader(Objects.requireNonNull(PostMainController.class.getResource("AddPost/AddPost.fxml")));
//        Parent root = loader.load();
//        AddPostController controller = loader.getController();
//        controller.initializeUser();
//        controller.theme();
//        Scene scene = new Scene(root);
//        Stage stage = Main.mainStage;
//        stage.setScene(scene);
//        stage.show();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Post/AddPost/AddPost.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AddPostController controller = loader.getController();
        controller.initializeUser();
        controller.theme();
        Stage stage = Main.mainStage;
        Scene scene = new Scene(root);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(isFromChat){
            try {
                setUser(id);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            theme();
            setIsFromChat(false);
        }
    }

    @FXML
    public void toMyPosts() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader =  new FXMLLoader(Objects.requireNonNull(PostMainController.class.getResource("/Post/Myposts/ShowMyPosts.fxml")));
        Parent root = loader.load();
        ShowMyPostsController controller = loader.getController();
        controller.initializeUser();
        ShowMyPostsController.pageNumber = 1;
        PostsHolder postsHolder = PostsHolder.getInstance();
        postsHolder.setPosts(AppContext.getPostComRepos().getAllPostsByUser(UserHolder.getINSTANCE().getUser().getUserName(), AppContext.getConnection()));
        controller.initializePost();
        controller.main();
        Scene scene = new Scene(root);
        Stage stage = Main.mainStage;
        stage.setScene(scene);
        stage.show();
    }
}
