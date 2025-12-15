package controller;

import dao.AppartementDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appartement;

public class AppartementFormController {

    @FXML private TextField txtNumero;
    @FXML private TextField txtSurface;
    @FXML private ComboBox<String> cbStatut;
    @FXML private TextField txtDescription;

    private AppartementDAO appartementDAO;
    private Stage dialogStage;
    private Appartement currentAppartement;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAppartement(Appartement apt) {
        this.currentAppartement = apt;

        txtNumero.setText(apt.getNumero());
        txtSurface.setText(String.valueOf(apt.getSurface()));
        cbStatut.setValue(apt.getStatut());
        txtDescription.setText(apt.getDescription());
    }

    @FXML
    public void initialize() {
        appartementDAO = new AppartementDAO();
        cbStatut.getItems().addAll("LIBRE", "LOUE");
        cbStatut.setValue("LIBRE");
    }

    @FXML
    public void handleSave() {
        String numero = txtNumero.getText();
        String surfaceStr = txtSurface.getText();
        String statut = cbStatut.getValue();
        String desc = txtDescription.getText();

        if (numero.isEmpty() || surfaceStr.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        try {
            double surface = Double.parseDouble(surfaceStr);

            if (currentAppartement == null) {
                Appartement newApt = new Appartement(0, numero, surface, statut, desc);
                appartementDAO.addAppartement(newApt);
            } else {
                currentAppartement.setNumero(numero);
                currentAppartement.setSurface(surface);
                currentAppartement.setStatut(statut);
                currentAppartement.setDescription(desc);
                appartementDAO.updateAppartement(currentAppartement);
            }

            dialogStage.close();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "La surface doit être un nombre !");
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