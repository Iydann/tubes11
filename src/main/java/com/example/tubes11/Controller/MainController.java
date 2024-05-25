package com.example.tubes11.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class MainController {

    @FXML
    private AnchorPane DashboardPane;

    @FXML
    private AnchorPane GraphPane;

    @FXML
    private AnchorPane AlarmPane;

    @FXML
    private AnchorPane MorePane;

    @FXML
    private Label Savinggoal;

    @FXML
    private Label Savinggoal1;

    @FXML
    private Label balanceAmount;

    @FXML
    private Label SpendingAmount;

    @FXML
    private Button addButton;

    @FXML
    private Button addsavingbutton;

    @FXML
    private Button buttonDashboard;

    @FXML
    private Button buttonGraph;

    @FXML
    private Button buttonAlarm;

    @FXML
    private Button buttonMore;

    private final String defaultStyle = "-fx-text-fill: #000000; -fx-background-color: #FFFFFF; -fx-effect: dropshadow(three-pass-box, #AAAAAA, 3, 0, 0, 3); -fx-pref-width: 150; -fx-font-size: 1.5em; -fx-font-family: 'Calibri Light'; -fx-pref-height: 40";
    private final String activeStyle = "-fx-text-fill: #FFFFFF; -fx-background-color: #212121;-fx-effect: dropshadow(three-pass-box,  #212121, 3, 0, 0, 3); -fx-pref-width: 150; -fx-font-size: 1.5em; -fx-font-family: 'Calibri Light'; -fx-pref-height: 40";

    private void resetButtonStyles() {
        buttonDashboard.setStyle(defaultStyle);
        buttonGraph.setStyle(defaultStyle);
        buttonAlarm.setStyle(defaultStyle);
        buttonMore.setStyle(defaultStyle);
    }

    @FXML
    public void handleSwitchToDashboard() {
        DashboardPane.setVisible(true);
        GraphPane.setVisible(false);
        AlarmPane.setVisible(false);
        MorePane.setVisible(false);
        resetButtonStyles();
        buttonDashboard.setStyle(activeStyle);
    }

    @FXML
    public void handleSwitchToGraph() {
        DashboardPane.setVisible(false);
        GraphPane.setVisible(true);
        AlarmPane.setVisible(false);
        MorePane.setVisible(false);
        resetButtonStyles();
        buttonGraph.setStyle(activeStyle);
    }

    @FXML
    public void handleSwitchToAlarm() {
        DashboardPane.setVisible(false);
        GraphPane.setVisible(false);
        AlarmPane.setVisible(true);
        MorePane.setVisible(false);
        resetButtonStyles();
        buttonAlarm.setStyle(activeStyle);
    }

    @FXML
    public void handleSwitchToMore() {
        DashboardPane.setVisible(false);
        GraphPane.setVisible(false);
        AlarmPane.setVisible(false);
        MorePane.setVisible(true);
        resetButtonStyles();
        buttonMore.setStyle(activeStyle);
    }

    @FXML
    private TextField amountField;

    @FXML
    private Label dateLabel;

    @FXML
    private Label expensesAmount;

    @FXML
    private TextArea noteField;

    @FXML
    private TextField savingfield;

    @FXML
    private Button showGraphButton;

    @FXML
    private Button clearAllButton; // New button for clearing selected transactions

    private double balance = 0.00;
    private double spending = 0.00;
    private double savingGoal = 0.00;

    private double previousBalance = 0.00;
    private double previousSpending = 0.00;
    private double previousSavingGoal = 0.00;

    private ObservableList<String> transactionList = FXCollections.observableArrayList();

    @FXML
    private Label spendingAmount;

    @FXML
    private ListView<String> transactionListView;

    @FXML
    private ChoiceBox<String> transactionTypeChoiceBox;

    public void initialize() {
        // Adding options to ChoiceBox
        transactionTypeChoiceBox.setItems(FXCollections.observableArrayList(
                "Income", "Expense"
        ));

        updateLabels();

        transactionListView.setItems(transactionList);

        addButton.setOnAction(event -> addTransaction());
        addsavingbutton.setOnAction(event -> addSavingGoal());
        clearAllButton.setOnAction(event -> clearSelectedTransactions()); // Event handler for clearAllButton
    }

    private void updateLabels() {
        balanceAmount.setText(String.format("Rp. %.2f", balance));
        spendingAmount.setText(String.format("Rp. %.2f", spending));
        Savinggoal.setText(String.format("Rp. %.2f", savingGoal));
        Savinggoal1.setText(String.format("Rp. %.2f", balance - savingGoal));
    }

    private void addTransaction() {
        String type = transactionTypeChoiceBox.getValue();
        String amountText = amountField.getText();
        String note = noteField.getText();

        if (amountText.isEmpty() || !amountText.matches("\\d+(\\.\\d{1,2})?")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid amount.");
            alert.showAndWait();
            return;
        }

        // Save current state before modifying it
        saveCurrentState();

        double amount = Double.parseDouble(amountText);
        String transaction = String.format("%s: Rp. %.2f - %s", type, amount, note);

        transactionList.add(transaction);

        switch (type) {
            case "Income":
                balance += amount;
                break;
            case "Expense":
                balance -= amount;
                spending += amount;
                break;
            default:
                // Handle any unexpected types
                break;
        }

        updateLabels();

        amountField.clear();
        noteField.clear();
    }

    private void addSavingGoal() {
        String amountText = savingfield.getText();

        if (amountText.isEmpty() || !amountText.matches("\\d+(\\.\\d{1,2})?")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid amount.");
            alert.showAndWait();
            return;
        }

        // Save current state before modifying it
        saveCurrentState();

        savingGoal = Double.parseDouble(amountText);
        updateLabels();
        savingfield.clear();
    }

    private void clearSelectedTransactions() {
        ObservableList<String> selectedItems = transactionListView.getSelectionModel().getSelectedItems();
        transactionList.removeAll(selectedItems);

              restorePreviousState();
    }

    private void saveCurrentState() {
        previousBalance = balance;
        previousSpending = spending;
        previousSavingGoal = savingGoal;
    }

    private void restorePreviousState() {
        balance = previousBalance;
        spending = previousSpending;
        savingGoal = previousSavingGoal;
        updateLabels();
    }

    @FXML
    public void SwitchToExit() {
        // Buat dialog konfirmasi
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Keluar");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin keluar dari aplikasi?");

        // Tampilkan dialog dan tunggu respons pengguna
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Keluar dari aplikasi
                Platform.exit();
            }
        });
    }
}
