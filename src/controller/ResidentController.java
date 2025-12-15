package controller;

import dao.ResidentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Resident;

import java.util.Date;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.IOException;

public class ResidentController {

    @FXML
    private TableView<Resident> tableResident;

    @FXML private TableColumn<Resident, Integer> colId;
    @FXML private TableColumn<Resident, String> colNom;
    @FXML private TableColumn<Resident, String> colPrenom;
    @FXML private TableColumn<Resident, String> colCMND;
    @FXML private TableColumn<Resident, String> colPhone;
    @FXML private TableColumn<Resident, Date> colDate;

    private ResidentDAO residentDAO;

    public ResidentController() {
        this.residentDAO = new ResidentDAO();
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idResident"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colCMND.setCellValueFactory(new PropertyValueFactory<>("numeroIdentite"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));

        loadData();
    }

    private void loadData() {
        ObservableList<Resident> list = FXCollections.observableArrayList(residentDAO.getAllResidents());
        tableResident.setItems(list);
    }

    @FXML
    public void handleAdd() {
        showEditDialog(null);
    }

    @FXML
    public void handleEdit() {
        Resident selected = tableResident.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un résident à modifier !");
            return;
        }
        showEditDialog(selected);
    }

    @FXML
    public void handleDelete() {
        Resident selected = tableResident.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un résident à supprimer !");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer le résident : " + selected.getNom() + " " + selected.getPrenom() + " ?");

        if (alert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            residentDAO.deleteResident(selected.getIdResident());
            loadData();
        }
    }

    private void showEditDialog(Resident resident) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ResidentForm.fxml"));
            javafx.scene.Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(resident == null ? "Ajouter un résident" : "Modifier le résident");
            dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialogStage.initOwner(tableResident.getScene().getWindow());
            dialogStage.setScene(new javafx.scene.Scene(page));

            ResidentFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            if (resident != null) {
                controller.setResident(resident);
            }

            dialogStage.showAndWait();
            loadData();

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