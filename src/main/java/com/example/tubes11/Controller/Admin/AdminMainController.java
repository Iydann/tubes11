package com.example.tubes11.Controller.Admin;

import com.example.tubes11.Controller.User.MainController;

public class AdminMainController extends MainController {
    @Override
    public void initialize() {
        // Panggil initialize() dari kelas induk untuk menjalankan inisialisasi yang sudah ada
        super.initialize();

        // Mengubah editable dari homeTextArea dan updateLogTextArea menjadi true
        homeTextArea.setEditable(true);
        updateLogTextArea.setEditable(true);
    }
}