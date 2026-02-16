package src.dao;
import src.model.Product;
//import src.model.Cart;
import src.model.Prac;
import src.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PracDAO {

    // ===== Get all products =====
    public List<Prac> getAllCart() {
        List<Prac> list = new ArrayList<>();
        String sql = "SELECT * FROM praccart";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Prac p = new Prac();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setBrandId(rs.getInt("brand_id"));
                p.setSupplierId(rs.getInt("supplier_id"));
                p.setUnitId(rs.getInt("unit_id"));
                p.setCost(rs.getDouble("cost"));
                p.setMarkup(rs.getDouble("markup"));
                p.setPrice(rs.getDouble("price"));
                p.setQuantity(rs.getInt("quantity"));
                p.setDateAdded(rs.getDate("date_added").toLocalDate());
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // ===== Add a new product =====
    public boolean addCart(Prac s) {
        String sql = "INSERT INTO praccart(id, name, category_id, brand_id, supplier_id, unit_id, cost, markup, price, quantity, date_added) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, s.getId());
            ps.setString(2, s.getName());
            ps.setInt(3, s.getCategoryId());
            ps.setInt(4, s.getBrandId());
            ps.setInt(5, s.getSupplierId());
            ps.setInt(6, s.getUnitId());
            ps.setDouble(7, s.getCost());
            ps.setDouble(8, s.getMarkup());
            ps.setDouble(9, s.getPrice());
            ps.setInt(10, s.getQuantity());
            ps.setDate(11, Date.valueOf(s.getDateAdded()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== Update product =====
    public boolean updateCart(Prac p) {
        String sql = "UPDATE praccart SET name=?, category_id=?, brand_id=?, supplier_id=?, unit_id=?, price=?, quantity=?, date_added=? " +
                     "WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setInt(2, p.getCategoryId());
            ps.setInt(3, p.getBrandId());
            ps.setInt(4, p.getSupplierId());
            ps.setInt(5, p.getUnitId());
            ps.setDouble(6, p.getPrice());
            ps.setInt(7, p.getQuantity());
            ps.setDate(8, Date.valueOf(p.getDateAdded()));
            ps.setInt(9, p.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== Delete product =====
    public boolean deleteCart(int id) {
        String sql = "DELETE FROM praccart WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== Get product by ID (optional helper) =====
    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setBrandId(rs.getInt("brand_id"));
                p.setSupplierId(rs.getInt("supplier_id"));
                p.setUnitId(rs.getInt("unit_id"));
                p.setPrice(rs.getDouble("price"));
                p.setQuantity(rs.getInt("quantity"));
                p.setDateAdded(rs.getDate("date_added").toLocalDate());
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
