package com.example.prpjectfx1.Post;

import com.example.prpjectfx1.Holder.PostHolder;
import com.example.prpjectfx1.entity.PostCom;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.SQLException;

public class LikeExploreController {
    @FXML
    private ListView<String> userNameLikeList;

    private PostCom postCom;

    public void initializePost() {
        PostHolder postHolder = PostHolder.getINSTANCE();
        this.postCom = postHolder.getPostCom();
    }

    public void mainLikesUserName() throws SQLException, ClassNotFoundException {
        userNameLikeList.getItems().addAll(AppContext.getPostComRepos().getUsernameOfLikes(postCom,AppContext.getConnection()));
    }
}