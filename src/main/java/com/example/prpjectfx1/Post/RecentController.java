package com.example.prpjectfx1.Post;

import com.example.prpjectfx1.Holder.*;
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

public class RecentController {
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
    private Button likeRecent;
    @FXML
    private Button addComRecent;
    @FXML
    private Button commentsAds;


    private PostCom postCom;
    private User user;
    public static int pageNumber;

    //initialize the page with the postCom and pageNumber
    public void initializePost() {
        PostsHolder postsHolder = PostsHolder.getInstance();
        if(postsHolder.getPosts().size() >= pageNumber) {
            this.postCom = postsHolder.getPosts().get(pageNumber-1);
        }
    }

    public void initializeUser() {
        UserHolder userHolder = UserHolder.getInstance();
        user = userHolder.getUser();
    }

    public void main(){
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
        if(user.getUserName().equals(postCom.getUserName())) {
            addComRecent.setVisible(false);
            likeRecent.setVisible(false);
            commentsAds.setVisible(false);
        }
        else {
            addComRecent.setVisible(true);
            likeRecent.setVisible(true);
            commentsAds.setVisible(true);
        }
    }

    //next page

    public void nextPage() throws MalformedURLException {
        pageNumber++;
        initializePost();
        main();
    }

    //previous page

    public void previousPage() throws MalformedURLException {
        if(pageNumber > 1) {
            pageNumber--;
            initializePost();
            main();
        }
    }

    //back to the main page

    public void back(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PostMain.fxml"));
        Parent root = fxmlLoader.load();
        PostMainController controller = fxmlLoader.getController();
        controller.initializeUser();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setTitle("Post Main");
        stage.setScene(scene);
        stage.show();
    }

    //like the post

    public void likePost() throws SQLException, ClassNotFoundException {
        if(!AppContext.getPostComRepos().isLiked(postCom,user,AppContext.getConnection())) {
            AppContext.getPostComRepos().likePost(postCom, user, AppContext.getConnection());
            postCom.setLikes(postCom.getLikes()+1);
            likes.setText(String.valueOf(postCom.getLikes()));
        }
    }

    //add comment to the post

    public void addComment() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Explore/Comment/AddComment.fxml"));
        Parent root = loader.load();
        AddCommentController addCommentController = loader.getController();
        PostHolder postHolder = PostHolder.getInstance();
        postHolder.setPostCom(postCom);
        UserHolder userHolder = UserHolder.getInstance();
        userHolder.setUser(user);
        addCommentController.initializePost();
        addCommentController.initializeUser();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add Comment");
        stage.show();
    }

    //show all comments of the post

    public void showComments() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Explore/Comment/Comments.fxml"));
        Parent root = loader.load();
        CommentsController commentsController = loader.getController();
        IntegerHolder integerHolder = IntegerHolder.getInstance();
        integerHolder.setNum(1);
        integerHolder.setIndex(ManyPostsHolder.instances.size());
        commentsController.initializePageNumber();
        commentsController.initializeIndex();
        ManyPostsHolder manyPostsHolder = ManyPostsHolder.getInstance(ManyPostsHolder.instances.size());
        manyPostsHolder.setPosts(AppContext.getPostComRepos().getChildren(postCom.getId(),AppContext.getConnection()));
        UserHolder userHolder = UserHolder.getInstance();
        userHolder.setUser(user);
        commentsController.initializePost();
        commentsController.initializeUser();
        commentsController.initializeCom();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setOnHidden(e -> commentsController.cleanUp());
        stage.setTitle("Comments");
        stage.show();
    }
}