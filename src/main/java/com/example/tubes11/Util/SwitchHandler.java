package com.example.tubes11.Util;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class SwitchHandler {
    private AnchorPane HomePane;
    private AnchorPane DashboardPane;
    private AnchorPane ChartPane;
    private AnchorPane GraphPane;
    private AnchorPane AlarmPane;
    private AnchorPane MorePane;
    private Button buttonHome;
    private Button buttonDashboard;
    private Button buttonChart;
    private Button buttonGraph;
    private Button buttonAlarm;
    private Button buttonMore;

    public SwitchHandler(AnchorPane homePane, AnchorPane dashboardPane, AnchorPane chartPane,
                         AnchorPane graphPane, AnchorPane alarmPane, AnchorPane morePane,
                         Button buttonHome, Button buttonDashboard, Button buttonChart,
                         Button buttonGraph, Button buttonAlarm, Button buttonMore) {
        this.HomePane = homePane;
        this.DashboardPane = dashboardPane;
        this.ChartPane = chartPane;
        this.GraphPane = graphPane;
        this.AlarmPane = alarmPane;
        this.MorePane = morePane;
        this.buttonHome = buttonHome;
        this.buttonDashboard = buttonDashboard;
        this.buttonChart = buttonChart;
        this.buttonGraph = buttonGraph;
        this.buttonAlarm = buttonAlarm;
        this.buttonMore = buttonMore;
    }

    public void handleSwitchToHome() {
        HomePane.setVisible(true);
        DashboardPane.setVisible(false);
        GraphPane.setVisible(false);
        AlarmPane.setVisible(false);
        MorePane.setVisible(false);
        ChartPane.setVisible(false);
        ButtonStyleUtil.resetButtonStyles(buttonHome, buttonDashboard, buttonChart, buttonGraph, buttonAlarm, buttonMore);
        ButtonStyleUtil.setActiveStyle(buttonHome);
    }

    public void handleSwitchToDashboard() {
        HomePane.setVisible(false);
        DashboardPane.setVisible(true);
        GraphPane.setVisible(false);
        AlarmPane.setVisible(false);
        MorePane.setVisible(false);
        ChartPane.setVisible(false);
        ButtonStyleUtil.resetButtonStyles(buttonHome, buttonDashboard, buttonChart, buttonGraph, buttonAlarm, buttonMore);
        ButtonStyleUtil.setActiveStyle(buttonDashboard);
    }

    public void handleSwitchToPieChart() {
        HomePane.setVisible(false);
        DashboardPane.setVisible(false);
        GraphPane.setVisible(false);
        AlarmPane.setVisible(false);
        MorePane.setVisible(false);
        ChartPane.setVisible(true);
        ButtonStyleUtil.resetButtonStyles(buttonHome, buttonDashboard, buttonChart, buttonGraph, buttonAlarm, buttonMore);
        ButtonStyleUtil.setActiveStyle(buttonChart);
    }

    public void handleSwitchToGraph() {
        HomePane.setVisible(false);
        DashboardPane.setVisible(false);
        GraphPane.setVisible(true);
        AlarmPane.setVisible(false);
        MorePane.setVisible(false);
        ChartPane.setVisible(false);
        ButtonStyleUtil.resetButtonStyles(buttonHome, buttonDashboard, buttonChart, buttonGraph, buttonAlarm, buttonMore);
        ButtonStyleUtil.setActiveStyle(buttonGraph);
    }

    public void handleSwitchToAlarm() {
        HomePane.setVisible(false);
        DashboardPane.setVisible(false);
        GraphPane.setVisible(false);
        AlarmPane.setVisible(true);
        MorePane.setVisible(false);
        ChartPane.setVisible(false);
        ButtonStyleUtil.resetButtonStyles(buttonHome, buttonDashboard, buttonChart, buttonGraph, buttonAlarm, buttonMore);
        ButtonStyleUtil.setActiveStyle(buttonAlarm);
    }

    public void handleSwitchToMore() {
        HomePane.setVisible(false);
        DashboardPane.setVisible(false);
        GraphPane.setVisible(false);
        AlarmPane.setVisible(false);
        MorePane.setVisible(true);
        ChartPane.setVisible(false);
        ButtonStyleUtil.resetButtonStyles(buttonHome, buttonDashboard, buttonChart, buttonGraph, buttonAlarm, buttonMore);
        ButtonStyleUtil.setActiveStyle(buttonMore);
    }


    // Implementasi metode handleSwitchToDashboard(), handleSwitchToPieChart(), dll. seperti yang Anda inginkan
}
