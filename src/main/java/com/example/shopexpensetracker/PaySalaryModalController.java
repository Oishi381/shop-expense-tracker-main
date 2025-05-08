package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Models.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Objects;
import java.util.ResourceBundle;

public class PaySalaryModalController implements Initializable {
    public Label employeeName;
    public Label employeeEmail;
    public Label employeeLastPaid;
    public TextField slaryAmountField;
    public Button payBtn;
    public Button closeButton;
    public AnchorPane paySalaryModal;
    private Employee selectedEmployee;

    public void setData(Employee employee){
        this.selectedEmployee = employee;
        employeeName.setText(selectedEmployee.getEmployeeName());
        employeeEmail.setText(selectedEmployee.getEmployeeEmail());
        employeeLastPaid.setText(selectedEmployee.getLastPaidDate());
        System.out.println(selectedEmployee.getEmployeePaid());
        System.out.println(Objects.equals(selectedEmployee.getEmployeePaid(), "Not Paid"));
        payBtn.setDisable(true);
        payBtn.setDisable(false);
        if(Objects.equals(selectedEmployee.getEmployeePaid(), "Not Paid")){
            payBtn.setDisable(false);
            slaryAmountField.setDisable(false);
        } else{
          payBtn.setDisable(true);
          slaryAmountField.setDisable(true);
          payBtn.setText("Already Paid");
        }
    }

    public void closeModal(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Platform.runLater(() -> paySalaryModal.requestFocus());
    }

    public void onPay(ActionEvent actionEvent) throws IOException, ParseException {

        if(!NumberUtils.isCreatable(slaryAmountField.getText())){
            Helper.showModal("Wrong Input","Input is not a number \nEnter Again");
            slaryAmountField.setText("");
        }
        else{
            double salaryAmount = Double.parseDouble(slaryAmountField.getText());
            System.out.println("Salary Amount: " + salaryAmount);
            Admin.paySalary(selectedEmployee,salaryAmount);
            String reportTitle = "Salary Paid to (" + selectedEmployee.getEmployeeName() + ") - " + salaryAmount;
            Common.addReport(reportTitle,-1*salaryAmount);
        }
        PaySalaryController.getInstance().refreshTable();
        closeModal();
    }
}
