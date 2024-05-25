package com.example.tubes11.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    @FXML
    private LineChart financialGraph;

    @FXML
    private AnchorPane DashboardPane;

    @FXML
    private AnchorPane ChartPane;

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
    private Button setTypeGraph;

    @FXML
    private Button addButton;

    @FXML
    private Button addsavingbutton;

    @FXML
    private Button buttonDashboard;

    @FXML
    private Button buttonChart;

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
        buttonChart.setStyle(defaultStyle);
    }

    @FXML
    public void handleSwitchToDashboard() {
        DashboardPane.setVisible(true);
        GraphPane.setVisible(false);
        AlarmPane.setVisible(false);
        MorePane.setVisible(false);
        ChartPane.setVisible(false);
        resetButtonStyles();
        buttonDashboard.setStyle(activeStyle);
    }

    @FXML
    public void handleSwitchToPieChart() {
        DashboardPane.setVisible(false);
        GraphPane.setVisible(false);
        AlarmPane.setVisible(false);
        MorePane.setVisible(false);
        ChartPane.setVisible(true);
        resetButtonStyles();
        buttonChart.setStyle(activeStyle);
    }

    @FXML
    public void handleSwitchToGraph() {
        DashboardPane.setVisible(false);
        GraphPane.setVisible(true);
        AlarmPane.setVisible(false);
        MorePane.setVisible(false);
        ChartPane.setVisible(false);
        resetButtonStyles();
        buttonGraph.setStyle(activeStyle);
    }

    @FXML
    public void handleSwitchToAlarm() {
        DashboardPane.setVisible(false);
        GraphPane.setVisible(false);
        AlarmPane.setVisible(true);
        MorePane.setVisible(false);
        ChartPane.setVisible(false);
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

    private Map<LocalDate, Double> incomePerDay = new HashMap<>();
    private Map<LocalDate, Double> expensePerDay = new HashMap<>();

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
        String amountText = amountField.getText().replace(",", "."); // Mengganti koma dengan titik
        String note = noteField.getText();

        if (amountText.isEmpty() || !amountText.matches("\\d+(\\.\\d{1,2})?")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid amount.");
            alert.showAndWait();
            return;
        }

        // Menyimpan keadaan saat ini sebelum memodifikasi
        saveCurrentState();

        double amount = Double.parseDouble(amountText);

        // Mendapatkan tanggal dari label tanggal (dateLabel)
        String formattedDate = dateLabel.getText();

        // Membuat item transaksi dengan tanggal
        String transaction = String.format("%s - %s - Rp. %.2f - %s", formattedDate, type, amount, note);

        transactionList.add(transaction);

        // Menghitung ulang balance dan spending dari transactionList
        recalculateBalanceAndSpending();

        updateLineChart();
        updateLabels();

        amountField.clear();
        noteField.clear();
    }

    private void clearSelectedTransactions() {
        // Mendapatkan item yang dipilih dari transactionListView
        ObservableList<String> selectedItems = transactionListView.getSelectionModel().getSelectedItems();

        // Menghapus transaksi yang dipilih dari transactionList
        transactionList.removeAll(selectedItems);

        // Menghitung ulang balance dan spending
        recalculateBalanceAndSpending();

        updateLineChart();

        // Memperbarui label
        updateLabels();
    }
    private void updateLineChart() {
        // Bersihkan line chart dari data sebelumnya
        financialGraph.getData().clear();

        // Buat series baru untuk income dan expense
        XYChart.Series<String, Double> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        XYChart.Series<String, Double> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");

        // Loop melalui transactionList dan tambahkan data ke series yang sesuai
        for (String transaction : transactionList) {
            String[] parts = transaction.split(" - ");
            String dateString = parts[0];
            String type = parts[1];
            double amount = Double.parseDouble(parts[2].replace("Rp. ", "").replace(",", "."));

            // Tambahkan data ke series yang sesuai berdasarkan jenis transaksi
            if (type.equals("Income")) {
                incomeSeries.getData().add(new XYChart.Data<>(dateString, amount));
            } else if (type.equals("Expense")) {
                expenseSeries.getData().add(new XYChart.Data<>(dateString, amount));
            }
        }

        // Tambahkan series ke line chart
        financialGraph.getData().addAll(incomeSeries, expenseSeries);
    }

    private void recalculateBalanceAndSpending() {
        double totalIncome = 0;
        double totalExpense = 0;
        for (String trans : transactionList) {
            String[] parts = trans.split(" - ");
            // Memparsing jumlah dari string transaksi, menangani simbol mata uang dan koma
            String amountString = parts[2].replace("Rp. ", "").replace(".", "").replace(",", ".");
            double transAmount = Double.parseDouble(amountString);
            if (parts[1].equals("Income")) {
                totalIncome += transAmount;
            } else if (parts[1].equals("Expense")) {
                totalExpense += transAmount;
            }
        }
        balance = totalIncome - totalExpense;
        spending = totalExpense;
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

    private void calculateTotalPerDay() {
        incomePerDay.clear();
        expensePerDay.clear();

        for (String transaction : transactionList) {
            String[] parts = transaction.split(":");
            if (parts.length == 2) {
                String type = parts[0].trim();
                String amountStr = parts[1].trim().replace("Rp.", "").replace(",", "");
                double amount = Double.parseDouble(amountStr);

                LocalDate transactionDate = LocalDate.parse(dateLabel.getText());
                Map<LocalDate, Double> targetMap = type.equals("Income") ? incomePerDay : expensePerDay;
                targetMap.put(transactionDate, targetMap.getOrDefault(transactionDate, 0.0) + amount);
            }
        }
    }

    private void saveCurrentState() {
        previousBalance = balance;
        previousSpending = spending;
        previousSavingGoal = savingGoal;
    }

    @FXML
    private void dateChange() {
        // Buat jendela pengaturan tanggal
        Stage stage = new Stage();
        VBox vbox = new VBox();

        Label label = new Label("Pilih tanggal:");
        DatePicker datePicker = new DatePicker();

        Button button = new Button("OK");
        button.setOnAction(e -> {
            if (datePicker.getValue() != null) {
                // Mendapatkan tanggal dari date picker
                LocalDate selectedDate = datePicker.getValue();

                // Menampilkan tanggal dalam format yang diinginkan
                String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, yyyy-MM-dd"));

                // Menampilkan tanggal yang dipilih dalam sebuah jendela peringatan
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Tanggal yang Dipilih");
                alert.setHeaderText(null);
                alert.setContentText(formattedDate);
                alert.showAndWait();

                // Update label dateLabel dengan tanggal yang dipilih
                dateLabel.setText(formattedDate);
            } else {
                // Jika tidak ada tanggal yang dipilih, tampilkan pesan kesalahan
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Kesalahan");
                alert.setHeaderText(null);
                alert.setContentText("Silakan pilih tanggal.");
                alert.showAndWait();
            }

            // Tutup jendela setelah tombol OK ditekan
            stage.close();
        });

        vbox.getChildren().addAll(label, datePicker, button);

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
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