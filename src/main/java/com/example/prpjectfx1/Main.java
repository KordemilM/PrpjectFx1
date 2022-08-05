package com.example.prpjectfx1;

import com.example.prpjectfx1.repository.ConnectToTheDatabase;
import com.example.prpjectfx1.repository.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application {

    public static Stage mainStage;
    public static Connection connection;
    public static String OnlineUser;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DatabaseInitializer databaseInitializer = new DatabaseInitializer();
        connection = ConnectToTheDatabase.ConnectDatabase();
        databaseInitializer.createTables(connection);
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 500);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/image/icon2.png")));
        //stage.setMinWidth(450.0);
        //stage.setMinHeight(550.0);
        stage.show();
    }
}