package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductCouponModalController implements Initializable {
    public ChoiceBox<String> productChoiceList;
    public TextField couponCodeField;
    public TextField discountField;
    public Button closeButton;
    public Product product;
    ObservableList<Product> products;

    public void closeModal(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void onSave(ActionEvent actionEvent) throws IOException {
        if(couponCodeField.getText().isEmpty() || discountField.getText().isEmpty()){
            Helper.showModal("Wrong Input","Empty Input \nEnter Again");
        }
        else if(!NumberUtils.isCreatable(discountField.getText())){
            Helper.showModal("Wrong Input","Invalid Number \nEnter Again");
        }
        else{
            String couponCode = couponCodeField.getText();
            double discountAmount = Double.parseDouble(discountField.getText());
            Admin.AddProductCoupon(product, couponCode, discountAmount);
            ProductCouponController controller = ProductCouponController.getInstance();
            controller.refreshData();
            closeModal();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadList() throws IOException {
        products = Common.getAllProduct();
        ObservableList<String> list = FXCollections.observableArrayList();
        for (Product product:products) {
            list.add("ID: "+product.getProductID()+" - "+product.getProductName());
        }
        productChoiceList.setItems(list);
        this.product = products.get(0);
        productChoiceList.setValue("ID: "+products.get(0).getProductID()+" - "+products.get(0).getProductName());
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
        if(productChoiceList.getValue()!=null){
            productSelectID= Integer.parseInt(productChoiceList.getValue().split(" ")[1]); // for selecting ID
        }
        Product p = findProduct(productSelectID);

        if(p!=null){
            this.product = p;
        }
        else{
            this.product = products.get(0);
        }
    }
}
