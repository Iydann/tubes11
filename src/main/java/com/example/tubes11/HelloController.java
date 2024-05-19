package com.example.tubes11;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class HelloController {
    @FXML
    private AnchorPane pane_login;
    @FXML
    private Button btn_login;
    @FXML
    private Label label_login;
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
        pane_login.setVisible(false);
        pane_signup.setVisible(true);
    }

    @FXML
    public void handleSwitchToLogin() {
        pane_signup.setVisible(false);
        pane_login.setVisible(true);
    }
}