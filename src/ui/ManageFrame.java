package src.ui;
import src.dao.*;
import src.model.*;
import src.ui.InventoryFrame.LowStockRenderer;

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
public class ManageFrame extends JFrame
{
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
    public ManageFrame()
    {
        setTitle("E-Commerce Product Management");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        model = new DefaultTableModel(new String[]{
                "ID", "Name"}, 0);
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        JPanel tablePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Category", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        tablePanel.add(titleLabel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        model2 = new DefaultTableModel(new String[]{
                "ID", "Name"}, 0);
        table2 = new JTable(model2);
        sorter = new TableRowSorter<>(model2);
        table2.setRowSorter(sorter);

        JPanel tablePanel2 = new JPanel(new BorderLayout());
        JLabel titleLabel2 = new JLabel("Brand", SwingConstants.CENTER);
        titleLabel2.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel2.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        tablePanel2.add(titleLabel2, BorderLayout.NORTH);
        tablePanel2.add(new JScrollPane(table2), BorderLayout.CENTER);
        model3 = new DefaultTableModel(new String[]{
                "ID", "Name"}, 0);
        table3 = new JTable(model3);
        sorter = new TableRowSorter<>(model3);
        table3.setRowSorter(sorter);
        JPanel panel = new JPanel(new BorderLayout());
        cmbCategory = new JComboBox<>();
        cmbBrand = new JComboBox<>();
        cmbSupplier = new JComboBox<>();
        panel.add(cmbCategory);
        panel.add(cmbBrand);
        panel.add(cmbSupplier);
        JPanel tablePanel3 = new JPanel(new BorderLayout());
        JLabel titleLabel3 = new JLabel("Supplier", SwingConstants.CENTER);
        titleLabel3.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel3.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        tablePanel3.add(titleLabel3, BorderLayout.NORTH);
        tablePanel3.add(new JScrollPane(table3), BorderLayout.CENTER);
        CategoryLoad();
        BrandLoad();
        SupplierLoad();
        JPanel tablecontainer = new JPanel(new GridLayout(3,1,0,0));
        tablecontainer.add(tablePanel);
        tablecontainer.add(tablePanel2);
        tablecontainer.add(tablePanel3);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.LIGHT_GRAY);
        titleLabel2.setOpaque(true);
        titleLabel2.setBackground(new Color(220, 255, 220)); // light green
        titleLabel3.setOpaque(true);
        titleLabel3.setBackground(new Color(255, 220, 220));
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnReturn = new JButton("RETURN TO Main");
        topButtonPanel.add(btnReturn, BorderLayout.CENTER);
        topButtonPanel.add(btnAdd, BorderLayout.CENTER);
        topButtonPanel.add(btnUpdate, BorderLayout.CENTER);
        topButtonPanel.add(btnDelete, BorderLayout.CENTER);
        
        txtName = new JTextField(20);
        topButtonPanel.add(txtName);
        add(topButtonPanel, BorderLayout.NORTH);
        add(tablecontainer, BorderLayout.CENTER);
        btnAdd.addActionListener(e-> addchoices());
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    int modelRow = table.convertRowIndexToModel(row);
                    txtName.setText(model.getValueAt(row, 1).toString());
                }
            }
        });
        table2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table2.getSelectedRow();
                if (row >= 0) {
                    int modelRow = table.convertRowIndexToModel(row);
                    txtName.setText(model2.getValueAt(row, 1).toString());
                }
            }
        });
        table3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table3.getSelectedRow();
                if (row >= 0) {
                    int modelRow = table.convertRowIndexToModel(row);
                    txtName.setText(model3.getValueAt(row, 1).toString());
                }
            }
        });
        btnReturn.addActionListener(e->returnmain());
        btnDelete.addActionListener(e->deleteSelectedRow());
        setVisible(true);    
    }
