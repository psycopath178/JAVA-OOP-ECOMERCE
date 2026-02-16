package src.model;
import java.util.Date;
public class Report {
    private Date transactionDate;
    private String productName;
    private int totalQuantity;
    private double productcost;
    private double sellingprice;
    private double totalCost;
    private double totalRevenue;
    private double totalProfit;
    
    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getproductcost() { return productcost; }
    public void setproductcost(double productcost) { this.productcost = productcost; }

    public double getsellingprice() { return sellingprice; }
    public void setsellingprice(double sellingprice) { this.sellingprice = sellingprice; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public double getTotalProfit() { return totalProfit; }
    public void setTotalProfit(double totalProfit) { this.totalProfit = totalProfit; }

    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
}
