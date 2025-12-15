package dao;

import model.Appartement;
import utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AppartementDAO {

    // Hàm lấy tất cả căn hộ từ database
    public List<Appartement> getAllAppartements() {
        List<Appartement> list = new ArrayList<>();
        String sql = "SELECT * FROM Appartement";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Appartement apt = new Appartement(
                        rs.getInt("id_appartement"),
                        rs.getString("numero"),
                        rs.getDouble("surface"),
                        rs.getString("statut"),
                        rs.getString("description")
                );
                list.add(apt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public void addAppartement(Appartement apt) {
        String sql = "INSERT INTO Appartement (numero, surface, statut, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, apt.getNumero());
            stmt.setDouble(2, apt.getSurface());
            stmt.setString(3, apt.getStatut());
            stmt.setString(4, apt.getDescription());

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAppartement(int id) {
        String sql = "DELETE FROM Appartement WHERE id_appartement = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Dán đoạn này vào cuối file AppartementDAO.java (trước dấu } cuối cùng)

    public void updateAppartement(Appartement apt) {
        String sql = "UPDATE Appartement SET numero = ?, surface = ?, statut = ?, description = ? WHERE id_appartement = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, apt.getNumero());
            stmt.setDouble(2, apt.getSurface());
            stmt.setString(3, apt.getStatut());
            stmt.setString(4, apt.getDescription());
            stmt.setInt(5, apt.getIdAppartement()); // Cái này rất quan trọng để biết sửa dòng nào

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Lấy danh sách căn hộ đang TRỐNG để cho thuê
    public List<Appartement> getAvailableAppartements() {
        List<Appartement> list = new ArrayList<>();
        String sql = "SELECT * FROM Appartement WHERE statut = 'LIBRE'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Appartement(
                        rs.getInt("id_appartement"),
                        rs.getString("numero"),
                        rs.getDouble("surface"),
                        rs.getString("statut"),
                        rs.getString("description")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hàm đổi trạng thái căn hộ (Khi tạo hợp đồng thì đổi thành LOUE, khi xong thì đổi về LIBRE)
    public void updateStatut(int idAppartement, String newStatut) {
        String sql = "UPDATE Appartement SET statut = ? WHERE id_appartement = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatut);
            stmt.setInt(2, idAppartement);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}