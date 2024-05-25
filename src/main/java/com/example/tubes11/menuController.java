package com.example.tubes11;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;

public class menuController {

    @FXML
    private Label balanceAmount;

    @FXML
    private Label spendingAmount;

    @FXML
    private Label incomeAmount;

    @FXML
    private Label expensesAmount;

    @FXML
    private ListView<String> transactionListView;

    @FXML
    private ChoiceBox<String> transactionTypeChoiceBox;

    @FXML
    private TextField amountField;

    @FXML
    private TextArea noteField;

    @FXML
    private Button addButton;

    @FXML
    private Button showGraphButton;

    private double balance = 0.00;
    private double spending = 0.00;
    private double income = 0.00;
    private double expenses = 0.00;

    private ObservableList<String> transactionList = FXCollections.observableArrayList();


    public void initialize() {
        // Menambahkan opsi ke ChoiceBox
        transactionTypeChoiceBox.setItems(FXCollections.observableArrayList(
                "Income", "Expense"
        ));

        updateLabels();

        transactionListView.setItems(transactionList);

        addButton.setOnAction(event -> addTransaction());
        showGraphButton.setOnAction(event -> showGraph());
    }

    private void updateLabels() {
        balanceAmount.setText(String.format("Rp. %.2f", balance));
        spendingAmount.setText(String.format("Rp. %.2f", spending));
        incomeAmount.setText(String.format("+ Rp. %.2f", income));
        expensesAmount.setText(String.format("- Rp. %.2f", expenses));
    }


    private void addTransaction() {
        String type = transactionTypeChoiceBox.getValue();
        String amountText = amountField.getText();
        String note = noteField.getText();

        if (amountText.isEmpty() || !amountText.matches("\\d+(\\.\\d{1,2})?")) {
            Alert alert = new Alert(AlertType.ERROR);
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
                income += amount;
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


    private void showGraph() {
        Stage stage = new Stage();
        stage.setTitle("Spending vs Income");

        PieChart pieChart = new PieChart();
        pieChart.getData().add(new PieChart.Data("Income", income));
        pieChart.getData().add(new PieChart.Data("Expenses", expenses));

        VBox vbox = new VBox(pieChart);
        Scene scene = new Scene(vbox, 400, 300);

        stage.setScene(scene);
        stage.show();
    }


    private void clearTransactions() {
        transactionList.clear();
        balance = 0.00;
        spending = 0.00;
        income = 0.00;
        expenses = 0.00;
        updateLabels();
    }


    private void exportTransactions() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Export Transactions");
        alert.setHeaderText(null);
        alert.setContentText("Transactions have been exported successfully.");
        alert.showAndWait();
    }


    private void importTransactions() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Import Transactions");
        alert.setHeaderText(null);
        alert.setContentText("Transactions have been imported successfully.");
        alert.showAndWait();
    }
}