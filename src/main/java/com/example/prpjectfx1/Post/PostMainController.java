package com.example.prpjectfx1.Post;

import com.example.prpjectfx1.Holder.IntegerHolder;
import com.example.prpjectfx1.Holder.PostHolder;
import com.example.prpjectfx1.Holder.PostsHolder;
import com.example.prpjectfx1.Holder.UserHolder;
import com.example.prpjectfx1.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class PostMainController {
    private static User user;

    public void initializeUser() {
        UserHolder userHolder = UserHolder.getINSTANCE();
        user = userHolder.getUser();
    }

    public static void AddPost(ActionEvent event) throws IOException {
            FXMLLoader loader =  new FXMLLoader(Objects.requireNonNull(PostMainController.class.getResource("AddPost/AddPost.fxml")));
            Parent root = loader.load();
            AddPostController controller = loader.getController();
            UserHolder holder = UserHolder.getINSTANCE();
            holder.setUser(user);
            controller.initializeUser();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
    }

    public static void MyPosts(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader =  new FXMLLoader(Objects.requireNonNull(PostMainController.class.getResource("Myposts/ShowMyPosts.fxml")));
        Parent root = loader.load();
        ShowMyPostsController controller = loader.getController();
        UserHolder holder = UserHolder.getINSTANCE();
        holder.setUser(user);
        controller.initializeUser();
        ShowMyPostsController.pageNumber = 1;
        PostsHolder postsHolder = PostsHolder.getInstance();
        postsHolder.setPosts(AppContext.getPostComRepos().getAllPostsByUser(user.getUserName(), AppContext.getConnection()));
        controller.initializePost();
        controller.main();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public static void Recent(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader =  new FXMLLoader(Objects.requireNonNull(PostMainController.class.getResource("Recent/Recent.fxml")));
        Parent root = loader.load();
        RecentController controller = loader.getController();
        RecentController.pageNumber = 1;
        PostsHolder postsHolder = PostsHolder.getInstance();
        postsHolder.setPosts(AppContext.getPostComRepos().getLast10Post(user.getUserName(), AppContext.getConnection()));
        controller.initializePost();
        UserHolder holder = UserHolder.getINSTANCE();
        holder.setUser(user);
        controller.initializeUser();
        controller.main();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public static void Explore(ActionEvent event) throws IOException, SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(PostMainController.class.getResource("Explore/ShowAd.fxml")));
            Parent root = loader.load();
            ShowAdController controller = loader.getController();
            UserHolder holder = UserHolder.getINSTANCE();
            holder.setUser(user);
            controller.initializeUser();
            PostHolder postHolder = PostHolder.getINSTANCE();
            postHolder.setPostCom(AppContext.getPostComRepos().getRandomAdsPost(user.getUserName(), AppContext.getConnection()));
            controller.initializePost();
            controller.main();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ad");
            stage.show();
        }
        catch (Exception ignored) {}
        try {
            FXMLLoader loader2 = new FXMLLoader(Objects.requireNonNull(PostMainController.class.getResource("Explore/Explore.fxml")));
            Parent root2 = loader2.load();
            IntegerHolder holder2 = IntegerHolder.getINSTANCE();
            holder2.setNum(1);
            ExploreController controller2 = loader2.getController();
            controller2.initializePageNumber();
            controller2.initializeUser();
            PostsHolder postsHolder = PostsHolder.getInstance();
            postsHolder.setPosts(AppContext.getPostComRepos().getAllPosts(user.getUserName(), AppContext.getConnection()));
            controller2.initializePost();
            controller2.main();
            Scene scene2 = new Scene(root2);
            Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage2.setScene(scene2);
            stage2.show();
        }
        catch (Exception ignored) {}
    }
}
