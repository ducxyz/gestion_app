package controller;

import dao.ContratDAO;
import dao.ResidentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.ContratLocation;
import model.Resident;

import java.io.IOException;
import java.util.List;

public class UserController {

    @FXML private Label lblWelcome;
    @FXML private TextField txtPhone;

    @FXML private VBox contractsContainer;

    private ResidentDAO residentDAO = new ResidentDAO();
    private ContratDAO contratDAO = new ContratDAO();
    private Resident currentResident;

    public void setAccountID(int idCompte) {
        this.currentResident = residentDAO.getResidentByAccountId(idCompte);

        if (currentResident != null) {
            lblWelcome.setText("Bienvenue : " + currentResident.getNom() + " " + currentResident.getPrenom());
            txtPhone.setText(currentResident.getTelephone());

            List<ContratLocation> contracts = contratDAO.getActiveContractsByResident(currentResident.getIdResident());

            contractsContainer.getChildren().clear();

            if (contracts != null && !contracts.isEmpty()) {
                for (ContratLocation c : contracts) {
                    createContractCard(c);
                }
            } else {
                Label lblEmpty = new Label("Aucun contrat de location actif.");
                lblEmpty.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic;");
                contractsContainer.getChildren().add(lblEmpty);
            }
        } else {
            lblWelcome.setText("Bienvenue ! (Aucune information de profil trouvée)");
        }
    }

    private void createContractCard(ContratLocation contrat) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-color: #fdfaf6;");

        Label lblAppart = new Label("Appartement : " + contrat.getNumeroAppartement());
        lblAppart.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblAppart.setStyle("-fx-text-fill: #cfa670;");

        Label lblDates = new Label("Durée : " + contrat.getDateDebut() + " au " + contrat.getDateFin());
        lblDates.setStyle("-fx-text-fill: #2d3436;");

        Label lblLoyer = new Label("Loyer mensuel : " + String.format("%.0f", contrat.getLoyerMensuel()) + " €");
        lblLoyer.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblLoyer.setStyle("-fx-text-fill: #1a252f;");

        card.getChildren().addAll(lblAppart, lblDates, lblLoyer);
        contractsContainer.getChildren().add(card);
    }

    @FXML
    public void handleUpdateContact() {
        if (currentResident != null) {
            String newPhone = txtPhone.getText();

            residentDAO.updateContactInfo(currentResident.getIdResident(), newPhone);

            currentResident.setTelephone(newPhone);

            showAlert("Succès", "Votre numéro de téléphone a été mis à jour !");
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Maison de l'amour - Connexion");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}