package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Common;
import com.example.shopexpensetracker.Helper;
import com.example.shopexpensetracker.Models.Product;
import com.example.shopexpensetracker.Models.ProductCoupon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SellProductController implements Initializable {

    public ChoiceBox<String> productList;
    public Label productName;
    public Label productPrice;
    public Label productStock;
    public TextField productSellQuantity;
    public Label totalPrice;
    public ObservableList<Product> products;
    public Product product;
    public TextField productCouponField;
    public Double employeeDiscountRate = 0.05;
    public Double couponDiscount = 0.00;
    public CheckBox isEmployeeField;
    double finalPrice;
    public ObservableList<ProductCoupon> availableProductCoupon;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            refresh();
            availableProductCoupon = Common.getAllProductCoupon();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void refresh() throws IOException {
        products = Common.getAllProduct();
        ObservableList<String> list = FXCollections.observableArrayList();
        for (Product product:products) {
            list.add("ID: "+product.getProductID()+" - "+product.getProductName());
        }
        setData(products.get(0));
        this.product = products.get(0);
        productList.setItems(list);
        productList.setValue("ID: "+products.get(0).getProductID()+" - "+products.get(0).getProductName());
    }
    public void calculatePrice() {
        if(!NumberUtils.isCreatable(productSellQuantity.getText())){
            Helper.showModal("Wrong Input","Input is not a number \nEnter Again");
            productSellQuantity.setText("");
        }
        else{
            int productOrder = Integer.parseInt(productSellQuantity.getText());
            finalPrice = product.getProductPrice() * productOrder;
            finalPrice -= finalPrice * couponDiscount;
            if(isEmployeeField.isSelected()){
                double discount = productOrder * product.getProductPrice() * employeeDiscountRate;
                finalPrice -= discount;
            }
            totalPrice.setText("$ "+ String.format("%.2f",finalPrice));
        }
    }

    public void sellProduct(ActionEvent actionEvent) throws IOException {
        if(productSellQuantity.getText().isEmpty()){
            resetUI();
            Helper.showModal("Empty Input","Input is empty \nEnter Again");
        }
        else if(!NumberUtils.isCreatable(productSellQuantity.getText())){
            resetUI();
            Helper.showModal("Wrong Input","Input is not a number \nEnter Again");
        }
        else {
            if(Integer.parseInt(productSellQuantity.getText())>product.getProductStock()){
                Helper.showModal("Wrong Input","Order amount should be minimum than stock amount \nEnter Again");
            }
            else{
                String reportTitle = "Sold (" + product.getProductName() + ")" + " X " + productSellQuantity.getText();
                int productOrder = Integer.parseInt(productSellQuantity.getText());
//                double reportAmount = productOrder * product.getProductPrice();
                int remainStock = product.getProductStock() - productOrder;
                Common.sellProduct("Product",product.getProductName(),remainStock);

                Common.addReport(reportTitle,finalPrice);
                if(isEmployeeField.isSelected()){
                    Home_EmployeeController controller = Home_EmployeeController.getInstance();
                    double updatedSalary =  Common.reduceSalary(controller.getEmployee(),finalPrice);
                    controller.getEmployee().setBalance(updatedSalary);
                    controller.setData(controller.getEmployee());
                }

                Helper.showModal("Selling Successful",
                        "Product Name: " +product.getProductName()+"\n"
                                +"Sell Amount: " +productSellQuantity.getText()+"\n"
                                +"Sell Price Per Piece: $" +product.getProductPrice()+"\n"
                                +"Total Price: " +totalPrice.getText()+"\n");
                refresh();
            }
        }
        productSellQuantity.setText("");
    }
    public Product findProduct(int ID) {
        System.out.println(ID);
        for (Product product: products) {
            if(product.getProductID()==ID){
                return product;
            }
        }
        return null;
    }
    public void onSelectProduct(ActionEvent actionEvent) {
        int productSelectID = Integer.MIN_VALUE;
        if(productList.getValue()!=null){
            productSelectID= Integer.parseInt(productList.getValue().split(" ")[1]); // for selecting ID
        }
        Product p = findProduct(productSelectID);

        if(p!=null){
            setData(p);
            this.product = p;
            resetUI();
        }
        else{
            setData(products.get(0));
            this.product = products.get(0);
        }
    }
    private void resetUI() {
        totalPrice.setText("$ ");
        productSellQuantity.setText("");
        productCouponField.setText("");
    }

    private void setData(Product p) {
        productName.setText(p.getProductName());
        productPrice.setText("$"+ p.getProductPrice());
        productStock.setText(String.valueOf(p.getProductStock()));
    }

    public void validateCoupon() {
        String couponCode =  productCouponField.getText();
        couponDiscount = 0.00;
        for (ProductCoupon productCoupon : availableProductCoupon) {
            if (Objects.equals(productCoupon.getProductName(), product.getProductName()) &&Objects.equals(couponCode, productCoupon.getCouponCode())) {
                Helper.showModal("Successful", "Coupon for " + productCoupon.getDiscountPercentage()*100 + "% discount added successfully");
                couponDiscount = productCoupon.getDiscountPercentage();
            }
        }
        if(couponDiscount==0.00){
            productCouponField.setText("");
            Helper.showModal("Invalid Coupon", "Coupon is not valid \nTry again!");
        }
        calculatePrice();
    }

    public void isEmployee(ActionEvent actionEvent) {
        calculatePrice();
    }
}
