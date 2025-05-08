package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Models.ProductCoupon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductCouponController implements Initializable {
    public TableView<ProductCoupon> couponTable;
    public TableColumn<ProductCoupon, String> couponProductName;
    public TableColumn<ProductCoupon, String> couponCode;
    public TableColumn<ProductCoupon, String> discountPercentage;
    public TableColumn<ProductCoupon, String> couponAction;
    public static ProductCouponController instance;

    public ProductCouponController(){
        instance = this;
    }
    public static ProductCouponController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadCouponTable();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCouponTable() throws IOException {
        couponProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        couponCode.setCellValueFactory(new PropertyValueFactory<>("couponCode"));
        discountPercentage.setCellValueFactory(new PropertyValueFactory<>("discountPercentage"));
//add cell of button edit
        Callback<TableColumn<ProductCoupon, String>, TableCell<ProductCoupon, String>> cellFactory = (TableColumn<ProductCoupon, String> param) -> {
            // make cell containing buttons
            final TableCell<ProductCoupon, String> cell = new TableCell<ProductCoupon, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);

                    } else {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);

                        deleteIcon.setCursor(Cursor.HAND);
                        deleteIcon.setGlyphSize(28);
                        deleteIcon.setFill(Color.valueOf("#ff1744"));

                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            deleteProduct();
                        });

                        HBox btnWrapper = new HBox( deleteIcon);
                        btnWrapper.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));

                        setGraphic(btnWrapper);
                    }
                    setText(null);
                }
            };
            return cell;
        };
        couponAction.setCellFactory(cellFactory);
        refreshData();
    }

    public void refreshData() throws IOException {
        couponTable.setItems(Common.getAllProductCoupon());
    }

    private void deleteProduct() {
        ProductCoupon selectedProduct = couponTable.getSelectionModel().getSelectedItem();
        Optional<ButtonType> confirm = Helper.showConfirmationBox("Are you sure you want to delete " +  selectedProduct.getProductName()+ "?");

        if(confirm.isPresent() && confirm.get()== ButtonType.OK){
            System.out.println("====Clicked====");
            try {
                Common.deleteProductCoupon(selectedProduct.getProductName());
                couponTable.setItems(Common.getAllProductCoupon());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void addProductCoupon() throws IOException {
        FXMLLoader loader = new FXMLLoader ();
        loader.setLocation(getClass().getResource("product-coupon-modal.fxml"));
        loader.load();

        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }
}
