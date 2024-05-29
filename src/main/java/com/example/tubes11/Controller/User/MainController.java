package com.example.tubes11.Controller.User;

import com.example.tubes11.Util.DatabaseConnection;
import com.example.tubes11.Util.SwitchHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    @FXML
    private LineChart financialGraph;

    @FXML
    private PieChart financialChart;

    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private AnchorPane chartPane;

    @FXML
    private AnchorPane graphPane;

    @FXML
    private AnchorPane alarmPane;

    @FXML
    private AnchorPane morePane;

    @FXML
    private AnchorPane homePane;

    @FXML
    private Label currentDateLabel;

    @FXML
    private Label currentTimeLabel;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label Savinggoal;

    @FXML
    private Label Savinggoal1;

    @FXML
    private Label balanceAmount;

    @FXML
    private Label incomeAmount;

    @FXML
    private Label SpendingAmount;

    @FXML
    private Button addButton;

    @FXML
    private Button addsavingbutton;

    @FXML
    private Button buttonHome;

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

    private String username;
    private int userId;
    private DatabaseConnection dbConnection = new DatabaseConnection();

    private SwitchHandler switchHandler;

    @FXML
    public void initSwitchPane() {
        switchHandler = new SwitchHandler(homePane, dashboardPane, chartPane,
                graphPane, alarmPane, morePane,
                buttonHome, buttonDashboard, buttonChart,
                buttonGraph, buttonAlarm, buttonMore);
    }

    @FXML
    public void handleSwitchToHome() {
        switchHandler.handleSwitchToHome();
    }

    @FXML
    public void handleSwitchToDashboard() {
        switchHandler.handleSwitchToDashboard();
    }

    @FXML
    public void handleSwitchToPieChart() {
        switchHandler.handleSwitchToPieChart();
    }

    @FXML
    public void handleSwitchToGraph() {
        switchHandler.handleSwitchToGraph();
    }

    @FXML
    public void handleSwitchToAlarm() {
        switchHandler.handleSwitchToAlarm();
    }

    @FXML
    public void handleSwitchToMore() {
        switchHandler.handleSwitchToMore();
    }

    @FXML
    private TextField amountField;

    @FXML
    private Label dateLabel;

    @FXML
    private Label expensesAmount;

    @FXML
    protected TextArea noteField;

    @FXML
    protected TextArea homeTextArea;

    @FXML
    protected TextArea updateLogTextArea;

    @FXML
    private TextField savingfield;

    @FXML
    private Button clearAllButton; // New button for clearing selected transactions

    private double balance = 0.00;
    private double spending = 0.00;
    private double savingGoal = 0.00;
    private double totalIncome = 0.00; // New variable for total income
    private double totalExpense = 0.00; // New variable for total expense

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
        // Untuk mengedit Bila akun adalah admin
        homeTextArea.setEditable(false);
        updateLogTextArea.setEditable(false);

        // Adding options to ChoiceBox
        transactionTypeChoiceBox.setItems(FXCollections.observableArrayList(
                "Income", "Expense"
        ));

        loadTransactions(userId);
        totalIncomeExpense(userId);
        savingGoal = getGoal(userId);
        updateLabels();

        transactionListView.setItems(transactionList);

        addButton.setOnAction(event -> addTransaction(userId));
        addsavingbutton.setOnAction(event -> addSavingGoal(userId));
        clearAllButton.setOnAction(event -> clearSelectedTransactions(userId)); // Event handler for clearAllButton

        recalculateBalanceAndSpending();
        updatePieChart();


        // Inisialisasi Timeline untuk memperbarui waktu secara real-time
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            // Ambil waktu saat ini
            LocalTime currentTime = LocalTime.now();

            // Format waktu sesuai kebutuhan
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = currentTime.format(timeFormatter);

            // Update label waktu
            currentTimeLabel.setText(formattedTime);
        }), new KeyFrame(Duration.seconds(1))); // Perbarui setiap 1 detik

        // Memulai timeline
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();


        // Inisialisasi Timeline untuk memperbarui tanggal secara real-time
        Timeline dateUpdater = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            // Ambil tanggal saat ini
            LocalDate currentDate = LocalDate.now();

            // Format tanggal sesuai kebutuhan
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, yyyy-MM-dd");
            String formattedDate = currentDate.format(dateFormatter);

            // Update label tanggal
            currentDateLabel.setText(formattedDate);
        }), new KeyFrame(Duration.hours(1))); // Perbarui setiap 1 jam (karena tanggal jarang berubah)

