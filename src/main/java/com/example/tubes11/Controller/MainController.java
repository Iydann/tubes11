package com.example.tubes11.Controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    @FXML
    private TextArea textAreaMyNote;

    @FXML
    private LineChart financialGraph;

    @FXML
    private PieChart financialChart;

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
    private AnchorPane HomePane;

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

    private final String defaultStyle = "-fx-text-fill: #000000; -fx-background-color: #FFFFFF; -fx-effect: dropshadow(three-pass-box, #AAAAAA, 3, 0, 0, 3); -fx-pref-width: 150; -fx-font-size: 1.5em; -fx-font-family: 'Calibri Light'; -fx-pref-height: 40";
    private final String activeStyle = "-fx-text-fill: #FFFFFF; -fx-background-color: #212121;-fx-effect: dropshadow(three-pass-box,  #212121, 3, 0, 0, 3); -fx-pref-width: 150; -fx-font-size: 1.5em; -fx-font-family: 'Calibri Light'; -fx-pref-height: 40";
    private String username;

    private void resetButtonStyles() {
        buttonDashboard.setStyle(defaultStyle);
        buttonGraph.setStyle(defaultStyle);
        buttonAlarm.setStyle(defaultStyle);
        buttonMore.setStyle(defaultStyle);
        buttonChart.setStyle(defaultStyle);
        buttonHome.setStyle(defaultStyle);
    }

    @FXML
    public void handleSwitchToHome() {
        HomePane.setVisible(true);
        DashboardPane.setVisible(false);
        GraphPane.setVisible(false);
        AlarmPane.setVisible(false);
        MorePane.setVisible(false);
        ChartPane.setVisible(false);
        resetButtonStyles();
        buttonHome.setStyle(activeStyle);
    }

    @FXML
    public void handleSwitchToDashboard() {
        HomePane.setVisible(false);
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
        HomePane.setVisible(false);
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
        HomePane.setVisible(false);
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
        HomePane.setVisible(false);
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
        HomePane.setVisible(false);
        DashboardPane.setVisible(false);
        GraphPane.setVisible(false);
        AlarmPane.setVisible(false);
        MorePane.setVisible(true);
        ChartPane.setVisible(false);
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
        // Adding options to ChoiceBox
        transactionTypeChoiceBox.setItems(FXCollections.observableArrayList(
                "Income", "Expense"
        ));

        updateLabels();

        transactionListView.setItems(transactionList);

        addButton.setOnAction(event -> addTransaction());
        addsavingbutton.setOnAction(event -> addSavingGoal());
        clearAllButton.setOnAction(event -> clearSelectedTransactions()); // Event handler for clearAllButton

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


    private void updateLabels() {
        balanceAmount.setText(String.format("Rp. %.2f", balance));
        spendingAmount.setText(String.format("Rp. %.2f", spending));
        incomeAmount.setText(String.format("Rp. %.2f", totalIncome)); // Update label incomeAmount
        Savinggoal.setText(String.format("Rp. %.2f", savingGoal));
        Savinggoal1.setText(String.format("Rp. %.2f", balance - savingGoal));
        updatePieChart();
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
        updatePieChart();

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

    public void setUsername(String username) {
        this.username = username;
        welcomeLabel.setText("Welcome, " + username);
    }

}