package src.ui;
import src.dao.*;
import src.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
public class SalesFrame extends JFrame {
    // ===== Components =====
    private JTable table, table2;
    private DefaultTableModel model, model2;
    private JTextField txtName, txtQty, txtBrand, txtSupplier,txtCategory, txtSelling, txtUnit;
    private JComboBox<Category> cmbCategory;
    private JComboBox<Brand> cmbBrand;
    private JComboBox<Supplier> cmbSupplier;
    private JComboBox<Unit> cmbUnit;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> CategorycmbSortFilter;
    private JComboBox<String> BrandcmbSortFilter;
    private JComboBox<String> SuppliercmbSortFilter;
    private JLabel lblDate;
    // ===== DAOs =====
    //private ProductDAO productDAO = new ProductDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private BrandDAO brandDAO = new BrandDAO();
    private SupplierDAO supplierDAO = new SupplierDAO();
    private UnitDAO unitDAO = new UnitDAO();
    private CartDAO cartDAO = new CartDAO();
    private SalesDAO salesDAO = new SalesDAO();
    private InventoryDAO inventoryDAO = new InventoryDAO();
    private ProductDAO productDAO = new ProductDAO();
    private PracDAO pracDAO = new PracDAO();
    private double yourCost;
    private double yourMarkup;
    public SalesFrame() {
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

        model2 = new DefaultTableModel(new String[]{
                "ID", "Name", "Category", "Brand", "Supplier",
                "Unit","Cost Price", "Mark-Up", "Selling Price", "Quantity", "Date Added"}, 0);
        table2 = new JTable(model2);
        sorter = new TableRowSorter<>(model2);
        table2.setRowSorter(sorter);
        loadProducts();
       // loadimagenary();
        add(new JScrollPane(table), BorderLayout.CENTER);
        // ===== Form Panel =====
        JPanel form = new JPanel(new GridLayout(6, 4, 8, 5));
        // Row 1
        form.add(new JLabel("Name:"));
        txtName = new JTextField();
        form.add(txtName);
        form.add(new JLabel("Category:"));
        txtCategory = new JTextField();
        form.add(txtCategory);
        // Row 2
        form.add(new JLabel("Brand:"));
        txtBrand = new JTextField();
        form.add(txtBrand);
        form.add(new JLabel("Supplier:"));
        txtSupplier = new JTextField();
        form.add(txtSupplier);
        // Row 3
        form.add(new JLabel("Unit:"));
        txtUnit = new JTextField();
        form.add(txtUnit);
        form.add(new JLabel("Selling Price:"));
        txtSelling = new JTextField();
        form.add(txtSelling);
        // Row 4
        form.add(new JLabel("Quantity:"));
        txtQty = new JTextField();
        form.add(txtQty);
        form.add(new JLabel("Date Added:"));
        lblDate = new JLabel(LocalDate.now().toString());
        form.add(lblDate);
        // Row 5 - Buttons
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnSold = new JButton("SOLD");
        
        form.add(btnUpdate);
        form.add(btnDelete);
        form.add(btnSold);
        add(form, BorderLayout.SOUTH);
        // ===== Load Dropdowns =====
        loadDropdowns();
        // ===== Button Actions =====
        btnReturn.addActionListener(e->returnmain());    
        btnUpdate.addActionListener(e -> updateCart());
        btnDelete.addActionListener(e -> deleteCart(0));
        btnSold.addActionListener(e -> SoldProduct());
        CategorycmbSortFilter.addActionListener(e -> applyFilters());
        BrandcmbSortFilter.addActionListener(e -> applyFilters());
        SuppliercmbSortFilter.addActionListener(e -> applyFilters());
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
        txtName.setEnabled(false);
        txtUnit.setEnabled(false);
        txtBrand.setEditable(false);
        txtCategory.setEditable(false);
        txtSupplier.setEditable(false);
        txtSelling.setEditable(false);
        // Make combo boxes non-editable
        cmbCategory.setEnabled(false);
        cmbBrand.setEnabled(false);
        cmbSupplier.setEnabled(false);

        // ===== Table Click =====
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) 
                {
                    int modelRow = table.convertRowIndexToModel(row);
                    txtName.setText(model.getValueAt(row, 1).toString());
                    txtCategory.setText(model.getValueAt(row, 2).toString());
                    txtBrand.setText(model.getValueAt(row, 3).toString());
                    txtSupplier.setText(model.getValueAt(row, 4).toString());
                    txtUnit.setText(model.getValueAt(row, 5).toString());
                    String costStr = model.getValueAt(modelRow, 6).toString().replace("₱", "").replace(",", "").trim();
                    String markupStr = model.getValueAt(modelRow, 7).toString().replace("₱", "").replace(",", "").trim();
                    yourCost = Double.parseDouble(costStr);
                    yourMarkup = Double.parseDouble(markupStr);
                    txtSelling.setText(model.getValueAt(row, 8).toString());
                    txtQty.setText(model.getValueAt(row, 9).toString());
                    lblDate.setText(model.getValueAt(row, 10).toString());
                }
            }
        });
        setVisible(true);
    }
    // ===== FILTERS =====
