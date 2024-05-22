package com.example.tubes11;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HelloController {
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
        } else if (!txt_username.getText().isBlank() && !txt_password.getText().isBlank()) {
            //label_login.setText("Trying to Login...");
            //uji coba database, tp dimatiin aja soalnya gk work dilaptop lain keknya
            validateLogin();
        } else if (txt_username.getText().isBlank()) {
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

    public void validateLogin() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM UserAccounts WHERE username = '" + txt_username.getText() + "' AND password = '" + txt_password.getText() + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    label_login.setText("Welcome!");
                } else {
                    label_login.setText("Invalid login, please try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}