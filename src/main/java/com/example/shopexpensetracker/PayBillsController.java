package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Admin;
import com.example.shopexpensetracker.Common;
import com.example.shopexpensetracker.Helper;
import com.example.shopexpensetracker.Models.Bill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PayBillsController implements Initializable {
    public TextField billNameField;
    public TableView<Bill> table;
    public TableColumn<Bill,String> ProductName;
    public ChoiceBox<String> billChoiceList;
    public TextField billAmountField;
    ObservableList <Bill> bills = FXCollections.observableArrayList();

    public void addBillName(ActionEvent actionEvent) throws IOException {
        String billName = billNameField.getText();
        if(billName.isEmpty()){
            Helper.showModal("Empty Input","Input is empty \nEnter Again");
        }
        else{
            Admin.addBill(billName);
            Helper.showModal("Successful","Bill Type Added Successfully");
            refresh();
        }
        billNameField.setText("");
    }

    public void onSelectProduct(ActionEvent actionEvent) {
    }

    public void onPayBill(ActionEvent actionEvent) throws IOException {

        String billName = billChoiceList.getValue();

        if(billAmountField.getText().isEmpty()){
            Helper.showModal("Empty Input","Input is empty \nEnter Again");
        }
        else if(!NumberUtils.isCreatable(billAmountField.getText())){
            Helper.showModal("Wrong Input","Input is not a number \nEnter Again");
        }else{
            Common.addReport(billName, Double.parseDouble(billAmountField.getText())*-1);
        }
        billAmountField.setText("");
    }
    public void refresh(){
        ProductName.setCellValueFactory(new PropertyValueFactory<>("billName"));
        try {
            bills = Admin.getBills();
            table.setItems(bills);
            ObservableList<String> choiceBoxContent = FXCollections.observableArrayList();;
            for (Bill bill:bills) {
                choiceBoxContent.add(bill.getBillName());
            }
            billChoiceList.setItems(choiceBoxContent);
            billChoiceList.setValue(choiceBoxContent.get(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refresh();
    }
}