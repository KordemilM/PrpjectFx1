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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class CommentsController {
    @FXML
    private Label userNameCom;
    @FXML
    private Label contentCom;
    @FXML
    private Label dateCom;
    @FXML
    private Label likesCom;
    @FXML
    private Label pageCom;
    @FXML
    private Label subjectCom;

    private PostCom postCom;
    private User user;
    int pageNumber;
    int index;

    public void initializePost() {
        ManyPostsHolder manyPostsHolder = ManyPostsHolder.getInstance(index);
        if(manyPostsHolder.getPosts().size() >= pageNumber) {
            postCom = manyPostsHolder.getPosts().get(pageNumber-1);
        }
    }

    public void initializeUser() {
        UserHolder userHolder = UserHolder.getInstance();
        this.user = userHolder.getUser();
    }

    public void initializePageNumber(){
        IntegerHolder integerHolder = IntegerHolder.getInstance();
        this.pageNumber = integerHolder.getNum();
    }

    public void initializeIndex(){
        IntegerHolder integerHolder = IntegerHolder.getInstance();
        this.index = integerHolder.getIndex();
    }

    public void initializeCom() {
        if(postCom != null) {
            userNameCom.setText(postCom.getUserName());
            contentCom.setText(postCom.getContent());
            dateCom.setText(new SimpleDateFormat("yyyy-MM-dd").format(postCom.getDate()));
            likesCom.setText("Likes: "+ postCom.getLikes());
            subjectCom.setText(postCom.getSubject());
            pageCom.setText(String.valueOf(pageNumber));
        }
    }

    // like comment

    public void likeCom() throws SQLException, ClassNotFoundException {
        if(!AppContext.getPostComRepos().isLiked(postCom, user, AppContext.getConnection())) {
            AppContext.getPostComRepos().likePost(postCom, user, AppContext.getConnection());
            postCom.setLikes(postCom.getLikes()+1);
            likesCom.setText("Likes: "+ postCom.getLikes());
        }
    }

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

    public void commentsCom() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Explore/Comment/Comments.fxml"));
        Parent root = loader.load();
        IntegerHolder integerHolder = IntegerHolder.getInstance();
        integerHolder.setNum(1);
        integerHolder.setIndex(index+1);
        CommentsController commentsController = loader.getController();
        commentsController.initializePageNumber();
        commentsController.initializeIndex();
        ManyPostsHolder manyPostsHolder = ManyPostsHolder.getInstance(index+1);
        manyPostsHolder.setPosts(AppContext.getPostComRepos().getChildren(postCom.getId(), AppContext.getConnection()));
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

    public void nextCom() {
        if(ManyPostsHolder.getInstance(index).getPosts().size() > pageNumber) {
            pageNumber++;
            initializePost();
            initializeCom();
        }
    }

    public void previousCom() {
        if(pageNumber > 1) {
            pageNumber--;
            initializePost();
            initializeCom();
        }
    }

    public void cleanUp(){
        ManyPostsHolder.instances.remove(index);
    }
}
