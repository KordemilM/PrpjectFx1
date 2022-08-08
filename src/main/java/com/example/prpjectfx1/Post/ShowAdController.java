package com.example.prpjectfx1.Post;

import com.example.prpjectfx1.Holder.IntegerHolder;
import com.example.prpjectfx1.Holder.ManyPostsHolder;
import com.example.prpjectfx1.Holder.PostHolder;
import com.example.prpjectfx1.Holder.UserHolder;
import com.example.prpjectfx1.entity.PostCom;
import com.example.prpjectfx1.entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ShowAdController {
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

    private PostCom postCom;
    private User user;
    public boolean liked = false;

    public void initializePost() throws SQLException, ClassNotFoundException {
        PostHolder postHolder = PostHolder.getINSTANCE();
        postCom = postHolder.getPostCom();
        AppContext.getPostComRepos().addView(postCom,user, AppContext.getConnection());
    }

    public void initializeUser(){
        UserHolder userHolder = UserHolder.getINSTANCE();
        this.user = userHolder.getUser();
    }

    public void main(){
        userName.setText(postCom.getUserName());
        subject.setText(postCom.getSubject());
        content.setText(postCom.getContent());
        likes.setText(String.valueOf(postCom.getLikes()));
        views.setText(String.valueOf(postCom.getViews()));
        date.setText(String.valueOf(postCom.getDate()));
        image.setImage(new Image(postCom.getImage()));
    }

    //like only works once so it is not implemented yet
    public void like() throws SQLException, ClassNotFoundException {
        if(!liked){
            postCom.setLikes(postCom.getLikes()+1);
            liked = true;
            likes.setText(String.valueOf(postCom.getLikes()));
            AppContext.getPostComRepos().likePost(postCom,user, AppContext.getConnection());
        }
    }

    public void addCommentAds() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Post/Explore/Comment/AddComment.fxml"));
        Parent root = loader.load();
        AddCommentController addCommentController = loader.getController();
        PostHolder postHolder = PostHolder.getINSTANCE();
        postHolder.setPostCom(postCom);
        UserHolder userHolder = UserHolder.getINSTANCE();
        userHolder.setUser(user);
        addCommentController.initializePost();
        addCommentController.initializeUser();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add Comment");
        stage.show();
    }

    public void commentsAds() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Post/Explore/Comment/Comments.fxml"));
        Parent root = loader.load();
        CommentsController commentsController = loader.getController();
        IntegerHolder integerHolder = IntegerHolder.getINSTANCE();
        integerHolder.setNum(1);
        integerHolder.setIndex(ManyPostsHolder.instances.size());
        commentsController.initializePageNumber();
        commentsController.initializeIndex();
        ManyPostsHolder manyPostsHolder = ManyPostsHolder.getINSTANCE(ManyPostsHolder.instances.size());
        manyPostsHolder.setPosts(AppContext.getPostComRepos().getChildren(postCom.getId(),AppContext.getConnection()));
        UserHolder userHolder = UserHolder.getINSTANCE();
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
