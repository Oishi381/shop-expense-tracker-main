package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Common;
import com.example.shopexpensetracker.Models.Product;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Product_ListController implements Initializable{
    @FXML
    private TableView<Product> table;
    @FXML
    private TableColumn<Product, Integer> productID;
    @FXML
    private TableColumn<Product, String> productName;
    @FXML
    private TableColumn<Product, Double> price;
    @FXML
    private TableColumn<Product, Integer> stock;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        price.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        stock.setCellValueFactory(new PropertyValueFactory<>("productStock"));
        try {
            table.setItems(Common.getAllProduct());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}