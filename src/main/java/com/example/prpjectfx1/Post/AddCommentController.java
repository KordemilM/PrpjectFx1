package com.example.prpjectfx1.Post;

import com.example.prpjectfx1.Holder.PostHolder;
import com.example.prpjectfx1.Holder.UserHolder;
import com.example.prpjectfx1.entity.PostCom;
import com.example.prpjectfx1.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Timestamp;

public class AddCommentController {
    @FXML
    private TextField comment;
    @FXML
    private TextField commentSub;

    PostCom parent;
    User user;

    public void initializePost() {
        PostHolder postComHolder = PostHolder.getINSTANCE();
        parent = postComHolder.getPostCom();
    }

    public void initializeUser() {
        UserHolder userHolder = UserHolder.getINSTANCE();
        user = userHolder.getUser();
    }

    public void addThisComment(ActionEvent event) throws SQLException, ClassNotFoundException {
        PostCom postCom = new PostCom();
        postCom.setContent(comment.getText());
        postCom.setSubject(commentSub.getText());
        postCom.setUserName(user.getUserName());
        postCom.setParent(parent.getId());
        postCom.setAds(false);
        postCom.setLikes(0);
        postCom.setViews(0);
        postCom.setDate(Timestamp.valueOf(java.time.LocalDateTime.now()));
        postCom.setImage("");
        AppContext.getPostComRepos().addChildren(postCom,AppContext.getConnection());
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }
}
