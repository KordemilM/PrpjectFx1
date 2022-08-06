package com.example.prpjectfx1.Post;

import com.example.prpjectfx1.entity.PostCom;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class ShowLikesViewsController {
    @FXML
    private ListView<String> myList;

    public void mainLike(PostCom postCom) throws SQLException, ClassNotFoundException {
        LinkedHashMap<Date, Integer> likes = AppContext.getPostComRepos().getLikes(postCom, AppContext.getConnection());
        for(Date date : likes.keySet()){
            myList.getItems().add(date + "\t\t\t" + likes.get(date));
        }
    }

    public void mainView(PostCom postCom) throws SQLException, ClassNotFoundException {
        LinkedHashMap<Date, Integer> views = AppContext.getPostComRepos().getViews(postCom, AppContext.getConnection());
        for(Date date : views.keySet()){
            myList.getItems().add(date + "\t\t\t" + views.get(date));
        }
    }
}
