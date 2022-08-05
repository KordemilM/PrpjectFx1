package com.example.prpjectfx1.Messanger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class newChat_Controller {
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
    private String picturePath;
    private List<String> members = new ArrayList<>();
    public void choosePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Group Picture");
        FileChooser.ExtensionFilter extension =
                new FileChooser.ExtensionFilter("Photo",
                        "*.png","*.jpg");
        fileChooser.getExtensionFilters().add(extension);
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            try {
                picturePath = "/Profile_pic/"+file.getName();
                Files.copy(file.toPath(),new File(picturePath).toPath());
            }catch (Exception e) {
                System.out.println(e.getMessage());
                picturePath = file.toPath().toString();
            }
            Image image = new Image("file: "+picturePath);
            imageView.setImage(image);
        }
        else{
            picturePath = imageView.getImage().toString();
        }
    }

    public void backToChatList() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("Chats_View.fxml"));
        MainApp.scene = new Scene(fxmlLoader.load());
    }

    public void addMember() throws SQLException {
        String username = addUser.getText();
        if (username.equals(""))
            return;
        PreparedStatement st = MainApp.getConnection.prepareStatement("SELECT * FROM `project`.`user` WHERE username = ?");
        st.setString(1, username);
        ResultSet rs = st.executeQuery();
        if (members.contains(username)){
            Check_username.setText("already added");
        } else if(MainApp.OnlineUser.equals(username)) {
            Check_username.setText("XD");
        }
        else if (rs.next()) {
            members.add(username);
            MenuItem m = new MenuItem("remove");
            SplitMenuButton newMember = new SplitMenuButton(m);
            newMember.setText(username);
            memberList.getChildren().add(newMember);
            m.setOnAction(event -> {
                members.remove(username);
                memberList.getChildren().remove(newMember);
            });
            Check_username.setText("added");
        }else{
            Check_username.setText("not found");
        }
    }

    public void CreateGroup() throws SQLException {
        //check Name
        if (GroupName.getText().equals("")) {Warnings.setText("Please Choose A Name");}
        //check members
        else if (members.size() == 0) {Warnings.setText("Please");}
        //done
        else {
            PreparedStatement p = MainApp.getConnection.prepareStatement("INSERT INTO ");
        }
    }
}
