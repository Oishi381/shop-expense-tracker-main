package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Models.Product;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;

public class BuyProductFinalController {
    public Label productFinalStock;
    public Label productFinalName;
    public Label productFinalPrice;
    public TextField productFinalQuantity;
    public Label totalBill;
    public TextField productSellPrice;
    private Product product;

    public void setData(Product product) {
        this.product = product;
        System.out.println("data received");
        productFinalName.setText(product.getProductName());
        productFinalPrice.setText("$ " + product.getProductPrice());
        productFinalStock.setText(String.valueOf(product.getProductStock()));
    }

    public void toBuy() throws IOException {
        Home_AdminController.getInstance().toBuy();
    }

    public void calclulateBill(KeyEvent inputMethodEvent) {
        if(!NumberUtils.isCreatable(inputMethodEvent.getText())){
            Helper.showModal("Wrong Input","Input is not a number \nEnter Again");
            productFinalQuantity.setText("");
        }
        else{
           int productOrder = Integer.parseInt(productFinalQuantity.getText());
           double finalPrice = productOrder * product.getProductPrice();
            totalBill.setText("$ "+ finalPrice);
        }
    }

    public void placeOrder() throws IOException {
        if(productFinalQuantity.getText().isEmpty() || productSellPrice.getText().isEmpty()){
            Helper.showModal("Empty Input","Input is empty \nEnter Again");
            productFinalQuantity.setText("");
            productSellPrice.setText("");
        }
        else if(!NumberUtils.isCreatable(productFinalQuantity.getText()) || !NumberUtils.isCreatable(productSellPrice.getText())){
            Helper.showModal("Wrong Input","Input is not a number \nEnter Again");
            productFinalQuantity.setText("");
            productSellPrice.setText("");
        }
        else {
            if(Integer.parseInt(productFinalQuantity.getText())>product.getProductStock()){
                Helper.showModal("Wrong Input","Order amount should be minimum than stock amount \nEnter Again");
            }
            else{
                Admin.AddProduct(product.getProductName(),product.getProductPrice(), Double.parseDouble(productSellPrice.getText()), Integer.parseInt(productFinalQuantity.getText()));

                int remainStock = product.getProductStock()-Integer.parseInt(productFinalQuantity.getText());
                Common.sellProduct("ThirdPartyProduct",product.getProductName(),remainStock);
                setData(new Product(product.getProductID(), product.getProductName(),product.getProductPrice(),remainStock));

                Helper.showModal("Order Successful",
                        "Order Name: " +product.getProductName()+"\n"
                        +"Order Amount: " +productFinalQuantity.getText()+"\n"
                        +"Order Price Per Piece: $" +product.getProductPrice()+"\n"
                        +"Product Sell Price : $" + Double.parseDouble(productSellPrice.getText())+"\n"
                                +"Total Bill: " +totalBill.getText()+"\n");
            }
            productFinalQuantity.setText("");
            productSellPrice.setText("");
        }
    }
}
