package com.example.shopexpensetracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class Helper {
    public static void showModal(String ModalTitle, String modalMessage) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(ModalTitle);
        dialog.setContentText(modalMessage);
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.showAndWait();
        System.out.println(modalMessage);
    }

    public static void changeCurrentScreen(ActionEvent event, String PageFXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(Helper.class.getResource(PageFXML));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void changeScreenAt(StackPane contentArea, String FXMLFileName) throws IOException{
        Parent fxml =  FXMLLoader.load(Objects.requireNonNull(Helper.class.getResource(FXMLFileName)));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }
    public static Optional<ButtonType> showConfirmationBox(String text){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText(text);
        return alert.showAndWait();
    }
}
