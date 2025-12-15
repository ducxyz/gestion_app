package controller;

import dao.CompteDAO;
import dao.ResidentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Compte;
import model.Resident;

public class CompteFormController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cbRole;
    @FXML private ComboBox<String> cbStatut;
    @FXML private ComboBox<Resident> cbResident; // Ô chọn cư dân mới

    private CompteDAO compteDAO;
    private ResidentDAO residentDAO;
    private Stage dialogStage;
    private Compte currentCompte;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCompte(Compte compte) {
        this.currentCompte = compte;
        txtUsername.setText(compte.getNomUtilisateur());
        txtPassword.setText(compte.getMotDePasseHash());
        cbRole.setValue(compte.getRole());
        cbStatut.setValue(compte.getStatut() == 1 ? "Actif" : "Bloqué");

        // Khi sửa thì không cho chọn lại cư dân để tránh lỗi logic phức tạp
        cbResident.setDisable(true);
    }

    @FXML
    public void initialize() {
        compteDAO = new CompteDAO();
        residentDAO = new ResidentDAO();

        cbRole.getItems().addAll("ADMIN", "USER");
        cbRole.setValue("USER");

        cbStatut.getItems().addAll("Actif", "Bloqué");
        cbStatut.setValue("Actif");

        // Load danh sách cư dân chưa có tài khoản vào ComboBox
        cbResident.getItems().addAll(residentDAO.getResidentsWithoutAccount());

        // Hiển thị tên cư dân trong ComboBox
        cbResident.setConverter(new StringConverter<Resident>() {
            @Override
            public String toString(Resident r) {
                return (r != null) ? r.getNom() + " " + r.getPrenom() + " (ID: " + r.getIdResident() + ")" : "";
            }
            @Override
            public Resident fromString(String string) { return null; }
        });
    }

    @FXML
    public void handleSave() {
        String user = txtUsername.getText();
        String pass = txtPassword.getText();
        String role = cbRole.getValue();
        int statut = "Actif".equals(cbStatut.getValue()) ? 1 : 0;
        Resident selectedResident = cbResident.getValue();

        if (user.isEmpty() || pass.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        if (currentCompte == null) {
            Compte newCompte = new Compte(0, user, pass, role, statut);

            // 1. Tạo tài khoản và lấy ID mới
            int newAccountId = compteDAO.addCompte(newCompte);

            // 2. Nếu có chọn cư dân, cập nhật ID tài khoản cho cư dân đó
            if (newAccountId != -1 && selectedResident != null) {
                residentDAO.linkAccount(selectedResident.getIdResident(), newAccountId);
            }
        } else {
            currentCompte.setNomUtilisateur(user);
            currentCompte.setMotDePasseHash(pass);
            currentCompte.setRole(role);
            currentCompte.setStatut(statut);
            compteDAO.updateCompte(currentCompte);
        }
        dialogStage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
