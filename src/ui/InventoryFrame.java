package src.ui;
import src.dao.*;
import src.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
public class InventoryFrame extends JFrame{
    private JTable table;
    private JTable table2;
    private JTable table3;
    private JTable table4;
    private DefaultTableModel model;
    private DefaultTableModel model2;
    private DefaultTableModel model3;
    private DefaultTableModel model4;
    private JTextField txtName, txtPrice, txtQty;
    private JComboBox<Category> cmbCategory;
    private JComboBox<Brand> cmbBrand;
    private JComboBox<Supplier> cmbSupplier;
    private JComboBox<Unit> cmbUnit;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> CategorycmbSortFilter;
    private JComboBox<String> BrandcmbSortFilter;
    private JComboBox<String> SuppliercmbSortFilter;
    private JTextField txtUnit;
    private JLabel lblDate;
    // ===== DAOs =====
    private ProductDAO productDAO = new ProductDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private BrandDAO brandDAO = new BrandDAO();
    private SupplierDAO supplierDAO = new SupplierDAO();
    private CartDAO cartDAO = new CartDAO();
    private InventoryDAO inventoryDAO = new InventoryDAO();
    private SalesDAO salesDAO = new SalesDAO();
    private ThresholdDAO thresholdDAO = new ThresholdDAO();
    private UnitDAO unitDAO = new UnitDAO();
    //
    private double yourCost;
    private double yourMarkup;
    private double price;
    private boolean resettingLimit = false;
    public InventoryFrame()
    {
        setTitle("E-Commerce Product Management");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        cmbCategory = new JComboBox<>();
        cmbBrand = new JComboBox<>();
        cmbSupplier = new JComboBox<>();
        cmbUnit = new JComboBox<>();
        // ===== Table Panel =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Filter by Category:"));
        CategorycmbSortFilter = new JComboBox<>();
        CategorycmbSortFilter.addItem("All");
        // Load categories into filter
        for (Category c : categoryDAO.getAllCategories()) 
            CategorycmbSortFilter.addItem(c.getName());
        topPanel.add(CategorycmbSortFilter);
        //add(topPanel, BorderLayout.NORTH);

        JPanel topPanelRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanelRight.add(new JLabel("Filter by Supplier:"));
        SuppliercmbSortFilter = new JComboBox<>();
        SuppliercmbSortFilter.addItem("All");
        // Load categories into filter
        for (Supplier s : supplierDAO.getAllSuppliers()) 
            SuppliercmbSortFilter.addItem(s.getName());
        topPanelRight.add(SuppliercmbSortFilter);

        JPanel topPanelmiddle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanelmiddle.add(new JLabel("Filter by Brand:"));
        BrandcmbSortFilter = new JComboBox<>();
        BrandcmbSortFilter.addItem("All");
        // Load Brand into filter
        for (Brand b : brandDAO.getAllBrands()) 
            BrandcmbSortFilter.addItem(b.getName());
        topPanelmiddle.add(BrandcmbSortFilter);
        //add(topPanelmiddle, BorderLayout.NORTH);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(new JLabel("Search:"));
        JTextField txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        JPanel topContainer = new JPanel(new GridLayout(1, 4));
        JButton btnReturn = new JButton("Return to Main");
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
topContainer.add(topPanel);
topContainer.add(Box.createHorizontalStrut(10));
topContainer.add(topPanelmiddle);
topContainer.add(Box.createHorizontalStrut(10));
topContainer.add(topPanelRight);
topContainer.add(Box.createHorizontalGlue());
topContainer.add(searchPanel);     // Search bar
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(btnReturn);
        JPanel container = new JPanel(new BorderLayout());
        container.add(topContainer, BorderLayout.NORTH);
        container.add(bottomPanel, BorderLayout.SOUTH);
        add(container, BorderLayout.NORTH);
        model = new DefaultTableModel(new String[]{
                "ID", "Name", "Category", "Brand", "Supplier",
                "Unit","Cost Price", "Mark-Up", "Selling Price", "Quantity", "Date Added"}, 0);
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        JPanel tablePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Incoming", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        tablePanel.add(titleLabel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        model2 = new DefaultTableModel(new String[]{
                "ID", "Name", "Category", "Brand", "Supplier",
                "Unit","Cost Price", "Mark-Up", "Selling Price", "Quantity", "Date Added"}, 0);
        table2 = new JTable(model2);
        sorter = new TableRowSorter<>(model2);
        table2.setRowSorter(sorter);

        JPanel tablePanel2 = new JPanel(new BorderLayout());
        JLabel titleLabel2 = new JLabel("Inventory", SwingConstants.CENTER);
        titleLabel2.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel2.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        tablePanel2.add(titleLabel2, BorderLayout.NORTH);
        tablePanel2.add(new JScrollPane(table2), BorderLayout.CENTER);
        LowStockRenderer renderer = new LowStockRenderer();

        // Apply to ALL columns (entire row highlight)
        for (int i = 0; i < table2.getColumnCount(); i++) {
            table2.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        model3 = new DefaultTableModel(new String[]{
                "ID", "Name", "Category", "Brand", "Supplier",
                "Unit","Cost Price", "Mark-Up", "Selling Price", "Quantity", "Date Added"}, 0);
        table3 = new JTable(model3);
        sorter = new TableRowSorter<>(model3);
        table3.setRowSorter(sorter);

        JPanel tablePanel3 = new JPanel(new BorderLayout());
        JLabel titleLabel3 = new JLabel("Outgoing", SwingConstants.CENTER);
        titleLabel3.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel3.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        tablePanel3.add(titleLabel3, BorderLayout.NORTH);
        tablePanel3.add(new JScrollPane(table3), BorderLayout.CENTER);

        model4 = new DefaultTableModel(
        new String[]{"ID", "Name", "Stock Level", "Limit"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only Limit column editable
            }
        };
        table4 = new JTable(model4);
        
        sorter = new TableRowSorter<>(model4);
        table4.setRowSorter(sorter);
        
        JPanel tablePanel4= new JPanel(new BorderLayout());
        JLabel titleLabel4 = new JLabel("Threshold", SwingConstants.CENTER);
        titleLabel4.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel4.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        tablePanel4.add(titleLabel4, BorderLayout.NORTH);
        tablePanel4.add(new JScrollPane(table4), BorderLayout.CENTER);

        loadProducts();
        outgoingLoad();
        incomingLoad();
        thresholdLoad();
            //BACKGROUND COLOR 
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.LIGHT_GRAY);
        titleLabel2.setOpaque(true);
        titleLabel2.setBackground(new Color(220, 255, 220)); // light green
        titleLabel3.setOpaque(true);
        titleLabel3.setBackground(new Color(255, 220, 220));
        JPanel tablecontainer = new JPanel(new GridLayout(4,1,0,0));
        tablecontainer.add(tablePanel);
        tablecontainer.add(tablePanel2);
        tablecontainer.add(tablePanel3);
        tablecontainer.add(tablePanel4);
        add(tablecontainer, BorderLayout.CENTER);
        model4.addTableModelListener(e -> {
            if (resettingLimit) return;
    if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {

        int row = e.getFirstRow();
        int column = e.getColumn();

        // Only react when Limit column is edited
        if (column == 3) {
            Object value = model4.getValueAt(row, column);

            if (value != null) {
                try {
                    int newLimit = Integer.parseInt(value.toString());
                    updateThresholdFromModel(row, newLimit);
                    resettingLimit = true;
                model4.setValueAt(0, row, 3);
                resettingLimit = false;

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter a valid number");
                            resettingLimit = true;
                model4.setValueAt(0, row, 3);
                resettingLimit = false;

                }
            }
        }
    }
});
        btnReturn.addActionListener(e->returnmain()); 
        CategorycmbSortFilter.addActionListener(e -> applyFilters());
        BrandcmbSortFilter.addActionListener(e -> applyFilters());
        SuppliercmbSortFilter.addActionListener(e -> applyFilters());
        loadDropdowns();
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() 
        {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { search(); }
            private void search() 
            {
                String text = txtSearch.getText().trim();
                List<RowFilter<Object, Object>> filters = new java.util.ArrayList<>();
                if (!text.isEmpty())
                    filters.add(RowFilter.orFilter(List.of(
                        RowFilter.regexFilter("(?i)" +   text, 1),
                        RowFilter.regexFilter("(?i)" +   text, 2),
                        RowFilter.regexFilter("(?i)" +   text, 3),
                        RowFilter.regexFilter("(?i)" +   text, 4)
                    )));
                String cat = CategorycmbSortFilter.getSelectedItem().toString();
                String brand = BrandcmbSortFilter.getSelectedItem().toString();
                String supplier = SuppliercmbSortFilter.getSelectedItem().toString();
                if (!cat.equals("All"))
                    filters.add(RowFilter.regexFilter("^" + cat + "$", 2));
                if (!brand.equals("All"))
                    filters.add(RowFilter.regexFilter("^" + brand + "$", 3));
                if (!supplier.equals("All"))
                    filters.add(RowFilter.regexFilter("^" + supplier + "$", 4));
                sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
            }
        });
        setVisible(true);
    }
