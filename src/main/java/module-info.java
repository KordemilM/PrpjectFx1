module com.example.prpjectfx1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;


    opens com.example.prpjectfx1 to javafx.fxml;
    exports com.example.prpjectfx1;
}