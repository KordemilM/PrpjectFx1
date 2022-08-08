package com.example.prpjectfx1.Messanger;

import com.example.prpjectfx1.Main;
import com.example.prpjectfx1.Setting;
import com.example.prpjectfx1.entity.User;
import com.example.prpjectfx1.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.prpjectfx1.Main.OnlineUser;
import static com.example.prpjectfx1.Main.connection;

public class forward_Controller implements Initializable {
    public static User user;
    @FXML
    private CheckBox onlyGroup;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox chatListView;
    @Setter
    public static String content;
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
                ResultSet messages = Main.connection.createStatement().executeQuery
                        ("SELECT * FROM `project`.`"+OnlineUser+"_chatslist` WHERE" +
                                " `has_unseen_message` = 'true' AND" +
                                " `chat_id` = '"+chats.getString("chat_id")+"' ");
                if(messages.next()){
                    Circle circle = new Circle();//init circle
                    {
                        //<Circle fill="DODGERBLUE" radius="4.0" stroke="BLACK" strokeType="INSIDE" />
                        circle.setFill(javafx.scene.paint.Color.DODGERBLUE);
                        circle.setRadius(4.0);
                        circle.setStroke(javafx.scene.paint.Color.BLACK);
                        circle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
                    }
                    label.setGraphic(circle);//add circle to label
                }
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
                    //DB
                    try {
                        connection.createStatement().executeUpdate
                                ("INSERT INTO `project`.`"+btn.getId()+"_chat`" +
                                        " (`sender_username`, `content`) " +
                                        "VALUES ('"+OnlineUser+"', '[Forwarded] "+content+"')");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    //fxml
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

    public void theme(){
        borderPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/styles/" +
                (Setting.isLightMode ? "light" : "dark") + "Mode.css")).toExternalForm());
    }
    @FXML
    public void Filter() throws IOException {
        if (onlyGroup.isSelected()){
            for(Button b : Groups){
                chatListView.getChildren().remove(b);
            }
        }
        else{
            FXMLLoader loader = new FXMLLoader(forward_Controller.class.getResource("/chat/Chats_View.fxml"));
            Stage stage = Main.mainStage;
            Scene scene = new Scene(loader.load());
            stage.setTitle("");
            stage.setScene(scene);
        }
    }

    @FXML
    public void Cancel() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Chats_View_Controller.class.getResource("/chat/message_View.fxml"));
        Main.mainStage.setScene(new Scene(fxmlLoader.load()));
    }
}
