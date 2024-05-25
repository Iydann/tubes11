package com.example.tubes11.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {

    @FXML
    private Label Savinggoal;

    @FXML
    private Label Savinggoal1;

    @FXML
    private Button addButton;

    @FXML
    private Button addsavingbutton;

    @FXML
    private TextField amountField;

    @FXML
    private Label balanceAmount;

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
        String type = (String) transactionTypeChoiceBox.getValue();
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

        savingGoal = Double.parseDouble(amountText);
        updateLabels();
        savingfield.clear();
    }

    private void clearSelectedTransactions() {
        ObservableList<String> selectedItems = transactionListView.getSelectionModel().getSelectedItems();
        transactionList.removeAll(selectedItems);
        updateLabelsAfterClearing(selectedItems);
    }

    private void updateLabelsAfterClearing(ObservableList<String> clearedItems) {
        for (String transaction : clearedItems) {
            String[] parts = transaction.split(": Rp. ");
            String type = parts[0];
            double amount = Double.parseDouble(parts[1].split(" - ")[0]);

            switch (type) {
                case "Income":
                    balance -= amount;
                    break;
                case "Expense":
                    balance += amount;
                    spending -= amount;
                    break;
                default:
                    // Handle any unexpected types
                    break;
            }
        }

        updateLabels();
    }
}
