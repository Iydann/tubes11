module com.example.tubes11 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.compiler;


    opens com.example.tubes11 to javafx.fxml;
    exports com.example.tubes11;
    exports com.example.tubes11.Controller;
    opens com.example.tubes11.Controller to javafx.fxml;
}