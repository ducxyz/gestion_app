package dao;

import model.ContratLocation;
import utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ContratDAO {

    public List<ContratLocation> getAllContrats() {
        List<ContratLocation> list = new ArrayList<>();
        // Kỹ thuật JOIN 3 bảng để lấy tên Căn hộ và tên Cư dân
        String sql = "SELECT c.*, a.numero, r.nom, r.prenom " +
                "FROM ContratLocation c " +
                "JOIN Appartement a ON c.id_appartement = a.id_appartement " +
                "JOIN Resident r ON c.id_resident = r.id_resident";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String fullName = rs.getString("nom") + " " + rs.getString("prenom");

                ContratLocation contrat = new ContratLocation(
                        rs.getInt("id_contrat"),
                        rs.getDate("date_debut"),
                        rs.getDate("date_fin"),
                        rs.getDouble("loyer_mensuel"),
                        rs.getString("statut"),
                        rs.getInt("id_appartement"),
                        rs.getInt("id_resident"),
                        rs.getString("numero"), // Lấy số phòng từ bảng Appartement
                        fullName               // Lấy tên từ bảng Resident
                );
                list.add(contrat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void addContrat(ContratLocation contrat) {
        String sql = "INSERT INTO ContratLocation (date_debut, date_fin, loyer_mensuel, statut, id_appartement, id_resident) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, contrat.getDateDebut());
            stmt.setDate(2, contrat.getDateFin());
            stmt.setDouble(3, contrat.getLoyerMensuel());
            stmt.setString(4, contrat.getStatut());
            stmt.setInt(5, contrat.getIdAppartement());
            stmt.setInt(6, contrat.getIdResident());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Hàm Kết thúc hợp đồng: Vừa cập nhật Hợp đồng, vừa giải phóng Căn hộ
    public void terminateContrat(int idContrat, int idAppartement) {
        String sqlUpdateContrat = "UPDATE ContratLocation SET statut = 'TERMINE' WHERE id_contrat = ?";
        String sqlUpdateAppart = "UPDATE Appartement SET statut = 'LIBRE' WHERE id_appartement = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            // Bắt đầu giao dịch (Transaction) để đảm bảo cả 2 lệnh cùng thành công
            conn.setAutoCommit(false);

            // 1. Cập nhật trạng thái Hợp đồng
            try (PreparedStatement stmt1 = conn.prepareStatement(sqlUpdateContrat)) {
                stmt1.setInt(1, idContrat);
                stmt1.executeUpdate();
            }

            // 2. Cập nhật trạng thái Căn hộ về LIBRE
            try (PreparedStatement stmt2 = conn.prepareStatement(sqlUpdateAppart)) {
                stmt2.setInt(1, idAppartement); // Cần biết ID căn hộ để giải phóng
                stmt2.executeUpdate();
            }

            conn.commit(); // Xác nhận lưu
            conn.setAutoCommit(true); // Trả về mặc định

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (Exception ex) {} // Nếu lỗi thì hoàn tác
        }
    }

    // Hàm Xóa hẳn hợp đồng (Dùng khi nhập sai)
    public void deleteContrat(int id) {
        String sql = "DELETE FROM ContratLocation WHERE id_contrat = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy DANH SÁCH hợp đồng đang thuê (EN_COURS) của một cư dân
    public List<ContratLocation> getActiveContractsByResident(int idResident) {
        List<ContratLocation> list = new ArrayList<>();
        String sql = "SELECT c.*, a.numero FROM ContratLocation c " +
                "JOIN Appartement a ON c.id_appartement = a.id_appartement " +
                "WHERE c.id_resident = ? AND c.statut = 'EN_COURS'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idResident);
            ResultSet rs = stmt.executeQuery();

            // Dùng WHILE thay vì IF để lấy tất cả các dòng
            while (rs.next()) {
                ContratLocation c = new ContratLocation();
                c.setIdContrat(rs.getInt("id_contrat"));
                c.setDateDebut(rs.getDate("date_debut"));
                c.setDateFin(rs.getDate("date_fin"));
                c.setLoyerMensuel(rs.getDouble("loyer_mensuel"));
                c.setNumeroAppartement(rs.getString("numero"));

                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }}