// Memulai timeline
        dateUpdater.setCycleCount(Animation.INDEFINITE);
        dateUpdater.play();
    }

    private void loadTransactions(int userId) {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT date, type, amount, description FROM Transactions WHERE user_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            transactionList.clear();

            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String type = resultSet.getString("type");
                double amount = resultSet.getDouble("amount");
                String note = resultSet.getString("description");

                String transaction = String.format("%s - %s - Rp. %.2f - %s", date, type, amount, note);
                transactionList.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private double getGoal(int userId) {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT amount FROM Goal WHERE user_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.00;
    }

    private void totalIncomeExpense (int userId) {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT type, amount FROM Transactions WHERE user_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            double income = 0.00;
            double expense = 0.00;

            while (resultSet.next()) {
                String type = resultSet.getString("type");
                double amount = resultSet.getDouble("amount");

                if (type.equals("Income")) {
                    income += amount;
                } else if (type.equals("Expense")) {
                    expense += amount;
                }
            }

            this.totalIncome = income;
            this.totalExpense = expense;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateLabels() {
        balanceAmount.setText(String.format("Rp. %.2f", balance));
        spendingAmount.setText(String.format("Rp. %.2f", spending));
        incomeAmount.setText(String.format("Rp. %.2f", totalIncome)); // Update label incomeAmount
        Savinggoal.setText(String.format("Rp. %.2f", savingGoal));
        Savinggoal1.setText(String.format("Rp. %.2f", balance - savingGoal));
        updatePieChart();
    }

    private void addTransaction(int userId) {
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
//        String transaction = String.format("%s - %s - Rp. %.2f - %s", formattedDate, type, amount, note);
//
//        transactionList.add(transaction);

        Connection connection = dbConnection.getConnection();

        String insertTransaction = "INSERT INTO Transactions (user_id, type, amount, description, date) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertTransaction);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, type);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, note);
            preparedStatement.setString(5, formattedDate);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Menghitung ulang balance dan spending dari transactionList
        recalculateBalanceAndSpending();
        updatePieChart();

        updateLineChart();
        updateLabels();

        amountField.clear();
        noteField.clear();
    }

    private void clearSelectedTransactions(int userId) {
        // Mendapatkan item yang dipilih dari transactionListView
//        ObservableList<String> selectedItems = transactionListView.getSelectionModel().getSelectedItems();

        String selectedTransaction = transactionListView.getSelectionModel().getSelectedItem();

        if (selectedTransaction != null) {
            String[] parts = selectedTransaction.split(" - ");
            String date = parts[0];
            String type = parts[1];
            double amount = Double.parseDouble(parts[2].replace("Rp. ", "").replace(",", "."));
            String note = parts[3];

            boolean result = deleteTransaction(userId, date, type, amount, note);
            if (result) {
                // Menghapus transaksi yang dipilih dari transactionList
                transactionList.removeAll(selectedTransaction);

                // Menghitung ulang balance dan spending
                recalculateBalanceAndSpending();

                updateLineChart();

                // Memperbarui label
                updateLabels();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete the transaction. Please try again.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a transaction to delete.");
            alert.showAndWait();
        }
    }

    private boolean deleteTransaction(int userId, String date, String type, double amount, String note) {
        Connection connection = dbConnection.getConnection();

        String query = "DELETE FROM Transactions where user_id = ? AND date = ? AND type = ? AND amount = ? AND description = ? LIMIT 1";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, type);
            preparedStatement.setDouble(4, amount);
            preparedStatement.setString(5, note);

            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updatePieChart() {
        if (financialChart != null) {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Income", totalIncome),
                    new PieChart.Data("Expense", totalExpense)
            );
            financialChart.setData(pieChartData);
        }
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
            double transAmount = Double.parseDouble(parts[2].replace("Rp. ", "").replace(".", "").replace(",", "."));
            if (parts[1].equals("Income")) {
                totalIncome += transAmount;
            } else if (parts[1].equals("Expense")) {
                totalExpense += transAmount;
            }
        }
        balance = totalIncome - totalExpense;
        spending = totalExpense;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
    }

    private void addSavingGoal(int userId) {
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

        Connection connection = dbConnection.getConnection();
        String goalToDb = "INSERT INTO Goal (user_id, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = VALUES(amount)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(goalToDb);
            preparedStatement.setInt(1, userId);
            preparedStatement.setDouble(2, savingGoal);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

    public void setUsername(String username, int userId) {
        this.username = username;
        this.userId = userId;
        welcomeLabel.setText("Welcome, " + username);

        loadTransactions(userId);
    }

}