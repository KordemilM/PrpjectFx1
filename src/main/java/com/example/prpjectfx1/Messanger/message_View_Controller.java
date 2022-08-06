package com.example.prpjectfx1.Messanger;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.prpjectfx1.Main.OnlineUser;
import static com.example.prpjectfx1.Main.connection;

public class message_View_Controller implements Initializable {

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
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        try {
            //set Title (Name)
            ResultSet title = connection.createStatement().executeQuery("SELECT * FROM ``messenger`.`chat_info` WHERE `chat_id` = "+id);
            title.next();
            is_Group = title.getString("Type").equals("Group");
            GroupName.setText(title.getString("Name"));
            //set Title (members)
            if(is_Group){
                ResultSet members = connection.createStatement().executeQuery
                        ("SELECT * FROM ``messenger`.`"+id+"_members` m" +
                                " LEFT JOIN `project`.`user` u " +
                                "ON m.`member_username` = u.`username`");
                StringBuilder members_list = new StringBuilder();
                while (members.next()){
                    members_list.append(members.getString("name")).append(",");
                }
                this.members.setText(members_list.toString());
            }

            //set previous messages
            {
                ResultSet chats = connection.createStatement().executeQuery
                        ("SELECT * FROM `messenger`.`"+id+"_chat` ch" +
                                " LEFT JOIN `project`.`user` un" +
                                " ON ch.sender_username = us.username" +
                                " ORDER BY `last_Update`  ");

                while (chats.next()){
                    boolean is_me = chats.getString("sender_username").equals(OnlineUser);
                    addMassage(chats.getString("Name"),
                            chats.getInt("id"), chats.getString("content")
                            , chats.getString("picture") , chats.getTimestamp("send_time").toString(),
                            chats.getString("photo"),is_me );
                    }
                }
            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void newMassage(){

    }

    public void addMassage(String Name, int messageId , String content , String image , String time , String sender_profile , boolean IsOnlineUser) {
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
                    menuItem.setOnAction(event -> message.setText("[reply to "+Name+" about "+messageId+": "+content+"]"));
                    menuItem2.setOnAction(event -> System.out.println("Enter destination"));
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
                        }

                        Label time_label = new Label();
                        vbox.getChildren().add(time_label);
                        time_label.setText(time);
                        time_label.setTextAlignment(TextAlignment.RIGHT);
    }
    }