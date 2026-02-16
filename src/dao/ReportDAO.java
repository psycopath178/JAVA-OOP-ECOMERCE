package src.dao;
import src.model.Report;
import src.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ReportDAO {
    public List<Report> getDailySalesReport(String datechosen) {
        List<Report> list = new ArrayList<>();
        String sql = """
            SELECT
                date_added,
                name AS product_name,
                quantity AS quantity_sold,
                cost as costofproduct,
                price as selling_price,
                cost * quantity AS cost,
                (price - cost) * quantity AS profit,
                price * quantity AS revenue
            FROM sale_items
            WHERE price > 0 AND quantity > 0 AND DATE(date_added) = ?
            ORDER BY revenue DESC;
            """;
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            // Set the date parameter
            ps.setDate(1, java.sql.Date.valueOf(datechosen));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Report report = new Report();
                    report.setTransactionDate(rs.getDate("date_added"));
                    report.setProductName(rs.getString("product_name"));
                    report.setproductcost(rs.getDouble("costofproduct"));
                    report.setsellingprice(rs.getDouble("selling_price"));
                    report.setTotalQuantity(rs.getInt("quantity_sold"));
                    report.setTotalCost(rs.getDouble("cost")); 
                    report.setTotalProfit(rs.getDouble("profit"));
                    report.setTotalRevenue(rs.getDouble("revenue"));
                    
                    list.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Report> getMonthlyReports(int year, int month) {
        List<Report> list = new ArrayList<>();
        String sql = """
            SELECT
                date_added,
                name AS product_name,
                quantity AS quantity_sold,
                cost AS costofproduct,
                price AS selling_price,
                cost * quantity AS cost,
                (price - cost) * quantity AS profit,
                price * quantity AS revenue
            FROM sale_items
            WHERE price > 0
            AND quantity > 0
            AND YEAR(date_added) = ?
            AND MONTH(date_added) = ?
            ORDER BY revenue DESC;
            """;
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Report report = new Report();
                    report.setTransactionDate(rs.getDate("date_added"));
                    report.setProductName(rs.getString("product_name"));
                    report.setproductcost(rs.getDouble("costofproduct"));
                    report.setsellingprice(rs.getDouble("selling_price"));
                    report.setTotalQuantity(rs.getInt("quantity_sold"));
                    report.setTotalCost(rs.getDouble("cost")); 
                    report.setTotalProfit(rs.getDouble("profit"));
                    report.setTotalRevenue(rs.getDouble("revenue"));
                    
                    list.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Report> getYearlyReports(int year) {
        List<Report> list = new ArrayList<>();
        String sql = """
            SELECT
                date_added,
                name AS product_name,
                quantity AS quantity_sold,
                cost AS costofproduct,
                price AS selling_price,
                cost * quantity AS cost,
                (price - cost) * quantity AS profit,
                price * quantity AS revenue
            FROM sale_items
            WHERE price > 0
            AND quantity > 0
            AND YEAR(date_added) = ?
            ORDER BY MONTH(date_added), revenue DESC;
            """;
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Report report = new Report();
                    report.setTransactionDate(rs.getDate("date_added"));
                    report.setProductName(rs.getString("product_name"));
                    report.setproductcost(rs.getDouble("costofproduct"));
                    report.setsellingprice(rs.getDouble("selling_price"));
                    report.setTotalQuantity(rs.getInt("quantity_sold"));
                    report.setTotalCost(rs.getDouble("cost")); 
                    report.setTotalProfit(rs.getDouble("profit"));
                    report.setTotalRevenue(rs.getDouble("revenue"));
                    
                    list.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
