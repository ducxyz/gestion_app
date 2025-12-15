package dao;

import model.Compte;
import utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement; // Cần import thêm cái này
import java.util.ArrayList;
import java.util.List;

public class CompteDAO {

    public Compte checkLogin(String username, String password) {
        String sql = "SELECT * FROM Compte WHERE nom_utilisateur = ? AND mot_de_passe_hash = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Compte(
                        rs.getInt("id_compte"),
                        rs.getString("nom_utilisateur"),
                        rs.getString("mot_de_passe_hash"),
                        rs.getString("role"),
                        rs.getInt("statut")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Compte> getAllComptes() {
        List<Compte> list = new ArrayList<>();
        String sql = "SELECT * FROM Compte";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Compte(
                        rs.getInt("id_compte"),
                        rs.getString("nom_utilisateur"),
                        rs.getString("mot_de_passe_hash"),
                        rs.getString("role"),
                        rs.getInt("statut")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hàm thêm tài khoản trả về ID vừa tạo
    public int addCompte(Compte compte) {
        String sql = "INSERT INTO Compte (nom_utilisateur, mot_de_passe_hash, role, statut) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, compte.getNomUtilisateur());
            stmt.setString(2, compte.getMotDePasseHash());
            stmt.setString(3, compte.getRole());
            stmt.setInt(4, compte.getStatut());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Trả về ID mới
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu lỗi
    }

    public void updateCompte(Compte compte) {
        String sql = "UPDATE Compte SET nom_utilisateur=?, mot_de_passe_hash=?, role=?, statut=? WHERE id_compte=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, compte.getNomUtilisateur());
            stmt.setString(2, compte.getMotDePasseHash());
            stmt.setString(3, compte.getRole());
            stmt.setInt(4, compte.getStatut());
            stmt.setInt(5, compte.getIdCompte());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCompte(int id) {
        String sql = "DELETE FROM Compte WHERE id_compte=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}