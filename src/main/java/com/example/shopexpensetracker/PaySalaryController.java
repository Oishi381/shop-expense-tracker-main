package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Models.Employee;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

public class PaySalaryController implements Initializable {

    public TableColumn<Employee,Integer> employeeID;
    public TableColumn<Employee,String> employeeName;
    public TableColumn<Employee,String> employeeEmail;
    public TableColumn<Employee, String> employeePaid;
    public TableColumn<Employee,String> employeeAction;
    public TableView<Employee> salaryTable;
    private Employee selectedEmployee;
    public static PaySalaryController instance;
    public PaySalaryController(){
        instance = this;
    }
    public static PaySalaryController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadEmployeeTable();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadEmployeeTable() throws IOException, ParseException {
        employeeID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        employeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        employeeEmail.setCellValueFactory(new PropertyValueFactory<>("employeeEmail"));
        employeePaid.setCellValueFactory(new PropertyValueFactory<>("employeePaid"));

        //add cell of button edit
        Callback<TableColumn<Employee, String>, TableCell<Employee, String>> cellFactory = (TableColumn<Employee, String> param) -> {
            // make cell containing buttons
            final TableCell<Employee, String> cell = new TableCell<Employee, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                    } else {
                        Button paySalary = new Button("Pay");
                        paySalary.setStyle("-fx-background-color: #ff9100");
                        paySalary.setCursor(Cursor.HAND);

                        paySalary.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                paySalary();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        setGraphic(paySalary);
                    }
                    setText(null);
                }
            };
            return cell;
        };
        employeeAction.setCellFactory(cellFactory);
        refreshTable();
    }

    public void refreshTable() throws IOException, ParseException {
        salaryTable.setItems(Admin.getAllEmployee());
    }

    public void paySalary() throws IOException {
        selectedEmployee = salaryTable.getSelectionModel().getSelectedItem();
        if(selectedEmployee != null){
            FXMLLoader loader = new FXMLLoader ();
            loader.setLocation(getClass().getResource("pay-salary-modal.fxml"));

            loader.load();

            PaySalaryModalController employee_salaryController = loader.getController();
            employee_salaryController.setData(selectedEmployee);
            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } else{
            Helper.showModal("Selection Error!", "Please select one employee and click the pay button again.");
        }
    }
}