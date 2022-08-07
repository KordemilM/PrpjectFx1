package com.example.prpjectfx1.Post;

import com.example.prpjectfx1.Holder.PostsHolder;
import com.example.prpjectfx1.Holder.UserHolder;
import com.example.prpjectfx1.Main;
import com.example.prpjectfx1.PersonalPage;
import com.example.prpjectfx1.Setting;
import com.example.prpjectfx1.entity.PostCom;
import com.example.prpjectfx1.entity.User;
import com.example.prpjectfx1.repository.UserRepository;
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
    }

    public void theme(){
        borderPane.getStylesheets().add(getClass().getResource("/com/styles/" +
                (Setting.isLightMode ? "light" : "dark") + "Mode.css").toExternalForm());
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

    public void toFollow(){
    }

    public void toHome(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
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

    public void toChat(){
    }
}
