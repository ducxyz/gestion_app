package dao;

import model.Resident;
import utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResidentDAO {

    public List<Resident> getAllResidents() {
        List<Resident> list = new ArrayList<>();
        String sql = "SELECT * FROM Resident";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Resident(
                        rs.getInt("id_resident"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("numero_identite"),
                        rs.getString("telephone"),
                        rs.getDate("date_naissance"),
                        rs.getInt("id_compte")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addResident(Resident resident) {
        String sql = "INSERT INTO Resident (nom, prenom, numero_identite, telephone, date_naissance, id_compte) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resident.getNom());
            stmt.setString(2, resident.getPrenom());
            stmt.setString(3, resident.getNumeroIdentite());
            stmt.setString(4, resident.getTelephone());
            stmt.setDate(5, resident.getDateNaissance());
            stmt.setInt(6, resident.getIdCompte());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateResident(Resident resident) {
        String sql = "UPDATE Resident SET nom=?, prenom=?, numero_identite=?, telephone=?, date_naissance=? WHERE id_resident=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resident.getNom());
            stmt.setString(2, resident.getPrenom());
            stmt.setString(3, resident.getNumeroIdentite());
            stmt.setString(4, resident.getTelephone());
            stmt.setDate(5, resident.getDateNaissance());
            stmt.setInt(6, resident.getIdResident());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteResident(int id) {
        String sql = "DELETE FROM Resident WHERE id_resident=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Resident getResidentByAccountId(int idCompte) {
        String sql = "SELECT * FROM Resident WHERE id_compte = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCompte);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Resident(
                        rs.getInt("id_resident"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("numero_identite"),
                        rs.getString("telephone"),
                        rs.getDate("date_naissance"),
                        rs.getInt("id_compte")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Resident> getResidentsWithoutAccount() {
        List<Resident> list = new ArrayList<>();
        String sql = "SELECT * FROM Resident WHERE id_compte IS NULL OR id_compte = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Resident(
                        rs.getInt("id_resident"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("numero_identite"),
                        rs.getString("telephone"),
                        rs.getDate("date_naissance"),
                        rs.getInt("id_compte")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void linkAccount(int idResident, int idCompte) {
        String sql = "UPDATE Resident SET id_compte = ? WHERE id_resident = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCompte);
            stmt.setInt(2, idResident);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateContactInfo(int idResident, String telephone) {
        String sql = "UPDATE Resident SET telephone = ? WHERE id_resident = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, telephone);
            stmt.setInt(2, idResident);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
