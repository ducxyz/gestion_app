package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class AdminController {

    @FXML
    private BorderPane mainContent;

    private void setCenterView(String fxmlFile) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlFile));
            mainContent.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showAppartements(ActionEvent event) {
        setCenterView("/view/ManageAppartement.fxml");
    }

    @FXML
    public void showComptes(javafx.event.ActionEvent event) {
        setCenterView("/view/ManageCompte.fxml");
    }

    @FXML
    public void showResidents(ActionEvent event) {
        setCenterView("/view/ManageResident.fxml");
    }

    @FXML
    public void showContrats(ActionEvent event) {
        setCenterView("/view/ManageContrat.fxml");
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}