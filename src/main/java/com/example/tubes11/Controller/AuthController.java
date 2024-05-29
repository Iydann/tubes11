package com.example.tubes11.Controller;

import com.example.tubes11.Util.DatabaseConnection;
import com.example.tubes11.Models.User;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {
    @FXML
    private AnchorPane pane_start;
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

    private DatabaseConnection connectNow = new DatabaseConnection();

    public void loginButtonAction(ActionEvent e) {
        String username = txt_username.getText();
        String password = txt_password.getText();

        if (username.isBlank() || password.isBlank()) {
            label_login.setText("Username and password cannot be empty!");
            return;
        }

        User user = validateLoginDB(username, password);

        if (user != null) {
            label_login.setText("Login Successful!");
            loadMainScene(user);
        } else {
            label_login.setText("Invalid Username or Password.");
        }
    }

    public void signUpButtonAction(ActionEvent e) {
        String username = txt_username_up.getText();
        String password = txt_password_up.getText();
        String email = email_up.getText();

        if (username.isBlank() || password.isBlank() || email.isBlank()) {
            label_signup.setText("All fields are required!");
            return;
        }

        if (isUsernameTaken(username)) {
            label_signup.setText("Username already taken. Choose another one.");
        } else if (isEmailTaken(email)) {
            label_signup.setText("Email already taken. Use another email.");
        } else {
            if (saveUser(new User(0, username, password, email))) {
                label_signup.setText("Sign Up Successful!");
            } else {
                label_signup.setText("Sign Up Failed. Try Again.");
            }
        }
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

    private void loadMainScene(User user) {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/tubes11/view/Main.fxml");
            if (fxmlLocation == null) {
                throw new IOException("FXML file not found");
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.setUsername(user.getUsername(), user.getId());

            Stage stage = (Stage) label_login.getScene().getWindow();
            Scene scene = new Scene(root, 1400, 900);
            stage.setTitle("Money Manager - " + user.getUsername());
            stage.setResizable(true);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private User validateLoginDB(String username, String password) {
        Connection connectDB = connectNow.getConnection();
        String verifyLogin = "SELECT id, username, email FROM UserAccounts WHERE username = ? AND password = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(verifyLogin);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next()) {
                return new User(queryResult.getInt("id"), queryResult.getString("username"), password, queryResult.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isEmailTaken(String email) {
        return isFieldTaken("email", email);
    }

    private boolean isUsernameTaken(String username) {
        return isFieldTaken("username", username);
    }

    private boolean isFieldTaken(String field, String value) {
        Connection connectDB = connectNow.getConnection();
        String query = "SELECT count(1) FROM UserAccounts WHERE " + field + " = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, value);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveUser(User user) {
        Connection connectDB = connectNow.getConnection();
        String insertUser = "INSERT INTO UserAccounts (username, password, email) VALUES (?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(insertUser);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());

            int rowAffected = preparedStatement.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
