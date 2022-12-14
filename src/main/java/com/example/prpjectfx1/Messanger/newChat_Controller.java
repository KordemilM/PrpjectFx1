package com.example.prpjectfx1.Messanger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.example.prpjectfx1.Main;

import static com.example.prpjectfx1.Main.OnlineUser;

public class newChat_Controller implements Initializable {
    @FXML
    private TextField GroupName , addUser , addContact;
    @FXML
    private TextArea description;
    @FXML
    private VBox memberList;
    @FXML
    private Label Check_username , Warnings;
    @FXML
    private ImageView imageView;
    private final List<String> members = new ArrayList<>();

    public void choosePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Group Picture");
        FileChooser.ExtensionFilter extension =
                new FileChooser.ExtensionFilter("Photo",
                        "*.png","*.jpg");
        fileChooser.getExtensionFilters().add(extension);
        File groupPic_file = fileChooser.showOpenDialog(null);
        if(groupPic_file != null){
            Image image = new Image(String.valueOf(groupPic_file.toURI()));
            imageView.setImage(image);
        }
    }

    public void backToChatList() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Chats_View.fxml"));
        Main.mainStage.setScene(new Scene(fxmlLoader.load()));
    }

    public void addMember() throws SQLException {
        String username = addUser.getText();
        if (username.equals(""))
            return;
        PreparedStatement st = Main.connection.prepareStatement("SELECT * FROM `project`.`my_user` WHERE username = ?");
        st.setString(1, username);
        ResultSet rs = st.executeQuery();
        if (members.contains(username)){
            Check_username.setText("already added");
        } else if(OnlineUser.equals(username)) {
            Check_username.setText("XD");
        }
        else if (rs.next()) {
            members.add(username);
            MenuItem m = new MenuItem("remove");
            SplitMenuButton newMember = new SplitMenuButton(m);
            newMember.setText(username);
            memberList.getChildren().add(newMember);
            try{
            m.setOnAction(event -> {
                members.remove(username);
                memberList.getChildren().remove(newMember);
            });
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            Check_username.setText("added");
        }else{
            Check_username.setText("not found");
        }
    }

    public void CreateGroup() throws SQLException, IOException {
        //check Name
        if (GroupName.getText().equals("")) {Warnings.setText("Please Choose A Name");}
        //check members
        else if (members.size() == 0) {Warnings.setText("Please be alone some where else.");}
        //done
        else {
            int  id;
        //Database
            {
                //get chatroom id
                ResultSet r = Main.connection.createStatement().executeQuery
                        ("SELECT MAX(chat_id) AS Last_id FROM `project`.`chat_info`");
                r.next(); id = r.getInt("Last_id") + 1;
                //add Online User to members
                members.add(OnlineUser);
                //add to chat_info Table
                PreparedStatement p = Main.connection.prepareStatement("""
                    INSERT INTO `project`.`chat_info`
                    (`Name`,
                    `description`,
                    `profile`)
                    VALUES (?,?,?)""");
                p.setString(1,GroupName.getText());
                p.setString(2,description.getText());
                p.setString(3,imageView.getImage().getUrl());
                p.executeUpdate();
                //creat member Table for Group
                Main.connection.createStatement().executeUpdate("CREATE TABLE `"+id+"_members` (\n" +
                        "  `id` int NOT NULL AUTO_INCREMENT,\n" +
                        "  `member_username` varchar(45) NOT NULL,\n" +
                        "  `rool` enum('onerowner','admin','member') NOT NULL DEFAULT 'member',\n" +
                        "  PRIMARY KEY (`id`),\n" +
                        "  UNIQUE KEY `member_username_UNIQUE` (`member_username`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
                //creat chat Table for Group
                Main.connection.createStatement().executeUpdate("CREATE TABLE `"+id+"_chat` (\n" +
                        "  `id` int NOT NULL AUTO_INCREMENT,\n" +
                        "  `content` varchar(200) DEFAULT NULL,\n" +
                        "  `picture` varchar(45) DEFAULT NULL,\n" +
                        "  `send_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                        "  `sender_username` varchar(45) NOT NULL,\n" +
                        "  `reply_to` int DEFAULT NULL,\n" +
                        "  `forward_from` int DEFAULT NULL,\n" +
                        "  PRIMARY KEY (`id`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
                //add Group to all members chat_List
                for (String member : members){
                    //creat table for user if it hasn't been created yet
                    {
                        Main.connection.createStatement().executeUpdate(
                                "CREATE TABLE IF NOT EXISTS `project`.`"+member+"_chatslist` ( " +
                                        """
                                          `id` int NOT NULL AUTO_INCREMENT,
                                          `chat_id` int NOT NULL,
                                          `Type` enum('Group','private') NOT NULL DEFAULT 'Group',
                                          `last_Update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                          `has_unseen_message` enum('true','false') DEFAULT 'false',
                                          PRIMARY KEY (`id`),
                                          UNIQUE KEY `chat_id_UNIQUE` (`chat_id`)
                                        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;""");
                    }
                    //add the new chat
                    Main.connection.createStatement().executeUpdate(
                            "INSERT INTO `project`.`"+member+"_chatslist`\n" +
                                    "(`chat_id`)\n" +
                                    "VALUES\n" +
                                    "("+id+");\n");
                    //add members to Group
                    //set role
                    String roll = "member";
                    if (member.equals(OnlineUser))  roll = "onerowner";
                    Main.connection.createStatement().executeUpdate("INSERT INTO `project`.`"+id+"_members`\n" +
                            "(`member_username`,\n" +
                            "`rool`)\n" +
                            "VALUES\n" +
                            "( '"+member+"' ,\n" +
                            " '"+roll+"' );\n");

                }
            }
        //Change scene to Chat_View
            {
                message_View_Controller.id = id;
                FXMLLoader fxmlLoader = new FXMLLoader(message_View_Controller.class.getResource("/chat/Chats_View.fxml"));
                Main.mainStage.setScene(new Scene(fxmlLoader.load()));
            }
        }
    }

    public void Add_new_Contact() throws SQLException, IOException {
        String new_Contact = addContact.getText();
        ResultSet already_added = Main.connection.createStatement().executeQuery
                ("SELECT * FROM `project`.`"+OnlineUser+"_chatslist` lis" +
                        "LEFT JOIN `project`.`chat_info` inf " +
                        " USING(chat_id)" +
                        "WHERE inf.Type = 'private' AND Name = '"+new_Contact+"'");
        ResultSet if_exists = Main.connection.createStatement().executeQuery("SELECT `username` FROM `project`.`my_user` WHERE `username` = '"+new_Contact+"'");
        //check Name
            //Want to chat with himself/herself
        if (new_Contact.equals(OnlineUser)) {Warnings.setText("Talk with yourself out of my App XD");}
            //check if already added
        else if(already_added.next()){Warnings.setText("already added");}
            //check if exists
        else if(!if_exists.next()){Warnings.setText("User not found");}
            //done
        else {
            //add to user_chatsList -> DataBase
            //add to chat_info
            Main.connection.createStatement().executeUpdate("INSERT INTO `project`.`chat_info`\n" +
                    "(`name`,\n" +
                    "`description`,\n" +
                    "`Type` )\n " +
                    "VALUES\n" +
                    "('"+new_Contact+"|"+ OnlineUser + "',\n" +
                    "NULL," +
                    "'private');" );
                //find Last id
            ResultSet r = Main.connection.createStatement().executeQuery
                    ("SELECT MAX(chat_id) AS Last_id FROM `project`.`chat_info`");
            r.next(); int Last_id = r.getInt("Last_id");
                //add

            Main.connection.createStatement().executeUpdate("INSERT INTO `project`.`"+OnlineUser+"_chatslist`\n" +
                    "(`chat_id`,\n" +
                    "`Type`)\n" +
                    "VALUES\n" +
                    "("+Last_id+",\n" +
                    "'private');\n");
            //create table for new_Contact
            Main.connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `"+new_Contact+"_chatslist` (\n" +
                    "  `id` int NOT NULL AUTO_INCREMENT,\n" +
                    "  `chat_id` int NOT NULL,\n" +
                    "  `Type` enum('Group','private') NOT NULL DEFAULT 'private',\n" +
                    "  `last_Update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +//last update
                    "  `has_unseen_message` enum('true','false') DEFAULT 'false',\n" +//has unseen message
                    "  PRIMARY KEY (`id`),\n" +
                    "  UNIQUE KEY `chat_id_UNIQUE` (`chat_id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
            //add to new_Contact_chatsList
            Main.connection.createStatement().executeUpdate("INSERT INTO `project`.`"+new_Contact+"_chatslist`\n" +
                    "(`chat_id`,\n" +
                    "`Type`)\n" +
                    "VALUES\n" +
                    "("+Last_id+",\n" +
                    "'private');\n");
            //create table for chat
            Main.connection.createStatement().executeUpdate("CREATE TABLE `"+Last_id+"_chat` (\n" +
                    "  `id` int NOT NULL AUTO_INCREMENT,\n" +
                    "  `content` varchar(200) DEFAULT NULL,\n" +
                    "  `picture` varchar(45) DEFAULT NULL,\n" +
                    "  `send_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                    "  `sender_username` varchar(45) NOT NULL,\n" +
                    "  `reply_to` int DEFAULT NULL,\n" +
                    "  `forward_from` int DEFAULT NULL,\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
            //Initialize chat screen
            {
                message_View_Controller.id = Last_id;
                FXMLLoader fxmlLoader = new FXMLLoader(Chats_View_Controller.class.getResource("/chat/message_View.fxml"));
                Main.mainStage.setScene(new Scene(fxmlLoader.load()));
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Initialize DataBase
        try {
            Main.connection.createStatement().executeUpdate("""
                    CREATE TABLE IF NOT EXISTS `project`.`chat_info` (
                      `chat_id` int NOT NULL AUTO_INCREMENT,
                      `Name` varchar(45) NOT NULL,
                      `description` varchar(200) DEFAULT NULL,
                      `profile` varchar(200) NOT NULL DEFAULT 'file: /Profile_pic/default.png',
                      `Type` enum('Group','private') NOT NULL DEFAULT 'Group',
                        `last_Update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      PRIMARY KEY (`chat_id`),
                      UNIQUE KEY `chat_id_UNIQUE` (`chat_id`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
                    """);
            Main.connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS " +
                    "`project`.`"+OnlineUser+"_chatslist` (\n" +
                    "  `id` int NOT NULL AUTO_INCREMENT,\n" +
                    "  `chat_id` int NOT NULL,\n" +
                    "  `Type` enum('Group','private') NOT NULL DEFAULT 'Group',\n" +
                    "  `last_Update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +//last update
                    "`has_unseen_message` enum('true','false') DEFAULT 'false',\n" +
                    "  PRIMARY KEY (`id`),\n" +
                    "  UNIQUE KEY `chat_id_UNIQUE` (`chat_id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void BackToChatList() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/chat/Chats_View.fxml"));
        Main.mainStage.setScene(new Scene(fxmlLoader.load()));
    }

}
