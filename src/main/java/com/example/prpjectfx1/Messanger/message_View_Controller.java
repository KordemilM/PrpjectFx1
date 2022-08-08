package com.example.prpjectfx1.Messanger;


import com.example.prpjectfx1.Main;
import com.example.prpjectfx1.Setting;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.prpjectfx1.Main.OnlineUser;
import static com.example.prpjectfx1.Main.connection;

public class message_View_Controller implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView GroupImage;
    @FXML
    private Label members , GroupName;
    @FXML
    private TextArea message;
    @FXML
    private VBox chatList;

    public static int id;
    public static boolean is_Group;
    public List<String> membersList = new ArrayList<>();
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        try {
            //DB set has_unseen_message = false
            connection.createStatement().executeUpdate("UPDATE `project`.`"+OnlineUser+"_chatslist` " +
                    "SET has_unseen_message = 'false' " +
                    "WHERE chat_id = "+ id );

            theme();
            //set Title (Name)
            ResultSet title = connection.createStatement().executeQuery
                    ("SELECT * FROM `project`.`chat_info` WHERE chat_id = "+id);
            title.next();
            is_Group = title.getString("Type").equals("Group");
            GroupName.setText(title.getString("Name"));
            GroupImage.setImage(new Image("file:/Profile_pic"+title.getString("profile")));
            //set Title (members)
            if(is_Group){
                ResultSet members = connection.createStatement().executeQuery
                        ("SELECT * FROM `project`.`"+id+"_members` m" +
                                " LEFT JOIN `project`.`my_user` u " +
                                "ON m.member_username = u.username");
                StringBuilder members_list = new StringBuilder();
                while (members.next()){
                    String us = members.getString("username");
                    members_list.append(us).append(",");
                    membersList.add(us);
                }
                this.members.setText(members_list.toString());
            }

            //set previous messages
            {
                ResultSet chats = connection.createStatement().executeQuery
                        ("SELECT * FROM `project`.`"+id+"_chat` ch" +
                                " LEFT JOIN `project`.`my_user` un" +
                                " ON ch.sender_username = un.username" +
                                " ORDER BY send_time ");

                while (chats.next()){
                    boolean is_me = chats.getString("sender_username").equals(OnlineUser);
                    addMassage(chats.getString("Name"),
                             chats.getString("content")
                            , chats.getString("picture") , chats.getTimestamp("send_time").toString(),
                            chats.getString("photo"),is_me );
                    }
                }
            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void newMassage() throws SQLException {
        if (!message.getText().isEmpty()){
            //DB
            membersList.remove(OnlineUser);
            System.out.println(id);
            for (String username : membersList ){
                connection.createStatement().executeUpdate
                        ("UPDATE `project`.`"+username+"_chatslist` " +
                                "SET `has_unseen_message` = 'true' " +
                                "WHERE ( `chat_id` = '" + id + "')" );
            }
            //UPDATE `project`.`ali_chatslist` SET `has_unseen_message` = 'true' WHERE (`id` = '1');
            String content = message.getText().trim();
            connection.createStatement().executeUpdate
                    ("INSERT INTO `project`.`"+id+"_chat`" +
                            " (`sender_username`, `content`) " +
                            "VALUES ('"+OnlineUser+"', '"+content+"')");
            //GUI
            addMassage(OnlineUser,content,"", LocalDateTime.now().toString(),Chats_View_Controller.user.getPhoto(),true);
        }
    }

    public void addMassage(String Name , String content , String image , String time , String sender_profile , boolean IsOnlineUser) {
        HBox hbox = new HBox();
        chatList.getChildren().add(hbox);
        hbox.setLayoutX(20);
        hbox.setLayoutY(150);
        hbox.setMaxWidth(Double.MAX_VALUE);
        if (IsOnlineUser)
            hbox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        else hbox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        VBox.setVgrow(hbox, Priority.ALWAYS);

            ImageView imageView = new ImageView();
            hbox.getChildren().add(imageView);
            imageView.setFitHeight(56);
            imageView.setFitWidth(62);
            imageView.setPickOnBounds(true);
            imageView.setPreserveRatio(true);
            HBox.setHgrow(imageView, Priority.ALWAYS);
            imageView.setImage(new Image("file:/Profile_pic/"+image));
            HBox.setMargin(imageView, new Insets(10));

            VBox vbox = new VBox();
            hbox.getChildren().add(vbox);
            vbox.setAlignment(Pos.CENTER_LEFT);
            if(IsOnlineUser)
                vbox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            else vbox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            VBox.setVgrow(vbox, Priority.ALWAYS);

                Label sender_name = new Label();
                vbox.getChildren().add(sender_name);
                sender_name.setText(Name);

                SplitMenuButton splitMenuButton = new SplitMenuButton();
                vbox.getChildren().add(splitMenuButton);
                splitMenuButton.setMaxWidth(200);
                splitMenuButton.setMnemonicParsing(false);
                VBox.setVgrow(splitMenuButton, Priority.ALWAYS);

                    MenuItem menuItem = new MenuItem("reply");
                    MenuItem menuItem2 = new MenuItem("forward");
                    menuItem.setOnAction(event -> message.setText("[reply to "+content+" ]"));
                    menuItem2.setOnAction(event -> {
                        forward_Controller.content = content;
                        FXMLLoader fxmlLoader = new FXMLLoader(forward_Controller.class.getResource("/chat/forward.fxml"));
                        try {
                            Main.mainStage.setScene(new Scene(fxmlLoader.load()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    splitMenuButton.getItems().addAll(menuItem,menuItem2);


                        if(content == null){
                                ImageView imageView2 = new ImageView();
                                vbox.getChildren().add(imageView2);
                                imageView2.setFitHeight(150);
                                imageView2.setFitWidth(200);
                                imageView2.setPickOnBounds(true);
                                imageView2.setPreserveRatio(true);
                                imageView2.setImage(new Image("file:/Profile_pic/"+sender_profile));
                        }else {
                            TextFlow textFlow = new TextFlow();
                            splitMenuButton.setGraphic(textFlow);
                            textFlow.setMaxWidth(200);
                            textFlow.setTextAlignment(TextAlignment.RIGHT);
                            Text text = new Text(content);
                            textFlow.getChildren().add(text);
                            text.setStrokeType(StrokeType.OUTSIDE);
                            text.setStrokeWidth(0);
                            text.setWrappingWidth(146.53125);
                            textFlow.setPrefHeight(text.getLayoutBounds().getHeight());
                            splitMenuButton.setPrefHeight(text.getLayoutBounds().getHeight());
                            splitMenuButton.setWrapText(true);
                        }

                        Label time_label = new Label();
                        vbox.getChildren().add(time_label);
                        time_label.setText(time);
                        time_label.setTextAlignment(TextAlignment.RIGHT);
    }

    public void back() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Chats_View_Controller.class.getResource("/chat/Chats_View.fxml"));
        Main.mainStage.setScene(new Scene(fxmlLoader.load()));
    }

    public void theme(){
        borderPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/styles/" +
                (Setting.isLightMode ? "light" : "dark") + "Mode.css")).toExternalForm());
    }
}