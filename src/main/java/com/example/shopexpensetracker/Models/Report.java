package com.example.shopexpensetracker.Models;

public class Report {

    private final String reportDate;
    private final String reportTitle;
    private final Double reportAmount;

    public Report(String reportDate, String reportTitle, Double reportAmount) {
        this.reportDate = reportDate;
        this.reportTitle = reportTitle;
        this.reportAmount = reportAmount;
    }

    public String getReportDate() {
        return reportDate;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public Double getReportAmount() {
        return reportAmount;
    }
}
