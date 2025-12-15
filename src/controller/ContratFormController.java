package controller;

import dao.AppartementDAO;
import dao.ContratDAO;
import dao.ResidentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Appartement;
import model.ContratLocation;
import model.Resident;

import java.sql.Date;

public class ContratFormController {

    @FXML private ComboBox<Appartement> cbAppartement;
    @FXML private ComboBox<Resident> cbResident;
    @FXML private DatePicker dpStart;
    @FXML private DatePicker dpEnd;
    @FXML private TextField txtLoyer;

    private ContratDAO contratDAO;
    private AppartementDAO appartementDAO;
    private ResidentDAO residentDAO;
    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void initialize() {
        contratDAO = new ContratDAO();
        appartementDAO = new AppartementDAO();
        residentDAO = new ResidentDAO();

        cbAppartement.getItems().addAll(appartementDAO.getAvailableAppartements());

        cbAppartement.setConverter(new StringConverter<Appartement>() {
            @Override
            public String toString(Appartement apt) {
                return (apt != null) ? apt.getNumero() + " (" + apt.getSurface() + "m²)" : "";
            }
            @Override
            public Appartement fromString(String string) { return null; }
        });

        cbResident.getItems().addAll(residentDAO.getAllResidents());

        cbResident.setConverter(new StringConverter<Resident>() {
            @Override
            public String toString(Resident r) {
                return (r != null) ? r.getNom() + " " + r.getPrenom() : "";
            }
            @Override
            public Resident fromString(String string) { return null; }
        });
    }

    @FXML
    public void handleSave() {
        Appartement selectedApt = cbAppartement.getValue();
        Resident selectedRes = cbResident.getValue();

        if (selectedApt == null || selectedRes == null || dpStart.getValue() == null || dpEnd.getValue() == null || txtLoyer.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        try {
            double loyer = Double.parseDouble(txtLoyer.getText());

            ContratLocation contrat = new ContratLocation();
            contrat.setIdAppartement(selectedApt.getIdAppartement());
            contrat.setIdResident(selectedRes.getIdResident());
            contrat.setDateDebut(Date.valueOf(dpStart.getValue()));
            contrat.setDateFin(Date.valueOf(dpEnd.getValue()));
            contrat.setLoyerMensuel(loyer);
            contrat.setStatut("EN_COURS");

            contratDAO.addContrat(contrat);

            appartementDAO.updateStatut(selectedApt.getIdAppartement(), "LOUE");

            showAlert("Succès", "Nouveau contrat créé avec succès !");
            dialogStage.close();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le loyer doit être un nombre valide !");
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