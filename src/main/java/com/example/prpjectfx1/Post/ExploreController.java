package com.example.prpjectfx1.Post;


import com.example.prpjectfx1.Follow;
import com.example.prpjectfx1.Holder.*;
import com.example.prpjectfx1.Main;
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
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class ExploreController {
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

    private PostCom postCom;
    private User user;
    int pageNumber;

    public void initializePost() throws SQLException, ClassNotFoundException {
        PostsHolder postsHolder = PostsHolder.getInstance();
        if(postsHolder.getPosts().size() >= pageNumber) {
            postCom = postsHolder.getPosts().get(pageNumber-1);
            AppContext.getPostComRepos().addView(postCom,user, AppContext.getConnection());
        }
    }

    public void initializeUser() {
        UserHolder userHolder = UserHolder.getINSTANCE();
        this.user = userHolder.getUser();
    }

    public void initializePageNumber(){
        IntegerHolder integerHolder = IntegerHolder.getINSTANCE();
        this.pageNumber = integerHolder.getNum();
    }

    public void main() throws MalformedURLException {
        userName.setText(postCom.getUserName());
        subject.setText(postCom.getSubject());
        content.setText(postCom.getContent());
        likes.setText(String.valueOf(postCom.getLikes()));
        views.setText(String.valueOf(postCom.getViews()));
        date.setText(new SimpleDateFormat("yyyy-MM-dd").format(postCom.getDate()));
        if(!Objects.equals(postCom.getImage(), "") && postCom.getImage()!=null) {
            image.setImage(new Image(postCom.getImage()));
        }
        else image.setImage(null);
        page.setText(String.valueOf(pageNumber));
    }

    public void nextPost() throws MalformedURLException, SQLException, ClassNotFoundException {
        if(PostsHolder.getInstance().getPosts().size() > pageNumber) {
            pageNumber++;
            initializePost();
            main();
        }
    }

    public void previousPost() throws MalformedURLException, SQLException, ClassNotFoundException {
        if(pageNumber > 1) {
            pageNumber--;
            initializePost();
            main();
        }
    }

    public void likesUserName() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Explore/Likes.fxml"));
        Parent root = loader.load();
        LikeExploreController likesController = loader.getController();
        PostHolder postHolder = PostHolder.getINSTANCE();
        postHolder.setPostCom(postCom);
        likesController.initializePost();
        likesController.mainLikesUserName();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Likes");
        stage.show();
    }



    public void likePost() throws SQLException, ClassNotFoundException {
        if(!AppContext.getPostComRepos().isLiked(postCom,user,AppContext.getConnection())) {
            AppContext.getPostComRepos().likePost(postCom, user, AppContext.getConnection());
            postCom.setLikes(postCom.getLikes()+1);
            likes.setText(String.valueOf(postCom.getLikes()));
        }
    }

    public void addCommentExplore() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Explore/Comment/AddComment.fxml"));
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

    public void commentsExplore() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Explore/Comment/Comments.fxml"));
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
}
