package src.model;

public class Threshold {
    private int id;
  //  private int productId;
    private String name;
    private int StockLevel;

    public Threshold() {}

    public Threshold(int id,String name, int StockLevel) {
        this.id = id;
        this.name = name;
        this.StockLevel = StockLevel;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getStockLevel() { return StockLevel; }
    public void setStockLevel(int StockLevel) { this.StockLevel = StockLevel; }

    @Override
    public String toString() {
        return name;
    }
}
