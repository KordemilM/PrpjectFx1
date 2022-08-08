package com.example.prpjectfx1.Messanger;

import com.example.prpjectfx1.Follow;
import com.example.prpjectfx1.Holder.PostsHolder;
import com.example.prpjectfx1.Holder.UserHolder;
import com.example.prpjectfx1.Main;
import com.example.prpjectfx1.PersonalPage;
import com.example.prpjectfx1.Post.AddPostController;
import com.example.prpjectfx1.Post.AppContext;
import com.example.prpjectfx1.Post.PostMainController;
import com.example.prpjectfx1.Post.RecentController;
import com.example.prpjectfx1.Setting;
import com.example.prpjectfx1.entity.User;
import com.example.prpjectfx1.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static com.example.prpjectfx1.Main.OnlineUser;

public class Chats_View_Controller implements Initializable {
    public static User user;
    @FXML
    private CheckBox onlyGroup;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox chatListView;
    public ArrayList<Button> Groups = new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            theme();
            user = UserRepository.searchUser(OnlineUser);
            //get Chats from DB
            ResultSet chats = Main.connection.createStatement().executeQuery
                    ("SELECT * FROM `project`.`"+OnlineUser+"_chatslist` lis" +
                            "LEFT JOIN `project`.`chat_info` inf " +
                            " USING (chat_id) " +
                            "ORDER BY inf.last_Update DESC" );
            //make the list of chats
            while (chats.next()){
                Button btn = new Button();//init button
                {
                    if (chats.getString("Type").equals("Group")) Groups.add(btn);
                    chatListView.getChildren().add(btn);//add button to list
                    btn.setId(chats.getString("chat_id"));
                    btn.setMaxWidth(1.7976931348623157E308);
                    btn.setMnemonicParsing(false);
                    VBox.setVgrow(btn, javafx.scene.layout.Priority.ALWAYS);
                }
                HBox hBox = new HBox();
                btn.setGraphic(hBox);
                Image image ;
                try {
                    image = new Image(chats.getString("profile"));
                }catch (IllegalArgumentException e){
                    image = new Image("file:/com/example/prpjectfx1/images/default.png");
                    System.out.println(chats.getString("profile"));
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(image);//init image view
                {
                    imageView.setFitHeight(66.0);
                    imageView.setFitWidth(74.0);
                    imageView.setPickOnBounds(true);
                    imageView.setPreserveRatio(true);
                    hBox.getChildren().add(imageView);//add image view to hBox
                    HBox.setHgrow(imageView, javafx.scene.layout.Priority.ALWAYS);
                    //Insets left="20.0"
                    HBox.setMargin(imageView, new javafx.geometry.Insets(0.0, 0.0, 0.0, 20.0));

                }
                VBox vBox = new VBox();{
                    hBox.getChildren().add(vBox);//add vBox to hBox
                    vBox.setSpacing(10.0);
                    HBox.setHgrow(vBox, javafx.scene.layout.Priority.ALWAYS);
                }
                Label label = new Label();//init label
                {
                    vBox.getChildren().add(label);//add label to vBox
                    label.setText(chats.getString("Name"));
                    label.setMnemonicParsing(false);
                    VBox.setVgrow(label, javafx.scene.layout.Priority.ALWAYS);
                    //<Font name="System Bold" size="19.0" />
                    label.setFont(new javafx.scene.text.Font("System Bold", 19.0));
                }
                //check if there is new message
                System.out.println(chats.getString("has_unseen_message"));
                    if(chats.getString("has_unseen_message").equals("true"))
                        label.setStyle("-fx-font-weight: Italic");//bold


                Label lastMessage = new Label();//init label
                {
                    vBox.getChildren().add(lastMessage);//add label to vBox
                    //find last message
                    String lastMessageText, lastMessage_sender, lastMessage_time;
                    int chat_id = chats.getInt("chat_id");
                    ResultSet lastMessageResult = Main.connection.createStatement().executeQuery
                            ("SELECT * " +
                                    "FROM `project`.`"+chat_id+"_chat` c " +
                                    "LEFT JOIN `project`.`my_user` u ON c.sender_username = u.username " +
                                    "ORDER BY c.send_time DESC LIMIT 1");
                    if(lastMessageResult.next()){
                        lastMessageText = lastMessageResult.getString("content");
                        lastMessage_sender = lastMessageResult.getString("sender_username") ;
                        if (lastMessage_sender.equals(OnlineUser)){
                            lastMessage_sender = "You";
                        }
                        lastMessage_time = lastMessageResult.getTimestamp("send_time").toString();
                        if (lastMessageResult.getString("picture") != null) lastMessageText = "Picture";
                        lastMessage.setText(lastMessage_sender + ": " + lastMessageText + " " + lastMessage_time);
                    }

                    lastMessage.setMnemonicParsing(false);
                    VBox.setVgrow(lastMessage, javafx.scene.layout.Priority.ALWAYS);
                    lastMessage.setPrefHeight(18.0);
                    lastMessage.setFont(new javafx.scene.text.Font(17.0));
                }
                HBox.setMargin(vBox, new javafx.geometry.Insets(10.0, 0.0, 10.0, 10.0));

                //add event to button
                btn.setOnAction(event -> {
                    message_View_Controller.id = Integer.parseInt(btn.getId());
                    FXMLLoader fxmlLoader = new FXMLLoader
                            (message_View_Controller.class.getResource("/chat/message_View.fxml"));
                    try {
                        Main.mainStage.setScene(new Scene(fxmlLoader.load()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addChat() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/chat/newChat.fxml"));
        try {
            Main.mainStage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            System.out.println("problem with newChat.fxml");
            throw new RuntimeException(e);
        }
    }

    public void theme(){
        borderPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/styles/" +
                (Setting.isLightMode ? "light" : "dark") + "Mode.css")).toExternalForm());
    }

    @FXML
    public void PersonalPageClick() throws IOException {
        Preferences userPreferences = Preferences.userNodeForPackage(PersonalPage.class);
        String id = userPreferences.get("id", "");

        FXMLLoader loader = new FXMLLoader(PersonalPage.class.getResource("personalPage.fxml"));
        PersonalPage.setId(id);
        PersonalPage.setIsFromChat(true);
        Stage stage = Main.mainStage;
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void Filter() throws IOException {
        if (onlyGroup.isSelected()){
            for(Button b : Groups){
                chatListView.getChildren().remove(b);
            }
        }
        else{
            FXMLLoader loader = new FXMLLoader(Chats_View_Controller.class.getResource("/chat/Chats_View.fxml"));
            Stage stage = Main.mainStage;
            Scene scene = new Scene(loader.load());
            stage.setTitle("");
            stage.setScene(scene);
        }
    }

    public void toAddPost() throws IOException, SQLException {   ///OK
        Preferences userPreferences = Preferences.userNodeForPackage(PersonalPage.class);
        String id = userPreferences.get("id", "");
        User user = UserRepository.searchUser(id);

        FXMLLoader loader =  new FXMLLoader(Objects.requireNonNull(PostMainController.class.getResource("/Post/AddPost/AddPost.fxml")));
        Parent root = loader.load();
        AddPostController controller = loader.getController();
        UserHolder holder = UserHolder.getINSTANCE();
        holder.setUser(user);
        controller.initializeUser();
        controller.theme();
        Scene scene = new Scene(root);
        Stage stage = Main.mainStage;
        stage.setScene(scene);
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

    @FXML
    protected void homeButtonClick() throws IOException, SQLException, ClassNotFoundException {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(PostMainController.class.getResource("/Post/Recent/Recent.fxml")));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            RecentController controller = loader.getController();
            RecentController.pageNumber = 1;
            PostsHolder postsHolder = PostsHolder.getInstance();
            postsHolder.setPosts(AppContext.getPostComRepos().getLast10Post(UserHolder.getINSTANCE().getUser().getUserName(), AppContext.getConnection()));
            controller.initializePost();
            controller.initializeUser();
            controller.main();
            Stage stage = Main.mainStage;
            Scene scene = new Scene(root);
            stage.setTitle("");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception ignored){}
    }
}
