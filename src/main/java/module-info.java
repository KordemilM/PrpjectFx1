module com.example.prpjectfx1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;
    requires lombok;


    opens com.example.prpjectfx1 to javafx.fxml;
    exports com.example.prpjectfx1;
    exports com.example.prpjectfx1.entity;
    opens com.example.prpjectfx1.entity to javafx.fxml;
    exports com.example.prpjectfx1.repository;
    opens com.example.prpjectfx1.repository to javafx.fxml;
    exports com.example.prpjectfx1.Messanger;
    opens com.example.prpjectfx1.Messanger to javafx.fxml;
    exports com.example.prpjectfx1.Post;
    opens com.example.prpjectfx1.Post to javafx.fxml;
    exports com.example.prpjectfx1.Holder;
    opens com.example.prpjectfx1.Holder to javafx.fxml;
}