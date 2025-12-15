package controller;

import dao.ContratDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ContratLocation;
import javafx.scene.control.Alert;
import java.io.IOException;
import java.util.Date;

public class ContratController {

    @FXML private TableView<ContratLocation> tableContrat;
    @FXML private TableColumn<ContratLocation, Integer> colId;
    @FXML private TableColumn<ContratLocation, String> colAppart;
    @FXML private TableColumn<ContratLocation, String> colResident;
    @FXML private TableColumn<ContratLocation, Date> colDebut;
    @FXML private TableColumn<ContratLocation, Date> colFin;
    @FXML private TableColumn<ContratLocation, Double> colLoyer;
    @FXML private TableColumn<ContratLocation, String> colStatut;

    private ContratDAO contratDAO;

    @FXML
    public void initialize() {
        contratDAO = new ContratDAO();

        colId.setCellValueFactory(new PropertyValueFactory<>("idContrat"));
        colAppart.setCellValueFactory(new PropertyValueFactory<>("numeroAppartement"));
        colResident.setCellValueFactory(new PropertyValueFactory<>("nomResident"));
        colDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colLoyer.setCellValueFactory(new PropertyValueFactory<>("loyerMensuel"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        loadData();
    }

    private void loadData() {
        ObservableList<ContratLocation> list = FXCollections.observableArrayList(contratDAO.getAllContrats());
        tableContrat.setItems(list);
    }

    @FXML
    public void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ContratForm.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Nouveau Contrat");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tableContrat.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            ContratFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            loadData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleTerminate() {
        ContratLocation selected = tableContrat.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un contrat à terminer !");
            return;
        }

        if ("TERMINE".equals(selected.getStatut())) {
            showAlert("Information", "Ce contrat est déjà terminé !");
            return;
        }

        contratDAO.terminateContrat(selected.getIdContrat(), selected.getIdAppartement());

        showAlert("Succès", "Contrat terminé. L'appartement est désormais LIBRE !");
        loadData();
    }

    @FXML
    public void handleDelete() {
        ContratLocation selected = tableContrat.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un contrat à supprimer !");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer ce contrat ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                contratDAO.deleteContrat(selected.getIdContrat());
                loadData();
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
