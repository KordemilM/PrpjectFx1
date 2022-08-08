package com.example.prpjectfx1.Post;

import com.example.prpjectfx1.Holder.PostsHolder;
import com.example.prpjectfx1.Holder.UserHolder;
import com.example.prpjectfx1.Main;
import com.example.prpjectfx1.PersonalPage;
import com.example.prpjectfx1.entity.PostCom;
import com.example.prpjectfx1.entity.User;
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
import java.util.prefs.Preferences;

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
    public static int pageNumber;

    public void initializePost() {
        PostsHolder postsHolder = PostsHolder.getInstance();
        if(postsHolder.getPosts().size() >= pageNumber) {
            this.postCom = postsHolder.getPosts().get(pageNumber-1);
        }
    }

    public void initializeUser() {
        UserHolder userHolder = UserHolder.getINSTANCE();
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
        FXMLLoader loader1 = new FXMLLoader(PostMainController.class.getResource("/Post/Myposts/ShowLikesViews.fxml"));
        Parent root1 = loader1.load();
        ShowLikesViewsController showLikesViewsController = loader1.getController();
        showLikesViewsController.mainLike(postCom);
        FXMLLoader loader2 = new FXMLLoader(PostMainController.class.getResource("/Post/Myposts/ShowLikesViews.fxml"));
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

    public void backToPersonal() throws SQLException, ClassNotFoundException {
        Preferences userPreferences = Preferences.userNodeForPackage(PersonalPage.class);
        String id = userPreferences.get("id", "");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/prpjectfx1/personalPage.fxml"));
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
}
