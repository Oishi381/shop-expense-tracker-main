package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Models.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Home_AdminController implements Initializable {
    public Button logoutBtn;
    public Button payBillsBtn;
    public Button paySalaryBtn;
    public Button buyBtn;
    public Button productManageBtn;
    public Button couponDiscountBtn;
    @FXML
    protected Button addProductBtn;
    @FXML
    protected Button reportBtn;
    @FXML
    protected StackPane contentArea;

    public static Home_AdminController instance;

    public Home_AdminController(){
        instance = this;
    }
    public static Home_AdminController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            toProductManage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void toProductManage () throws IOException {
        Helper.changeScreenAt(contentArea,"product-manage.fxml");
    }
    public void toReport () throws IOException {
        Helper.changeScreenAt(contentArea,"report.fxml");
    }
    public void toAddProduct () throws IOException {
        Helper.changeScreenAt(contentArea,"add-product.fxml");
    }
    public void toPaySalary() throws IOException{
        Helper.changeScreenAt(contentArea,"pay-salary.fxml");
    }
    public void toBuy() throws IOException{
        Helper.changeScreenAt(contentArea,"buy-product.fxml");
    }
    public void toPayBills() throws IOException{
        Helper.changeScreenAt(contentArea,"pay-bills.fxml");
    }
    public void toBuyProductFinal(Product product) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("buy-product-final.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();

        BuyProductFinalController controller = fxmlLoader.getController();
        controller.setData(product);

        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(anchorPane);
    }

    public void toHome(ActionEvent event) throws IOException {
        Helper.changeCurrentScreen(event, "login.fxml");
    }
    public void toCoupon() throws IOException {
        Helper.changeScreenAt(contentArea,"product-coupon.fxml");
    }
}