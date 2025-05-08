package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Models.Product;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
public class Product_ManageController implements Initializable {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> mproductID;
    @FXML
    private TableColumn<Product, String> mproductName;
    @FXML
    private TableColumn<Product, Double> mprice;
    @FXML
    private TableColumn<Product, Integer> mstock;
    @FXML
    private TableColumn<Product, String> maction;
    Product selectedProduct = null;

    public static Product_ManageController instance;

    public Product_ManageController(){
        instance = this;
    }
    public static Product_ManageController getInstance() {
        return instance;
    }

    public void initialize(URL location, ResourceBundle resources) {
        loadTable();
    }
    public void loadTable(){
        mproductID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        mproductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        mprice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        mstock.setCellValueFactory(new PropertyValueFactory<>("productStock"));

        //add cell of button edit
        Callback<TableColumn<Product, String>, TableCell<Product, String>> cellFactory = (TableColumn<Product, String> param) -> {
            // make cell containing buttons
            final TableCell<Product, String> cell = new TableCell<Product, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);

                    } else {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        deleteIcon.setCursor(Cursor.HAND);
                        deleteIcon.setGlyphSize(28);
                        deleteIcon.setFill(Color.valueOf("#ff1744"));

                        editIcon.setCursor(Cursor.HAND);
                        editIcon.setGlyphSize(28);
                        editIcon.setFill(Color.valueOf("#00E676"));

                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            deleteProduct();
                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                editProduct();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        HBox btnWrapper = new HBox(editIcon, deleteIcon);
                        btnWrapper.setStyle("-fx-alignment:center");
                        HBox.setMargin(editIcon, new Insets(2, 6, 0, 2));
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));

                        setGraphic(btnWrapper);
                    }
                    setText(null);
                }
            };
            return cell;
        };
        maction.setCellFactory(cellFactory);
        try {
            productTable.setItems(Common.getAllProduct());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshData() throws IOException {
        productTable.setItems(Common.getAllProduct());
    }


    private void editProduct() throws IOException {
        selectedProduct = productTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader ();
        loader.setLocation(getClass().getResource("product-edit-modal.fxml"));

        loader.load();

        Product_EditController product_editController = loader.getController();
        product_editController.setData(selectedProduct);
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }

    private void deleteProduct() {
        selectedProduct = productTable.getSelectionModel().getSelectedItem();
        Optional<ButtonType> confirm = Helper.showConfirmationBox("Are you sure you want to delete " +  selectedProduct.getProductName()+ "?");

        if(confirm.isPresent() && confirm.get()== ButtonType.OK){
            System.out.println("====Clicked====");
            try {
                Admin.deleteProduct(selectedProduct.getProductName());
                productTable.setItems(Common.getAllProduct());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void toAddProduct(ActionEvent actionEvent) throws IOException {
        Home_AdminController controller = Home_AdminController.getInstance();
        controller.toAddProduct();
    }
}
