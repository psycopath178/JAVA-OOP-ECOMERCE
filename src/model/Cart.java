package src.model;

import java.time.LocalDate;

public class Cart {
    private int id;
    private String name;
    private int categoryId;
    private int brandId;
    private int supplierId;
    private int UnitId;
    private double price;
    private double cost;
    private double markup;
    private int quantity;
    private LocalDate dateAdded;

    public Cart() {}

    public Cart(int id, String name, int categoryId, int brandId, int supplierId,
                   int UnitId,double cost, double markup, double price, int quantity, LocalDate dateAdded) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.supplierId = supplierId;
        this.UnitId = UnitId;
        this.cost = cost;
        this.markup = markup;
        this.price = price;
        this.quantity = quantity;
        this.dateAdded = dateAdded;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public int getBrandId() { return brandId; }
    public void setBrandId(int brandId) { this.brandId = brandId; }

    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }

    public int getUnitId() { return UnitId; }
    public void setUnitId(int UnitId) { this.UnitId = UnitId; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public double getMarkup() { return markup; }
    public void setMarkup(double markup) { this.markup = markup; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDate getDateAdded() { return dateAdded; }
    public void setDateAdded(LocalDate dateAdded) { this.dateAdded = dateAdded; }

    @Override
    public String toString() {
        return name;
    }
}
