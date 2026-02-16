package src.ui;
import src.dao.*;
import src.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
public class HistoryFrame extends JFrame {
    private JTable table;
    private JTable table2;
    private DefaultTableModel model;
    private DefaultTableModel model2;
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
    private CartDAO cartDAO = new CartDAO();
    private SalesDAO salesDAO = new SalesDAO();
    private InventoryDAO inventoryDAO = new InventoryDAO();
    private HistoryDAO historyDAO = new HistoryDAO();
    private ReportDAO reportDAO = new ReportDAO();
    private ProductDAO productDAO = new ProductDAO();
    private UnitDAO unitDAO = new UnitDAO();
    private double yourCost;
    private double yourMarkup;
    public HistoryFrame()
    {
        setTitle("SALES HISTORY");
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

        loadProducts();
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadDropdowns();
        CategorycmbSortFilter.addActionListener(e -> applyFilters());
        BrandcmbSortFilter.addActionListener(e -> applyFilters());
        SuppliercmbSortFilter.addActionListener(e -> applyFilters());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Create buttons with preferred size
        JButton btnyear= new JButton("Yearly Report");
        btnyear.setPreferredSize(new Dimension(150, 35));

        JButton btnmonth = new JButton("Monthly Report");
        btnmonth.setPreferredSize(new Dimension(150, 35));

        JButton btndaily = new JButton("Daily Report");
        btndaily.setPreferredSize(new Dimension(150, 35));

        JButton btnProduct = new JButton("Product Report");
        btnProduct.setPreferredSize(new Dimension(150, 35));

        // Add buttons to panel
        buttonPanel.add(btnProduct);
        buttonPanel.add(btndaily);
        buttonPanel.add(btnmonth);
        buttonPanel.add(btnyear);
        btnReturn.addActionListener(e->returnmain()); 
        btndaily.addActionListener(e -> pracday());
        btnmonth.addActionListener(e -> monthprac());
        btnyear.addActionListener(e->yearprac());
        btnProduct.addActionListener(e-> ProductReport());
        // Add panel to frame
        add(buttonPanel, BorderLayout.SOUTH); 
        setVisible(true);
    }
private void practice()
{
    dispose();
    new ProductFrame();
}
private void returnmain()
{
    dispose();
    new ProductFrame();
}
private void ProductReport()
{
    JFrame reportframe = new JFrame("Product Report");
    reportframe.setSize(800,400);
    reportframe.setLocationRelativeTo(this);
    reportframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    reportframe.setLayout(new BorderLayout());
    DefaultTableModel reportmodel = new DefaultTableModel(new String[]{
                "ID", "Name", "Category", "Brand", "Supplier",
                "Unit", "Cost Price", "Mark-Up","Selling Price", "Quantity", "Date Added"}, 0);
    JTable reporttable = new JTable(reportmodel);
    List<Product> report = productDAO.getAllProducts();
    for (Product p : report) 
    {
        Category cat = categoryDAO.getCategoryById(p.getCategoryId());
        Brand brand = brandDAO.getBrandById(p.getBrandId());
        Supplier sup = supplierDAO.getSupplierById(p.getSupplierId());
        Unit unit = unitDAO.getUnitById(p.getUnitId());
        reportmodel.addRow(new Object[]
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
    reportframe.add(new JScrollPane(reporttable), BorderLayout.CENTER);
    JPanel btnpanel = new JPanel();
    JButton btnclose = new JButton("Close");
    btnclose.addActionListener(e ->reportframe.dispose());
    JButton btnexport = new JButton("Export to CSV");
    btnexport.addActionListener(e -> exportProduct(report));
    btnpanel.add(btnclose);
    btnpanel.add(btnexport);
    reportframe.add(btnpanel, BorderLayout.SOUTH);
    reportframe.setVisible(true);
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
private void yearprac()
{
    JTextField txtyear = new JTextField(4);
    JButton btnGenerate = new JButton("Generate");
    JPanel panel = new JPanel(new GridLayout(2, 2, 15, 10));
    panel.add(new JLabel("Enter Year (YYYY):"));
    panel.add(txtyear);
    panel.add(new JLabel()); // spacer
    panel.add(btnGenerate);
    JDialog dialog = new JDialog(this, "Yearly Report", true);
    dialog.setContentPane(panel);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    btnGenerate.addActionListener(e->
        {
            String year = txtyear.getText().trim();
            String regexyear = "^\\d{4}$";
            if (!year.matches(regexyear)) 
            {
                JOptionPane.showMessageDialog(dialog,
                    "Invalid format!\nPlease enter year as YYYY.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            int y = Integer.parseInt(year);
            if(y <1900 || y>2100)
            {
                JOptionPane.showMessageDialog(dialog,
                "Invalid date!\nPlease enter a real calendar date.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(dialog, "You entered: " + Integer.parseInt(year));
            YearlyReports(y);
            dialog.dispose();
        }
    );
    dialog.setVisible(true);
}
private void YearlyReports(int year)
{
    JFrame reportframe = new JFrame("Yearly Sales Report");
    reportframe.setSize(800,400);
    reportframe.setLocationRelativeTo(this);
    reportframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    reportframe.setLayout(new BorderLayout());
    DefaultTableModel reportmodel = new DefaultTableModel(new String[]{
        "Transaction Data", "Top Selling Product", "Quantity Sold", "Cost of Product",
        "Selling Price","Sales of Product", "Total Profit",
        "Total Revenue"}, 0);
    JTable reporttable = new JTable(reportmodel);
    List<Report> report = reportDAO.getYearlyReports(year);
    for(Report r : report)
    {
        reportmodel.addRow(new Object[]
            {
                r.getTransactionDate(),
                r.getProductName(),
                r.getTotalQuantity(),
                String.format("₱%.2f", r.getproductcost()),
                String.format("₱%.2f", r.getsellingprice()),
                String.format("₱%.2f", r.getTotalCost()),
                String.format("₱%.2f", r.getTotalProfit()),
                String.format("₱%.2f", r.getTotalRevenue())
            }
        );
    }
    reportframe.add(new JScrollPane(reporttable), BorderLayout.CENTER);
    JPanel btnpanel = new JPanel();
    JButton btnclose = new JButton("Close");
    btnclose.addActionListener(e ->reportframe.dispose());
    JButton btnexport = new JButton("Export to CSV");
    btnexport.addActionListener(e -> exportReportToCSV(report));
    btnpanel.add(btnclose);
    btnpanel.add(btnexport);
    reportframe.add(btnpanel, BorderLayout.SOUTH);
    reportframe.setVisible(true);
}
private void monthprac()
{
    JTextField txtmonth = new JTextField(2);
    JTextField txtyear = new JTextField(4);
    JButton btnGenerate = new JButton("Generate");
    JPanel panel = new JPanel(new GridLayout(3, 2, 15, 10));
    panel.add(new JLabel("Enter Year (YYYY):"));
    panel.add(txtyear);
    panel.add(new JLabel("Enter Month (MM):"));
    panel.add(txtmonth);
    panel.add(new JLabel()); // spacer
    panel.add(btnGenerate);
    JDialog dialog = new JDialog(this, "Monthly Report", true);
    dialog.setContentPane(panel);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    btnGenerate.addActionListener(e->
        {
            String month = txtmonth.getText().trim();
            String year = txtyear.getText().trim();
            String regexmonth = "^(0[1-9]|1[0-2])$";
            String regexyear = "^\\d{4}$";
            if (!month.matches(regexmonth) || !year.matches(regexyear)) 
            {
                JOptionPane.showMessageDialog(dialog,
                    "Invalid format!\nPlease enter year as YYYY and month as MM.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            try
            {
                LocalDate.parse(year + "-" + month + "-01");
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(dialog,
                "Invalid date!\nPlease enter a real calendar date.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(dialog, "You entered: " + Integer.parseInt(year) +"-"+ Integer.parseInt(month));
            MonthlyReports(Integer.parseInt(year), Integer.parseInt(month));
            dialog.dispose();
        }
    );
    dialog.setVisible(true);
}
private void MonthlyReports(int year, int month)
{
    JFrame reportframe = new JFrame("Monthly Sales Report");
    reportframe.setSize(800,400);
    reportframe.setLocationRelativeTo(this);
    reportframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    reportframe.setLayout(new BorderLayout());
    DefaultTableModel reportmodel = new DefaultTableModel(new String[]{
        "Transaction Data", "Top Selling Product", "Quantity Sold", "Cost of Product",
        "Selling Price","Sales of Product", "Total Profit",
        "Total Revenue"}, 0);
    JTable reporttable = new JTable(reportmodel);
    List<Report> report = reportDAO.getMonthlyReports(year, month);
    for(Report r : report)
    {
        reportmodel.addRow(new Object[]
            {
                r.getTransactionDate(),
                r.getProductName(),
                r.getTotalQuantity(),
                String.format("₱%.2f", r.getproductcost()),
                String.format("₱%.2f", r.getsellingprice()),
                String.format("₱%.2f", r.getTotalCost()),
                String.format("₱%.2f", r.getTotalProfit()),
                String.format("₱%.2f", r.getTotalRevenue())
            }
        );
    }
    reportframe.add(new JScrollPane(reporttable), BorderLayout.CENTER);
    JPanel btnpanel = new JPanel();
    JButton btnclose = new JButton("Close");
    btnclose.addActionListener(e ->reportframe.dispose());
    JButton btnexport = new JButton("Export to CSV");
    btnexport.addActionListener(e -> exportReportToCSV(report));
    btnpanel.add(btnclose);
    btnpanel.add(btnexport);
    reportframe.add(btnpanel, BorderLayout.SOUTH);
    reportframe.setVisible(true);
}
private void pracday()
{
    JTextField txtDate = new JTextField(10);
    JButton btnGenerate = new JButton("Generate");
    JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
    panel.add(new JLabel("Enter Date (YYYY-MM-DD):"));
    panel.add(txtDate);
    panel.add(new JLabel()); // spacer
    panel.add(btnGenerate);
    JDialog dialog = new JDialog(this, "Daily Report", true);
    dialog.setContentPane(panel);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    btnGenerate.addActionListener(e -> {
        String date = txtDate.getText().trim();
        String regex = "^\\d{4}-\\d{2}-\\d{2}$";
        if (!date.matches(regex)) {
            JOptionPane.showMessageDialog(dialog,
                "Invalid format!\nPlease enter date as YYYY-MM-DD.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            LocalDate.parse(date); 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog,
                "Invalid date!\nPlease enter a real calendar date.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(dialog, "You entered: " + date);
        DayReport(date);
        dialog.dispose();
    });
    dialog.setVisible(true);
}
private void DayReport(String chosendate)
{
    JFrame reportframe = new JFrame("Daily Sales Report");
    reportframe.setSize(800,400);
    reportframe.setLocationRelativeTo(this);
    reportframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    reportframe.setLayout(new BorderLayout());
    DefaultTableModel reportmodel = new DefaultTableModel(new String[]{
        "Transaction Data", "Top Selling Product", "Quantity Sold", "Cost of Product","Selling Price","Sales of Product", "Total Profit",
        "Total Revenue"}, 0);
    JTable reporttable = new JTable(reportmodel);
    List<Report> report = reportDAO.getDailySalesReport(chosendate);
    for(Report r : report)
    {
        reportmodel.addRow(new Object[]
            {
                r.getTransactionDate(),
                r.getProductName(),
                r.getTotalQuantity(),
                String.format("₱%.2f", r.getproductcost()),
                String.format("₱%.2f", r.getsellingprice()),
                String.format("₱%.2f", r.getTotalCost()),
                String.format("₱%.2f", r.getTotalProfit()),
                String.format("₱%.2f", r.getTotalRevenue())
            }
        );
    }
    reportframe.add(new JScrollPane(reporttable), BorderLayout.CENTER);
    JPanel btnpanel = new JPanel();
    JButton btnclose = new JButton("Close");
    btnclose.addActionListener(e ->reportframe.dispose());
    JButton btnexport = new JButton("Export to CSV");
    btnexport.addActionListener(e -> exportReportToCSV(report));
    btnpanel.add(btnclose);
    btnpanel.add(btnexport);
    reportframe.add(btnpanel, BorderLayout.SOUTH);
    reportframe.setVisible(true);
}
private void exportProduct(List<Product> reportList) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save CSV File");
    fileChooser.setSelectedFile(new File("ChangeFileName.csv"));
    int userSelection = fileChooser.showSaveDialog(null);
    if (userSelection != JFileChooser.APPROVE_OPTION) {
        return; // User canceled
    }
    File fileToSave = fileChooser.getSelectedFile();
    try (PrintWriter writer = new PrintWriter(fileToSave)) {
        writer.println("ID,Name,Category,Brand,Supplier,Unit,Cost Price,Mark-Up,Selling Price,Quantity,Date Added");
        // CSV ROWS
        for (Product r : reportList) {
            Category cat = categoryDAO.getCategoryById(r.getCategoryId());
            Brand brand = brandDAO.getBrandById(r.getBrandId());
            Supplier sup = supplierDAO.getSupplierById(r.getSupplierId());
            Unit unit = unitDAO.getUnitById(r.getUnitId());
            writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%d,%s%n",
                r.getId(),
                r.getName(),
                cat.getName(),
                brand.getName(),
                sup.getName(),
                unit.getName(),
                formatCurrency(r.getCost()),
                formatCurrency(r.getMarkup()),
                formatCurrency(r.getPrice()),
                r.getQuantity(),
                r.getDateAdded()
            );
        }
        JOptionPane.showMessageDialog(null, "CSV exported successfully!");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error exporting CSV: " + ex.getMessage());
        ex.printStackTrace();
    }
}
private void exportReportToCSV(List<Report> reportList) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save CSV File");
    fileChooser.setSelectedFile(new File("ChangeFileName.csv"));

    int userSelection = fileChooser.showSaveDialog(null);
    if (userSelection != JFileChooser.APPROVE_OPTION) {
        return; // User canceled
    }

    File fileToSave = fileChooser.getSelectedFile();

    try (PrintWriter writer = new PrintWriter(fileToSave)) {

        // CSV HEADER
        writer.println("Transaction Date,Product Name,Quantity Sold,Cost of Product,Selling Price,Product Cost,Profit,Revenue");

        // CSV ROWS
        for (Report r : reportList) {
            writer.printf("%s,%s,%d,%s,%s,%s,%s,%s%n",
                    r.getTransactionDate(),
                    r.getProductName(),
                    r.getTotalQuantity(),
                    formatCurrency(r.getproductcost()),
                    formatCurrency(r.getsellingprice()),
                    formatCurrency(r.getTotalCost()),
                    formatCurrency(r.getTotalProfit()),
                    formatCurrency(r.getTotalRevenue())
            );
        }
        JOptionPane.showMessageDialog(null, "CSV exported successfully!");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error exporting CSV: " + ex.getMessage());
        ex.printStackTrace();
    }
}
private String formatCurrency(double value) {
    return String.format("₱%.2f", value);
}
    // ===== Load Products =====
private void loadProducts() 
{
    model.setRowCount(0);
    List<History> history = historyDAO.getAllHistory();
   // NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
    for (History p : history) 
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
}
