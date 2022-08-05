package com.example.prpjectfx1.Messanger;

import com.example.prpjectfx1.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.example.prpjectfx1.Main.OnlineUser;

public class Chats_View_Controller implements Initializable {
    @FXML
    private VBox chatListView;

    public static int id;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //get Chats from DB
        try {
            ResultSet chats = Main.connection.createStatement().executeQuery("SELECT * FROM `messenger`.`"+OnlineUser+"_chatslist` ORDER BY `last_Update` DESC " );
            while (chats.next()){
                Button btn = new Button();//init button
                {
                    chatListView.getChildren().add(btn);//add button to list
                    btn.setId(chats.getString("chat_id"));
                    btn.setMaxWidth(1.7976931348623157E308);
                    btn.setMnemonicParsing(false);
                    VBox.setVgrow(btn, javafx.scene.layout.Priority.ALWAYS);
                }
                HBox hBox = new HBox();
                btn.setGraphic(hBox);
                Image image = new Image("file : Profile_pic/default.png");
                try {
                    image = new Image(chats.getString("profile"));
                }catch (Exception e){
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
                        ("SELECT * FROM `messenger`.`"+OnlineUser+"_chatslist` WHERE" +
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
                    String lastMessageText = "";
                    ResultSet lastMessageResult = Main.connection.createStatement().executeQuery
                            ("SELECT * ّ" +
                                    "FROM `messenger`.`"+chats.getString("chat_id")+"_chat` " +
                                    "ORDER BY `send_time` DESC LIMIT 1");
                    lastMessage.setMnemonicParsing(false);
                    VBox.setVgrow(lastMessage, javafx.scene.layout.Priority.ALWAYS);
                    //<Font name="System Bold" size="19.0" />
                    lastMessage.setFont(new javafx.scene.text.Font("System Bold", 19.0));
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
