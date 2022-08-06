package com.example.prpjectfx1.Post;

import com.example.prpjectfx1.Holder.UserHolder;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AddPostController {
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
        UserHolder userHolder = UserHolder.getInstance();
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
        postCom.setIsAds(user.getAccount() == 0);
        AppContext.getPostComRepos().addPost(postCom, AppContext.getConnection());
    }

    public void backToMain(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PostMain.fxml"));
        Parent root = loader.load();
        PostMainController controller = loader.getController();
        UserHolder holder = UserHolder.getInstance();
        holder.setUser(user);
        controller.initializeUser();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
