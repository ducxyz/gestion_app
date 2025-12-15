package controller;

import dao.ResidentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Resident;

import java.sql.Date;
import java.time.LocalDate;

public class ResidentFormController {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtCMND;
    @FXML private TextField txtPhone;
    @FXML private DatePicker dpDate;

    private ResidentDAO residentDAO;
    private Stage dialogStage;
    private Resident currentResident;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setResident(Resident res) {
        this.currentResident = res;
        // Điền dữ liệu cũ vào form
        txtNom.setText(res.getNom());
        txtPrenom.setText(res.getPrenom());
        txtCMND.setText(res.getNumeroIdentite());
        txtPhone.setText(res.getTelephone());

        if (res.getDateNaissance() != null) {
            dpDate.setValue(res.getDateNaissance().toLocalDate());
        }
    }

    @FXML
    public void initialize() {
        residentDAO = new ResidentDAO();
    }

    @FXML
    public void handleSave() {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String cmnd = txtCMND.getText();
        String phone = txtPhone.getText();
        LocalDate localDate = dpDate.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || cmnd.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập Họ, Tên và CMND!");
            return;
        }

        Date sqlDate = (localDate != null) ? Date.valueOf(localDate) : null;

        if (currentResident == null) {
            // Thêm mới (id_compte tạm để 0)
            Resident newRes = new Resident(0, nom, prenom, cmnd, phone, sqlDate, 0);
            residentDAO.addResident(newRes);
        } else {
            // Cập nhật
            currentResident.setNom(nom);
            currentResident.setPrenom(prenom);
            currentResident.setNumeroIdentite(cmnd);
            currentResident.setTelephone(phone);
            currentResident.setDateNaissance(sqlDate);
            residentDAO.updateResident(currentResident);
        }
        dialogStage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
