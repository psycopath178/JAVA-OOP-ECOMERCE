package src.dao;

import src.model.Product;
import src.model.Threshold;
import src.db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThresholdDAO {

    // ===== Get all products =====
    public List<Threshold> getAllProducts() {
        List<Threshold> list = new ArrayList<>();
        String sql = "SELECT * FROM threshold";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Threshold p = new Threshold();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("product_name"));
                p.setStockLevel(rs.getInt("stock_level"));
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ===== Add a new product =====
    public boolean addProduct(Threshold p) {
        String sql = "INSERT INTO threshold(product_name, stock_level) " +
                     "VALUES(?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setInt(2, p.getStockLevel());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // ===== Update product =====
    public boolean updateProduct(Threshold p) {
        String sql = "UPDATE threshold SET product_name=?, stock_level= ? " +
                     "WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setInt(2, p.getStockLevel());
            ps.setInt(3, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== Delete product =====
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id=?";

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
                p.setCost(rs.getDouble("cost"));
                p.setMarkup(rs.getDouble("markup"));
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
