package com.example.prpjectfx1.Post;

import com.example.prpjectfx1.Holder.PostsHolder;
import com.example.prpjectfx1.Holder.UserHolder;
import com.example.prpjectfx1.Main;
import com.example.prpjectfx1.entity.PostCom;
import com.example.prpjectfx1.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class ShowMyPostsController {
    @FXML
    private Label userName;
    @FXML
    private Label subject;
    @FXML
    private Label content;
    @FXML
    private Label likes;
    @FXML
    private Label views;
    @FXML
    private Label date;
    @FXML
    private ImageView image;
    @FXML
    private Label page;
    @FXML
    private Button info;

    private User user;
    private PostCom postCom;
    static int pageNumber;

    public void initializePost() {
        PostsHolder postsHolder = PostsHolder.getInstance();
        if(postsHolder.getPosts().size() >= pageNumber) {
            this.postCom = postsHolder.getPosts().get(pageNumber-1);
        }
    }

    public void initializeUser() {
        UserHolder userHolder = UserHolder.getInstance();
        this.user = userHolder.getUser();
    }

    public void main() throws MalformedURLException {
        userName.setText(postCom.getUserName());
        subject.setText(postCom.getSubject());
        content.setText(postCom.getContent());
        likes.setText("Likes: " + postCom.getLikes());
        views.setText("Views: " + postCom.getViews());
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
        date.setText("Date: " + simpleFormat.format(postCom.getDate()));
        if(!Objects.equals(postCom.getImage(), "") && postCom.getImage()!=null) {
            image.setImage(new Image(postCom.getImage()));
        }
        else {
            image.setImage(null);
        }
        page.setText(Integer.toString(pageNumber));
        if(user.getAccount() == 1){
            info.setVisible(false);
        }
        else info.setVisible(true);
    }

    // next page

    public void nextPage() throws MalformedURLException {
        if(pageNumber < PostsHolder.getInstance().getPosts().size()) {
            pageNumber++;
            initializePost();
            main();
        }
    }

    // previous page

    public void previousPage() throws MalformedURLException {
        if(pageNumber > 1) {
            pageNumber--;
            initializePost();
            main();
        }
    }

    public void info() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("Myposts/ShowLikesViews.fxml"));
        Parent root1 = loader1.load();
        ShowLikesViewsController showLikesViewsController = loader1.getController();
        showLikesViewsController.mainLike(postCom);
        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("Myposts/ShowLikesViews.fxml"));
        Parent root2 = loader2.load();
        ShowLikesViewsController showLikesViewsController2 = loader2.getController();
        showLikesViewsController2.mainView(postCom);
        Scene scene1 = new Scene(root1);
        Stage stage1 = new Stage();
        stage1.setTitle("Likes");
        stage1.setScene(scene1);
        stage1.show();
        Scene scene2 = new Scene(root2);
        Stage stage2 = new Stage();
        stage2.setTitle("Views");
        stage2.setScene(scene2);
        stage2.show();
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PostMain.fxml"));
        Parent root = fxmlLoader.load();
        PostMainController controller = fxmlLoader.getController();
        UserHolder holder = UserHolder.getInstance();
        holder.setUser(user);
        controller.initializeUser();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setTitle("Post Main");
        stage.setScene(scene);
        stage.show();
    }
}