private void applyFilters() 
{
    List<RowFilter<Object, Object>> filters = new java.util.ArrayList<>();
    String cat = CategorycmbSortFilter.getSelectedItem().toString();
    String brand = BrandcmbSortFilter.getSelectedItem().toString();
    String supplier = SuppliercmbSortFilter.getSelectedItem().toString();
    if (!cat.equals("All"))
        filters.add(RowFilter.regexFilter("^" + cat + "$", 2));
    if (!brand.equals("All"))
        filters.add(RowFilter.regexFilter("^" + brand + "$", 3));
    if (!supplier.equals("All"))
        filters.add(RowFilter.regexFilter("^" + supplier + "$", 4));
    sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
}
private void returnmain()
{
    dispose();
    new ProductFrame();
}
private Map<Integer, Integer> getThresholdMap() {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < model4.getRowCount(); i++) {
        int id = (int) model4.getValueAt(i, 0);
        int stockLevel = (int) model4.getValueAt(i, 2);
        map.put(id, stockLevel);
    }
    return map;
}
class LowStockRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        // Reset colors first
        if (isSelected) {
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
        } else {
            c.setBackground(Color.WHITE);
            c.setForeground(Color.BLACK);
        }
        if (c instanceof JComponent) {
            ((JComponent) c).setToolTipText(null);
        }
        int modelRow = table.convertRowIndexToModel(row);

        int productId = (int) model2.getValueAt(modelRow, 0);
        int quantity = (int) model2.getValueAt(modelRow, 9);

        Integer threshold = getThresholdMap().get(productId);

        if (threshold != null && quantity <= threshold) {
            if (!isSelected) {
                c.setBackground(new Color(255, 180, 180)); // light red
            }
            if (c instanceof JComponent) {
                ((JComponent) c).setToolTipText(
                    "Low stock warning (Threshold: " + threshold + ")"
                );
            }
        }

        return c;
    }
}
private void updateThresholdFromModel(int modelRow, int newLimit) {

    try {
        Threshold p = new Threshold();
        p.setId((int) model4.getValueAt(modelRow, 0));
        p.setName(model4.getValueAt(modelRow, 1).toString());
        p.setStockLevel(newLimit); // this is the LIMIT value

        if (ConfirmationAction("Update")) {

            if (thresholdDAO.updateProduct(p)) {

                JOptionPane.showMessageDialog(this, "Threshold updated!");

                // ✅ Update Stock Level column (index 2)
                model4.setValueAt(newLimit, modelRow, 2);

            } else {
                JOptionPane.showMessageDialog(this, "Update failed!");
            }
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}

    // ===== Load Products =====
private void thresholdLoad() 
{
    model4.setRowCount(0);
    List<Threshold> sale = thresholdDAO.getAllProducts();
   // NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
    for (Threshold p : sale) 
    {
        model4.addRow(new Object[]
        {
            p.getId(),
            p.getName(),
            p.getStockLevel(),
            0
        });
    }
}
private void loadProducts() 
{
    model2.setRowCount(0);
    List<Product> sale = productDAO.getAllProducts();
   // NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
    for (Product p : sale) 
    {
        Category cat = categoryDAO.getCategoryById(p.getCategoryId());
        Brand brand = brandDAO.getBrandById(p.getBrandId());
        Supplier sup = supplierDAO.getSupplierById(p.getSupplierId());
        Unit unit = unitDAO.getUnitById(p.getUnitId());
        model2.addRow(new Object[]
        {
            p.getId(),
            p.getName(),
            cat.getName(),
            brand.getName(),
            sup.getName(),
            unit.getName(),
            p.getCost(),
            p.getMarkup(),
            p.getPrice(),
            p.getQuantity(),
            p.getDateAdded()
        });
    }
}

private void incomingLoad() 
{
    model.setRowCount(0);
    List<Inventory> sale = inventoryDAO.getAllInventory();
   // NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
    for (Inventory p : sale) 
    {
        Category cat = categoryDAO.getCategoryById(p.getCategoryId());
        Brand brand = brandDAO.getBrandById(p.getBrandId());
        Supplier sup = supplierDAO.getSupplierById(p.getSupplierId());
        Unit unit = unitDAO.getUnitById(p.getUnitId());
        model.addRow(new Object[]
        {
            p.getId(),
            p.getName(),
            cat.getName(),
            brand.getName(),
            sup.getName(),
            unit.getName(),
            p.getCost(),
            p.getMarkup(),
            p.getPrice(),
            p.getQuantity(),
            p.getDateAdded()
        });
    }
}
private void outgoingLoad() 
{
    model3.setRowCount(0);
    List<Sales> sale = salesDAO.getAllSales();
   // NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
    for (Sales p : sale) 
    {
        Category cat = categoryDAO.getCategoryById(p.getCategoryId());
        Brand brand = brandDAO.getBrandById(p.getBrandId());
        Supplier sup = supplierDAO.getSupplierById(p.getSupplierId());
        Unit unit = unitDAO.getUnitById(p.getUnitId());
        model3.addRow(new Object[]
        {
            p.getId(),
            p.getName(),
            cat.getName(),
            brand.getName(),
            sup.getName(),
            unit.getName(),
            p.getCost(),
            p.getMarkup(),
            p.getPrice(),
            p.getQuantity(),
            p.getDateAdded()
        });
    }
}
    // ===== Load Dropdowns =====
private void loadDropdowns() 
{
    cmbCategory.removeAllItems();
    for (Category c : categoryDAO.getAllCategories()) cmbCategory.addItem(c);
    cmbBrand.removeAllItems();
    for (Brand b : brandDAO.getAllBrands()) cmbBrand.addItem(b);
    cmbSupplier.removeAllItems();
    for (Supplier s : supplierDAO.getAllSuppliers()) cmbSupplier.addItem(s);
}
private boolean ConfirmationAction(String Action)
{
    int confirm = JOptionPane.showConfirmDialog(
    this,
    "Are you sure you want to " + Action +" this product?",
    "Confirm"+ Action,
    JOptionPane.YES_NO_OPTION
    );
    if (confirm == JOptionPane.YES_OPTION)
        return true;
    else
        return false;
}
}
