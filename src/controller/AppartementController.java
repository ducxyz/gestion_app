package controller;

import dao.AppartementDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Appartement;

import java.io.IOException;
import java.util.Optional;

public class AppartementController {

    @FXML private TableView<Appartement> tableAppartement;
    @FXML private TableColumn<Appartement, Integer> colId;
    @FXML private TableColumn<Appartement, String> colNumero;
    @FXML private TableColumn<Appartement, Double> colSurface;
    @FXML private TableColumn<Appartement, String> colStatut;
    @FXML private TableColumn<Appartement, String> colDesc;

    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cbFilterStatut;

    private AppartementDAO appartementDAO;
    private ObservableList<Appartement> masterData = FXCollections.observableArrayList();

    public AppartementController() {
        this.appartementDAO = new AppartementDAO();
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idAppartement"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colSurface.setCellValueFactory(new PropertyValueFactory<>("surface"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        cbFilterStatut.getItems().addAll("TOUT", "LIBRE", "LOUE");
        cbFilterStatut.setValue("TOUT");

        loadData();
    }

    private void loadData() {
        masterData.setAll(appartementDAO.getAllAppartements());

        FilteredList<Appartement> filteredData = new FilteredList<>(masterData, p -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(appartement -> checkFilter(appartement, newValue, cbFilterStatut.getValue()));
        });

        cbFilterStatut.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(appartement -> checkFilter(appartement, txtSearch.getText(), newValue));
        });

        SortedList<Appartement> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableAppartement.comparatorProperty());

        tableAppartement.setItems(sortedData);
    }

    private boolean checkFilter(Appartement apt, String searchText, String statusFilter) {
        if (statusFilter != null && !"TOUT".equals(statusFilter)) {
            if (!apt.getStatut().equals(statusFilter)) {
                return false; //
            }
        }

        if (searchText == null || searchText.isEmpty()) {
            return true; //
        }

        String lowerCaseFilter = searchText.toLowerCase();

        if (apt.getNumero().toLowerCase().contains(lowerCaseFilter)) {
            return true;
        }
        if (apt.getDescription() != null && apt.getDescription().toLowerCase().contains(lowerCaseFilter)) {
            return true;
        }

        return false;
    }

    @FXML
    public void handleAdd() {
        showDialog(null);
    }

    @FXML
    public void handleEdit() {
        Appartement selected = tableAppartement.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un appartement à modifier !");
            return;
        }
        showDialog(selected);
    }

    private void showDialog(Appartement apt) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AppartementForm.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(apt == null ? "Ajouter un appartement" : "Modifier l'appartement");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tableAppartement.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            AppartementFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            if (apt != null) controller.setAppartement(apt);

            dialogStage.showAndWait();
            loadData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDelete() {
        Appartement selected = tableAppartement.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un appartement à supprimer !");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer l'appartement numéro " + selected.getNumero() + " ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            appartementDAO.deleteAppartement(selected.getIdAppartement());
            loadData();
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
