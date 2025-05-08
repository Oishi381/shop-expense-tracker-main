package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Models.Product;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BuyProductController implements Initializable {

    public GridPane grid;
    public ScrollPane scroll;
    private final List<Product> products = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            products.addAll(Admin.getThirdPartyProduct());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int column = 0;
        int row = 1;
        try {
            for (Product product : products) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("third-party-product-card.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ThirdPartyProductController productController = fxmlLoader.getController();
                productController.setData(product);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row); //(child,column,row)
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
//                grid.setHgap(10);
//                grid.setVgap(10);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}