package com.example.tubes11.Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MoneyManagerApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Use a relative path instead of an absolute path
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tubes11/view/Auth.fxml"));
        Scene scene = new Scene(loader.load(), 800, 512);
        primaryStage.setTitle("Money Manager");
        primaryStage.setResizable(false); //False utk mengunci size windows
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
