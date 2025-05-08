package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Common;
import com.example.shopexpensetracker.Models.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static java.time.DayOfWeek.MONDAY;
public class ReportController implements Initializable {
    public TableView<Report> reportTable;
    public TableColumn<Report, Date> reportDate;
    public TableColumn<Report, String> reportTitle;
    public TableColumn<Report, Double> reportAmount;
    public ChoiceBox<String> reportDurationChoice;
    public ObservableList<Report> reports;

    {
        try {
            reports = Common.getReport();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Label reportRevenue;
    public Label reportExpense;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> durations = FXCollections.observableArrayList("All", "Yearly", "Monthly", "Weekly");
        reportDurationChoice.setValue(durations.get(0));
        reportDurationChoice.setItems(durations);
        reportDate.setCellValueFactory(new PropertyValueFactory<>("reportDate"));
        reportTitle.setCellValueFactory(new PropertyValueFactory<>("reportTitle"));
        reportAmount.setCellValueFactory(new PropertyValueFactory<>("reportAmount"));
        reportTable.setItems(reports);
        calculateReport();
    }

    private void calculateReport() {
        double expense=0,revenue=0;
        for (Report report: reports) {
            if(report.getReportAmount()>0){
                revenue+=report.getReportAmount();
            }
            else{
                expense+=report.getReportAmount();
            }
        }
        reportRevenue.setText("$"+revenue);
        reportExpense.setText("-$"+Math.abs(expense));
    }

    public void onSelectDuration(ActionEvent e) throws IOException, ParseException {
        String durationChoice = reportDurationChoice.getValue();

        if(Objects.equals(durationChoice, "Yearly")){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String dateLimit = formatter.format(LocalDate.now().withDayOfYear(1));
            reports.setAll(Common.getReport(dateLimit));
            reportTable.setItems(reports);
        }else if(Objects.equals(durationChoice, "Monthly")){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String dateLimit = formatter.format(LocalDate.now().withDayOfMonth(1));
            reports.setAll(Common.getReport(dateLimit));
            reportTable.setItems(reports);
        }else if(Objects.equals(durationChoice, "Weekly")){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String dateLimit = formatter.format(LocalDate.now().with(previousOrSame(MONDAY)));
            reports.setAll(Common.getReport(dateLimit));
            reportTable.setItems(reports);
        }else{
            reports.setAll(Common.getReport());
            reportTable.setItems(reports);
        }
        calculateReport();
    }
}
