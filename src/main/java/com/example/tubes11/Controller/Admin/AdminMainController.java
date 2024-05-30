package com.example.tubes11.Controller.Admin;

import com.example.tubes11.Controller.User.MainController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

public class AdminMainController extends MainController {
    @FXML
    private TextArea messageAdmin;

    @Override
    public void initialize() {
        // Panggil initialize() dari kelas induk untuk menjalankan inisialisasi yang sudah ada
        super.initialize();

        // Mengubah editable dari homeTextArea dan updateLogTextArea menjadi true
        aksesEditAdmin();
    }

    // Fungsi untuk menampilkan pesan fitur admin
    public void showAdminFeatureMessage() {
        messageAdmin.setText("Anda sedang menggunakan akun admin,\n" +
                "Akun admin dapat mengedit TextArea tampilan Home serta Update Log\n");
        messageAdmin.setVisible(true); // Tampilkan TextArea saat fungsi dijalankan

        // Buat timeline untuk mengatur waktu tampilan TextArea
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            messageAdmin.setVisible(false); // Sembunyikan TextArea setelah 3 detik
        }));
        timeline.play();
    }

    private void aksesEditAdmin() {
        homeTextArea.setEditable(true);
        updateLogTextArea.setEditable(true);
    }

}