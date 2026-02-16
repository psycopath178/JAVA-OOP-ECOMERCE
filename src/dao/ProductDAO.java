package src.dao;

import src.model.Product;
import src.db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // ===== Get all products =====
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
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

                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ===== Add a new product =====
    public boolean addProduct(Product p) {
        String sql = "INSERT INTO products(name, category_id, brand_id, supplier_id, unit_id, cost, markup, price, quantity, date_added) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setInt(2, p.getCategoryId());
            ps.setInt(3, p.getBrandId());
            ps.setInt(4, p.getSupplierId());
            ps.setInt(5, p.getUnitId());
            ps.setDouble(6, p.getCost());
            ps.setDouble(7, p.getMarkup());
            ps.setDouble(8, p.getPrice());
            ps.setInt(9, p.getQuantity());
            ps.setDate(10, Date.valueOf(p.getDateAdded()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== Update product =====
    public boolean updateProduct(Product p) {
        String sql = "UPDATE products SET name=?, category_id=?, brand_id=?, supplier_id=?, unit_id=?, cost=?, markup=?, price=?, quantity=?, date_added=? " +
                     "WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setInt(2, p.getCategoryId());
            ps.setInt(3, p.getBrandId());
            ps.setInt(4, p.getSupplierId());
            ps.setInt(5, p.getUnitId());
            ps.setDouble(6, p.getCost());
            ps.setDouble(7, p.getMarkup());
            ps.setDouble(8, p.getPrice());
            ps.setInt(9, p.getQuantity());
            ps.setDate(10, Date.valueOf(p.getDateAdded()));
            ps.setInt(11, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public int getProductQuantity(int productId) {
    String sql = "SELECT quantity FROM products WHERE id = ?";
    System.out.println("DEBUG: Getting quantity for product ID: " + productId); 
    System.out.println("DEBUG: SQL: " + sql);
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, productId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("quantity");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0; // safer default
}
    // ===== Delete product =====
    public boolean deleteProduct(int id) {
    String deleteThreshold = "DELETE FROM threshold WHERE id = ?";
    String deleteProduct = "DELETE FROM products WHERE id = ?";

    try (Connection con = DatabaseConnection.getConnection()) {
        con.setAutoCommit(false);

        try (PreparedStatement ps1 = con.prepareStatement(deleteThreshold);
             PreparedStatement ps2 = con.prepareStatement(deleteProduct)) {

            ps1.setInt(1, id);
            ps1.executeUpdate();

            ps2.setInt(1, id);
            ps2.executeUpdate();

            con.commit();
            return true;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    /*
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
     */
    

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
    public boolean isCategoryUsed(int categoryId) {
    String sql = "SELECT COUNT(*) FROM products WHERE category_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, categoryId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public boolean isBrandUsed(int brandId) {
    String sql = "SELECT COUNT(*) FROM products WHERE brand_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, brandId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public boolean isSupplierUsed(int supplierId) {
    String sql = "SELECT COUNT(*) FROM products WHERE supplier_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, supplierId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
}
