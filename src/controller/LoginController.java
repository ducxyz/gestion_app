package controller;

import dao.CompteDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Compte;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    private CompteDAO compteDAO;

    public LoginController() {
        this.compteDAO = new CompteDAO();
    }

    @FXML
    public void handleLogin() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        Compte compte = compteDAO.checkLogin(username, password);

        if (compte != null) {
            if (compte.getStatut() == 0) {
                showAlert("Erreur", "Ce compte a été désactivé !");
                return;
            }

            try {
                if ("ADMIN".equals(compte.getRole())) {
                    Parent root = FXMLLoader.load(getClass().getResource("/view/AdminDashboard.fxml"));
                    Stage stage = (Stage) txtUsername.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Tableau de bord - Admin");
                    stage.centerOnScreen();
                    stage.show();
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserDashboard.fxml"));
                    Parent root = loader.load();

                    UserController userController = loader.getController();
                    userController.setAccountID(compte.getIdCompte());

                    Stage stage = (Stage) txtUsername.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Espace Résident");
                    stage.centerOnScreen();
                    stage.show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            showAlert("Erreur", "Nom d'utilisateur ou mot de passe incorrect !");
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