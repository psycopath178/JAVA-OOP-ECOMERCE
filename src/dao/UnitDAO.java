package src.dao;
import src.model.Unit;
import src.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnitDAO {
    public List<Unit> getAllUnit() {
        List<Unit> list = new ArrayList<>();
        String sql = "SELECT * FROM unit";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while(rs.next()) list.add(new Unit(rs.getInt("id"), rs.getString("name")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Unit getUnitById(int id) {
        String sql = "SELECT * FROM unit WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return new Unit(rs.getInt("id"), rs.getString("name"));
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean addUnit(Unit c) {
        String sql = "INSERT INTO unit(name) VALUES(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
