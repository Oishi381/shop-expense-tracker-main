package com.example.shopexpensetracker;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.math.NumberUtils;
import java.io.IOException;


public class AddProductController {

    public TextField addProductSellPrice;
    public TextField addProductBuyPrice;
    @FXML
    private Button addProduct;
    @FXML
    private TextField addProductName;
    @FXML
    private TextField addProductStock;
    @FXML
    protected void onAddProduct() throws IOException {

        if(addProductSellPrice.getText().isEmpty() || addProductBuyPrice.getText().isEmpty() || addProductName.getText().isEmpty() || addProductStock.getText().isEmpty()){
            Helper.showModal("Wrong Input","Empty Input \nEnter Again");
        }
        else if(!NumberUtils.isCreatable(addProductSellPrice.getText()) || !NumberUtils.isCreatable(addProductBuyPrice.getText()) || !NumberUtils.isCreatable(addProductStock.getText())){
            Helper.showModal("Wrong Input","Invalid Number \nEnter Again");
        }
        else {
            String ProductName = addProductName.getText().trim();
            double ProductSellPrice = Double.parseDouble(addProductSellPrice.getText());
            double ProductBuyPrice = Double.parseDouble(addProductBuyPrice.getText());
            int ProductStock = Integer.parseInt(addProductStock.getText());
            System.out.println(ProductName);
            System.out.println(ProductSellPrice);
            System.out.println(ProductBuyPrice);
            System.out.println(ProductStock);

            Admin.AddProduct(ProductName,ProductBuyPrice,ProductSellPrice,ProductStock);
            Helper.showModal("Successful","Product has been added successfully.");

        }
        //Clearing the input boxes
        addProductName.setText("");
        addProductBuyPrice.setText("");
        addProductSellPrice.setText("");
        addProductStock.setText("");
    }

    public void toProductManage(MouseEvent mouseEvent) throws IOException {
        Home_AdminController controller = Home_AdminController.getInstance();
        controller.toProductManage();
    }
}