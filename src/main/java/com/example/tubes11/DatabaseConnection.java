package com.example.tubes11;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaselink;

    public Connection getConnection() {
        String databaseName = "sql12709250";
        String databaseUser = "sql12709250";
        String databasePassword = "NZTRwD45Gn";
        String url = "jdbc:mysql://sql12.freesqldatabase.com/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaselink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaselink;
    }


}
