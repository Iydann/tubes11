package com.example.tubes11.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LoginController {
    @FXML
    private AnchorPane pane_start;
    @FXML
    private AnchorPane pane_login;
    @FXML
    private Button btn_login;
    @FXML
    private Label label_login;
    public void loginButtonAction(ActionEvent e) {
        //code for show label notification login
        if (txt_username.getText().isBlank() && txt_password.getText().isBlank()) {
            label_login.setText("Username and password cannot be empty!");
        }
        else if (!txt_username.getText().isBlank() && !txt_password.getText().isBlank()) {
            label_login.setText("Trying to Login...");
            String username = txt_username.getText();
            String password = txt_password.getText();
            if (validateLogin(username, password)) {
                // Jika login berhasil
                label_login.setText("Login Successful!");
                loadMainScene();
            } else {
                label_login.setText("Invalid Username or Password.");
            }
        }
        else if (txt_username.getText().isBlank()) {
            label_login.setText("Please fill in the Username!");
        } else if (txt_password.getText().isBlank()) {
            label_login.setText("Please fill in the Password!");
        }
    }
    @FXML
    private TextField txt_username;
    @FXML
    private PasswordField txt_password;

    @FXML
    private AnchorPane pane_signup;
    @FXML
    private Button btn_signup;
    @FXML
    private Label label_signup;

    public void signUpButtonAction(ActionEvent e) {
        String username = txt_username_up.getText();
        String password = txt_password_up.getText();
        String email = email_up.getText();

        if (username.isBlank() && password.isBlank() && email.isBlank()) {
            label_signup.setText("Please fill in the data above first!");
        } else if (username.isBlank() && password.isBlank()) {
            label_signup.setText("Username and password cannot be empty!");
        } else if (username.isBlank()) {
            label_signup.setText("Please fill in the Username!");
        } else if (password.isBlank()) {
            label_signup.setText("Please fill in the Password!");
        } else if (email.isBlank()) {
            label_signup.setText("Please fill in the Email!");
        } else {
            label_signup.setText("Trying to Sign Up...");
        }
    }
    @FXML
    private TextField txt_username_up;
    @FXML
    private TextField txt_password_up;
    @FXML
    private TextField email_up;

    @FXML
    public void handleLoginButtonAction() {
        // Code for login button functionality
        System.out.println("Login button clicked");
    }

    @FXML
    public void handleSignupButtonAction() {
        // Code for signup button functionality
        System.out.println("Signup button clicked");
    }

    @FXML
    public void handleSwitchToSignup() {
        pane_start.setVisible(false);
        pane_login.setVisible(false);
        pane_signup.setVisible(true);
    }

    @FXML
    public void handleSwitchToLogin() {
        pane_start.setVisible(false);
        pane_signup.setVisible(false);
        pane_login.setVisible(true);
    }


    private void loadMainScene() {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/tubes11/view/Main.fxml");
            if (fxmlLocation == null) {
                throw new IOException("FXML file not found");
            }
            System.out.println("Loading FXML from: " + fxmlLocation);
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            Stage stage = (Stage) label_login.getScene().getWindow();
            Scene scene = new Scene(root, 1100, 800);
            stage.setTitle("Money Manager - " + txt_username.getText());
            stage.setResizable(true); // Mengunci Size Windows
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public boolean validateLogin(String username, String password) {
        // validateLogin ini hanya untuk sementara, karena database belum siap

        // Username dan password yang diharapkan
        String expectedUsername = "admin";
        String expectedPassword = "admin";

        // Memeriksa apakah nilai username dan password sesuai dengan yang diharapkan
        return username.equals(expectedUsername) && password.equals(expectedPassword);
    }

//    public void validateLoginDB(TextField txt_username, PasswordField txt_password) {
//        DatabaseConnection connectNow = new DatabaseConnection();
//        Connection connectDB = connectNow.getConnection();
//
//        String verifyLogin = "SELECT count(1) FROM UserAccounts WHERE username = '" + this.txt_username.getText() + "' AND password = '" + this.txt_password.getText() + "'";
//
//        try {
//            Statement statement = connectDB.createStatement();
//            ResultSet queryResult = statement.executeQuery(verifyLogin);
//
//            while (queryResult.next()) {
//                if (queryResult.getInt(1) == 1) {
//                    label_login.setText("Welcome!");
//                    loadMainScene();
//                } else {
//                    label_login.setText("Invalid login, please try again.");
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
