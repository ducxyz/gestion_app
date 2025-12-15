package controller;

import dao.CompteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Compte;
import java.io.IOException;
import java.util.Optional;

public class CompteController {

    @FXML private TableView<Compte> tableCompte;
    @FXML private TableColumn<Compte, Integer> colId;
    @FXML private TableColumn<Compte, String> colUser;
    @FXML private TableColumn<Compte, String> colRole;
    @FXML private TableColumn<Compte, Integer> colStatut;

    private CompteDAO compteDAO;

    public CompteController() {
        this.compteDAO = new CompteDAO();
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idCompte"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("nomUtilisateur"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        loadData();
    }

    private void loadData() {
        ObservableList<Compte> list = FXCollections.observableArrayList(compteDAO.getAllComptes());
        tableCompte.setItems(list);
    }

    @FXML
    public void handleAdd() {
        showEditDialog(null);
    }

    @FXML
    public void handleEdit() {
        Compte selected = tableCompte.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un compte à modifier !");
            return;
        }
        showEditDialog(selected);
    }

    @FXML
    public void handleDelete() {
        Compte selected = tableCompte.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un compte à supprimer !");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer le compte : " + selected.getNomUtilisateur() + " ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            compteDAO.deleteCompte(selected.getIdCompte());
            loadData();
        }
    }

    private void showEditDialog(Compte compte) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CompteForm.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(compte == null ? "Ajouter un compte" : "Modifier le compte");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tableCompte.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            CompteFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            if (compte != null) {
                controller.setCompte(compte);
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
