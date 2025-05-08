package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Models.Product;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Product_EditController implements Initializable {

    public TextField productStock;
    public TextField productName;
    public TextField productPrice;
    public AnchorPane editProductModal;
    public Button closeButton;
    public Button saveButton;
    private Product selectedProduct;

    public void setData(Product product){
        this.selectedProduct = product;
        productName.setText(product.getProductName());
        productStock.setText(String.valueOf(product.getProductStock()));
        productPrice.setText(String.valueOf(product.getProductPrice()));
    }

    public void closeModal(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater( () -> editProductModal.requestFocus() );
    }

    public void onSave() throws IOException {
        System.out.println(productName.getText());
        System.out.println(productStock.getText());
        System.out.println(productPrice.getText());
        String name = productName.getText();
        int stock = Integer.parseInt(productStock.getText());
        double price = Double.parseDouble(productPrice.getText());
        Admin.editProduct(selectedProduct,name,stock,price);
        Product_ManageController.getInstance().refreshData();
        closeModal();
    }
}
