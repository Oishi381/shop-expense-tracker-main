package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class ThirdPartyProductController {
    public Button buyProductBtn;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label stockLabel;

    private Product product;

    public void setData(Product product) {
        this.product = product;
        productNameLabel.setText(product.getProductName());
        priceLabel.setText("Product Price: $" + product.getProductPrice());
        stockLabel.setText("Available: " + product.getProductStock());
    }

    public void toBuyProductFinal() throws IOException {
        Home_AdminController.getInstance().toBuyProductFinal(this.product);
    }
}
