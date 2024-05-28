module com.example.tubes11 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.compiler;
    requires java.desktop;


    opens com.example.tubes11.view to javafx.fxml;
    exports com.example.tubes11.Controller;
    opens com.example.tubes11.Controller to javafx.fxml;
    exports com.example.tubes11.Util;
    opens com.example.tubes11.Util to javafx.fxml;
}