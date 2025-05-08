package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Models.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOError;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    protected ChoiceBox<String> userCheckBox;
    @FXML
    protected PasswordField passwordField;
    @FXML
    protected TextField emailField;
    @FXML
    protected Button LoginButton;
    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws IOError, IOException, ParseException {
        String email = emailField.getText();
        String password = passwordField.getText();
        String userType = userCheckBox.getValue();
        System.out.print(email+password+userType+"\n");
        if(Objects.equals(userType, "Employee")){
            Employee isPresent = Common.isUserValid(userType, email, password);
            if(isPresent != null){
                Helper.changeCurrentScreen(event,"home-employee.fxml");
                Home_EmployeeController.getInstance().setData(isPresent);
            }
            else {
                Helper.showModal("Login - Error","Wrong Credential!!!\nEnter Again");
            }
        }
        else if(Objects.equals(userType, "Admin")){
            if(Objects.equals(email, "admin@shop.com") && Objects.equals(password, "password")){
                Helper.changeCurrentScreen(event,"home-admin.fxml");
            }
            else {
                Helper.showModal("Login - Error","Wrong Credential!!!\nEnter Again");
            }
        }
        else {
            Helper.showModal("Login - Error","Wrong Credential!!!\nEnter Again");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> names = FXCollections.observableArrayList("Admin", "Employee");
        userCheckBox.setValue(names.get(0));
        userCheckBox.setItems(names);
    }
}