private void returnmain()
{
    dispose();
    new ProductFrame();
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
    // ===== Load Products =====
private void loadProducts() 
{
    model.setRowCount(0);
    List<Prac> sale = pracDAO.getAllCart();
   // NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
    for (Prac p : sale) 
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
private void loadimagenary() 
{
    model2.setRowCount(0);
    List<Product> products = productDAO.getAllProducts();
    //NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
    for (Product p : products) 
    {
        Category cat = categoryDAO.getCategoryById(p.getCategoryId());
        Brand brand = brandDAO.getBrandById(p.getBrandId());
        Supplier sup = supplierDAO.getSupplierById(p.getSupplierId());
        Unit unit = unitDAO.getUnitById(p.getUnitId());
        model2.addRow(new Object[]
        {
            p.getId(),
            p.getName(),
            cat,
            brand,
            sup,
            unit,
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
    cmbUnit.removeAllItems();   
    for (Unit u : unitDAO.getAllUnit()) cmbUnit.addItem(u);
}
// ===== Add Product =====
private void SoldProduct() 
{
    try 
    {
        Sales p = new Sales();
        Product i = new Product();
        p.setName(txtName.getText());
        p.setCategoryId(((Category)cmbCategory.getSelectedItem()).getId());
        p.setBrandId(((Brand)cmbBrand.getSelectedItem()).getId());
        p.setSupplierId(((Supplier)cmbSupplier.getSelectedItem()).getId());
        p.setUnitId(((Unit)cmbUnit.getSelectedItem()).getId());
        p.setCost(yourCost);
        p.setMarkup(yourMarkup);
        p.setPrice(Double.parseDouble(txtSelling.getText()));
        p.setQuantity(Integer.parseInt(txtQty.getText()));
        p.setDateAdded(LocalDate.now());
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) return;
        int modelRow = table.convertRowIndexToModel(viewRow);
        int productId = (int) model.getValueAt(modelRow, 0);
        int currentQty = productDAO.getProductQuantity(productId);
        int soldQty = Integer.parseInt(txtQty.getText());
        System.out.println("Product ID: " + productId);
        System.out.println("Current Qty from DB: " + currentQty);
        System.out.println("Sold Qty requested: " + soldQty);
        System.out.println("Sold Qty > Current Qty? " + (soldQty > currentQty));
        if (soldQty > currentQty) {
            JOptionPane.showMessageDialog(this, "Insufficient stock!");
            return;
        }
        i.setId((int) model.getValueAt(modelRow, 0));
        i.setName(txtName.getText());
        i.setCategoryId(((Category)cmbCategory.getSelectedItem()).getId());
        i.setBrandId(((Brand)cmbBrand.getSelectedItem()).getId());
        i.setSupplierId(((Supplier)cmbSupplier.getSelectedItem()).getId());
        i.setUnitId(((Unit)cmbUnit.getSelectedItem()).getId());
        i.setCost(yourCost);
        i.setMarkup(yourMarkup);
        i.setPrice(Double.parseDouble(txtSelling.getText()));
        i.setQuantity(currentQty - soldQty);
        i.setDateAdded(LocalDate.parse(lblDate.getText()));
        if(ConfirmationAction("Sold"))
        {
           // deleteCart(1);
            delete(viewRow);
            if(salesDAO.addSales(p))
            {
                productDAO.updateProduct(i);
                JOptionPane.showMessageDialog(this, "Product Sold!");
                loadProducts();
            }
        }
        else 
        {
            JOptionPane.showMessageDialog(this, "Sold failed!");
        }
    } catch (Exception ex) 
    {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}
// ===== Update Product =====
private void updateCart() 
{
    int row = table.getSelectedRow();
    if(row >= 0) 
    {
        try 
        {
            Prac p = new Prac();
            p.setId((int) model.getValueAt(row, 0));
            p.setName(txtName.getText());
            p.setCategoryId(((Category)cmbCategory.getSelectedItem()).getId());
            p.setBrandId(((Brand)cmbBrand.getSelectedItem()).getId());
            p.setSupplierId(((Supplier)cmbSupplier.getSelectedItem()).getId());
            p.setUnitId(((Unit)cmbUnit.getSelectedItem()).getId());
            p.setPrice(Double.parseDouble(txtSelling.getText()));
            p.setQuantity(Integer.parseInt(txtQty.getText()));
            p.setDateAdded(LocalDate.parse(lblDate.getText()));
            if(ConfirmationAction("Update"))
            {
                if(pracDAO.updateCart(p)) 
                {
                    JOptionPane.showMessageDialog(this, "Product updated!");
                    loadProducts();
                }
            }
            else 
            {
                JOptionPane.showMessageDialog(this, "Update failed!");
            }
            
        } catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
    // ===== Delete Product =====
private void delete(int row)
{
    if(row >= 0) 
    {
        int id = (int) model.getValueAt(row, 0);
        pracDAO.deleteCart(id);
    }
}
private void deleteCart(int signal) 
{
    int row = table.getSelectedRow();
    if(row >= 0) 
    {
        int id = (int) model.getValueAt(row, 0);
        if(signal == 1)
        {
            cartDAO.deleteCart(id);
            return;
        }
        if(ConfirmationAction("Delete"))
        {
            if(cartDAO.deleteCart(id))
            {
                JOptionPane.showMessageDialog(this, "Product deleted from Cart!");
                loadProducts();
            }
        }
        else 
        {
            JOptionPane.showMessageDialog(this, "Delete failed!");
        }
    }
}

// ===== Add New Category / Brand / Supplier =====
private void addNewCategory() {
    String name = JOptionPane.showInputDialog(this, "Enter new category:");
    if(name != null && !name.isBlank()) {
        if(categoryDAO.addCategory(new Category(0, name))) {
            JOptionPane.showMessageDialog(this, "Category added!");
            loadDropdowns();
        }
    }
}

private void addNewBrand() {
    String name = JOptionPane.showInputDialog(this, "Enter new brand:");
    if(name != null && !name.isBlank()) {
        if(brandDAO.addBrand(new Brand(0, name))) {
            JOptionPane.showMessageDialog(this, "Brand added!");
            loadDropdowns();
        }
    }
}

private void addNewSupplier() {
    String name = JOptionPane.showInputDialog(this, "Enter new supplier:");
    if(name != null && !name.isBlank()) {
        if(supplierDAO.addSupplier(new Supplier(0, name))) {
            JOptionPane.showMessageDialog(this, "Supplier added!");
            loadDropdowns();
        }
    }
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
