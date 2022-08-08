package com.example.prpjectfx1.Post;

import com.example.prpjectfx1.Follow;
import com.example.prpjectfx1.Holder.PostsHolder;
import com.example.prpjectfx1.Holder.UserHolder;
import com.example.prpjectfx1.Main;
import com.example.prpjectfx1.Messanger.Chats_View_Controller;
import com.example.prpjectfx1.PersonalPage;
import com.example.prpjectfx1.Setting;
import com.example.prpjectfx1.entity.PostCom;
import com.example.prpjectfx1.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.prefs.Preferences;

public class AddPostController {

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField postSubject;
    @FXML
    private ImageView postImage;
    @FXML
    private TextField postContent;

    final FileChooser fileChooser = new FileChooser();

    private User user;
    private String imagePath;

    public void initializeUser() {
        UserHolder userHolder = UserHolder.getINSTANCE();
        this.user = userHolder.getUser();
    }

    public void chooseImage() throws MalformedURLException {
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath = file.toURI().toURL().toString();
            postImage.setImage(new Image(imagePath));
        }
        else imagePath = "";
    }

    public void addPost() throws SQLException, ClassNotFoundException {
        PostCom postCom = new PostCom();
        postCom.setUserName(user.getUserName());
        postCom.setSubject(postSubject.getText());
        postCom.setContent(postContent.getText());
        postCom.setDate(Timestamp.valueOf(java.time.LocalDateTime.now()));
        postCom.setLikes(0);
        postCom.setParent(0);
        postCom.setImage(imagePath);
        postCom.setViews(0);
        postCom.setAds(user.getAccount() == 0);
        AppContext.getPostComRepos().addPost(postCom, AppContext.getConnection());
        postSubject.setText("");
        postContent.setText("");
        postImage.setImage(null);
    }

    public void theme(){
        borderPane.getStylesheets().add(getClass().getResource("/com/styles/" +
                (Setting.isLightMode ? "light" : "dark") + "Mode.css").toExternalForm());
    }

    @FXML
    protected void PersonalPageClick() throws SQLException, ClassNotFoundException {
        Preferences userPreferences = Preferences.userNodeForPackage(PersonalPage.class);
        String id = userPreferences.get("id", "");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/prpjectfx1/personalPage.fxml"));
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
    private void toFollow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/prpjectfx1/follow.fxml"));
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