private void returnmain()
{
    dispose();
    new ProductFrame();
}
private void deleteSelectedRow() {
    JTable selectedTable = null;
    DefaultTableModel selectedModel = null;
    String type = null;
    boolean isUsed = false;
    if (table.getSelectedRow() >= 0) {
        selectedTable = table;
        selectedModel = model;
        type = "CATEGORY";
    } 
    if (table2.getSelectedRow() >= 0) {
        selectedTable = table2;
        selectedModel = model2;
        type = "BRAND";
    } 
    if (table3.getSelectedRow() >= 0) {
        selectedTable = table3;
        selectedModel = model3;
        type = "SUPPLIER";
    }
    if (selectedTable == null) {
        JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        return;
    }
    int viewRow = selectedTable.getSelectedRow();
    int modelRow = selectedTable.convertRowIndexToModel(viewRow);
    int id = (int) selectedModel.getValueAt(modelRow, 0);
    if ("CATEGORY".equals(type)) {
        isUsed = productDAO.isCategoryUsed(id);
    } else if ("BRAND".equals(type)) {
        isUsed = productDAO.isBrandUsed(id);
    } else if ("SUPPLIER".equals(type)) {
        isUsed = productDAO.isSupplierUsed(id);
    }
    if (isUsed) {
        JOptionPane.showMessageDialog(
            this,
            "Cannot delete. This " + type.toLowerCase() + " is still linked to products.",
            "Delete blocked",
            JOptionPane.WARNING_MESSAGE
        );
        return;
    }
    if (ConfirmationAction("Delete")) {
        if (productDAO.deleteProduct(id)) {
            selectedModel.removeRow(modelRow);
            JOptionPane.showMessageDialog(this, "Deleted!");
        } else {
            JOptionPane.showMessageDialog(this, "Delete failed!");
        }
    }
}
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
private void CategoryLoad()
{
    model.setRowCount(0);
    List<Category> sale = categoryDAO.getAllCategories();
   // NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
    for (Category p : sale) 
    {
        model.addRow(new Object[]
        {
            p.getId(),
            p.getName()
        });
    }
}
private void BrandLoad()
{
    model2.setRowCount(0);
    List<Brand> sale = brandDAO.getAllBrands();
   // NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
    for (Brand p : sale) 
    {
        model2.addRow(new Object[]
        {
            p.getId(),
            p.getName()
        });
    }
}
private void SupplierLoad()
{
    model3.setRowCount(0);
    List<Supplier> sale = supplierDAO.getAllSuppliers();
   // NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
    for (Supplier p : sale) 
    {
        model3.addRow(new Object[]
        {
            p.getId(),
            p.getName()
        });
    }
}

private void addchoices()
{
    String[] options = {"Category", "Brand", "Supplier"};
    int choice = JOptionPane.showOptionDialog(
            this,
            "Choose what you want to add:",
            "Add Choice",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
    );
    switch (choice) {
        case 0 -> addNewCategory();
        case 1 -> addNewBrand();
        case 2 -> addNewSupplier();
        default -> {            // User closed dialog or pressed cancel
        }
    }
}
private void addNewCategory() {
    String name = JOptionPane.showInputDialog(this, "Enter new category:");
    if(name != null && !name.isBlank()) {
        if(categoryDAO.addCategory(new Category(0, name))) {
            JOptionPane.showMessageDialog(this, "Category added!");
            CategoryLoad();
        }
    }
}

private void addNewBrand() {
    String name = JOptionPane.showInputDialog(this, "Enter new brand:");
    if(name != null && !name.isBlank()) {
        if(brandDAO.addBrand(new Brand(0, name))) {
            JOptionPane.showMessageDialog(this, "Brand added!");
            BrandLoad();
        }
    }
}

private void addNewSupplier() {
    String name = JOptionPane.showInputDialog(this, "Enter new supplier:");
    if(name != null && !name.isBlank()) {
        if(supplierDAO.addSupplier(new Supplier(0, name))) {
            JOptionPane.showMessageDialog(this, "Supplier added!");
            SupplierLoad();
        }
    }
}
// ===== Update Product =====
private void updateProduct() 
{
    int row = table.getSelectedRow();
    if(row >= 0) 
    {
        try 
        {
            Product p = new Product();
            p.setId((int) model.getValueAt(row, 0));
            p.setName(txtName.getText());
            p.setCategoryId(((Category)cmbCategory.getSelectedItem()).getId());
            p.setBrandId(((Brand)cmbBrand.getSelectedItem()).getId());
            p.setSupplierId(((Supplier)cmbSupplier.getSelectedItem()).getId());
            p.setUnitId(((Unit)cmbUnit.getSelectedItem()).getId());
            p.setCost(yourCost);
            p.setMarkup(yourMarkup);
            p.setPrice(Double.parseDouble(txtPrice.getText()));
            p.setQuantity(Integer.parseInt(txtQty.getText()));
            p.setDateAdded(LocalDate.parse(lblDate.getText()));
            if(ConfirmationAction("Update"))
            {
                if(productDAO.updateProduct(p)) 
                {
                    JOptionPane.showMessageDialog(this, "Product updated!");
                    SupplierLoad();
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
}
    // ===== Delete Product =====
private void deleteProduct() 
{
    int row = table.getSelectedRow();
    if(row >= 0) 
    {
        int id = (int) model.getValueAt(row, 0);
        if(ConfirmationAction("Delete"))
        {
            if(productDAO.deleteProduct(id))
            {
                JOptionPane.showMessageDialog(this, "Product deleted!");
                SupplierLoad();
            }
        }
        else 
        {
            JOptionPane.showMessageDialog(this, "Delete failed!");
        }
    }
}
private boolean ConfirmationAction(String Action)
{
    int confirm = JOptionPane.showConfirmDialog(
    this,
    "Are you sure you want to " + Action +" this?",
    "Confirm"+ Action,
    JOptionPane.YES_NO_OPTION
    );
    if (confirm == JOptionPane.YES_OPTION)
        return true;
    else
        return false;
}
}