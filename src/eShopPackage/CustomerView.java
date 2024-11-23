/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package eShopPackage;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author xrist
 */
public class CustomerView extends javax.swing.JFrame {
    CardLayout cardSetting,cardLayout, cardSubCategory, cardProducts, cardBuyPoduct;
    /**
     * Creates new form CustomerView
     */
    public CustomerView() {
        initComponents();
        String username = SessionManager.getUsername();
        userNameCustomer.setText(" " + username);
        cardLayout = (CardLayout)(NewFrame.getLayout());
        cardSubCategory = (CardLayout)(NewFrame2.getLayout());
        cardProducts = (CardLayout)(NewFrame3.getLayout());
        cardSetting = (CardLayout)(SettingsNewFrame.getLayout());
    }

//code for street
    private void createProfileButtons(Map<String, Map<String, String>> profileData) {
        JPanel cardProfile = (JPanel) SettingsNewFrame.getComponent(SettingsNewFrame.getComponentCount() - 1);

        // Calculate rows for grid layout (add 1 for the "Add New Address" button)
        int rows = (int) Math.ceil((profileData.size() + 1) / 3.0);
        cardProfile.setLayout(new GridLayout(rows, 3, 10, 10)); // Grid with 3 columns and gaps

        // Add "Add New Address" button
        cardProfile.add(createAddressNewButton());

        // Create and add profile buttons
        profileData.forEach((profileId, profileDetails) -> {
            JButton profileButton = createProfileButton(profileId, profileDetails);
            cardProfile.add(profileButton);
        });

        // Fill remaining cells for consistent grid
        int totalCells = rows * 3;
        int currentCells = profileData.size() + 1; // Profile buttons + "Add New Address"
        for (int i = 0; i < totalCells - currentCells; i++) {
            cardProfile.add(new JPanel()); // Empty panel
        }

        // Revalidate and repaint the panel to reflect changes
        cardProfile.revalidate();
        cardProfile.repaint();
    }

    // Helper to create "Add New Address" button
    private JButton createAddressNewButton() {
        JButton addressNewButton = new JButton("Add New Address");
        addressNewButton.addActionListener(this::addressNewActionPerformed);
        profileNameField.setText("");
        profileSurnameField.setText("");
        profileStreetField.setText("");
        profileStreetNumberField.setText("");
        profileCityField.setText("");
        profilePostCodeField.setText("");
        profilePhoneField.setText("");
        profileCellPhoneField.setText("");
        cardSetting.show(SettingsNewFrame, "cardAddAddress");
        return addressNewButton;
    }

    // Helper to create profile buttons
    private JButton createProfileButton(String profileId, Map<String, String> profileDetails) {
        String street = profileDetails.getOrDefault("street", "Unknown");
        JButton button = new JButton(street);

        button.addActionListener(e -> {
            loadDataIntoAddressCard(profileDetails); // Populate fields with profile data
            currentProfileId = Integer.parseInt(profileId);
            cardSetting.show(SettingsNewFrame, "cardEditAddress"); // Navigate to "Add Address" card
        });

        return button;
    }
    
    private Integer currentProfileId;
    
    private void loadDataIntoAddressCard(Map<String, String> profileDetails) {
        profileNameField.setText(profileDetails.getOrDefault("name", ""));
        profileSurnameField.setText(profileDetails.getOrDefault("surname", ""));
        profileStreetField.setText(profileDetails.getOrDefault("street", ""));
        profileStreetNumberField.setText(profileDetails.getOrDefault("streetnumber", ""));
        profileCityField.setText(profileDetails.getOrDefault("city", ""));
        profilePostCodeField.setText(profileDetails.getOrDefault("postcode", ""));
        profilePhoneField.setText(profileDetails.getOrDefault("phone", ""));
        profileCellPhoneField.setText(profileDetails.getOrDefault("cellphone", ""));
    }
//finish code for street
  
//code for view category
    private void createCategoriesButton() {
        JPanel cardCategories = (JPanel) NavigationCategories.getComponent(NavigationCategories.getComponentCount() - 1);
        // Calculate rows for grid layout (add 1 for the "Add New Address" button)
        cardCategories.setLayout(new GridLayout(0, 1, 0, 10)); // Grid with 3 columns and gaps
        
        try{
            DatabaseConnection dbConnection = new DatabaseConnection();
            Map<Integer, String> categories = new DataAccessObject(dbConnection).getCategoryNames();
            for (Map.Entry<Integer, String> entry : categories.entrySet()) {
                int categoryId = entry.getKey();
                String categoryName = entry.getValue();
                
                JButton categoryButton = new JButton(categoryName);
                categoryButton.addActionListener(e -> {
                    cardSubCategory.show(NewFrame2, "cardViewSubCategories");
                    subCateogriesButtons.removeAll();
                    createSubCategoryButtons(categoryId);
                });
                
                cardCategories.add(categoryButton);
            }
            
        cardCategories.revalidate();
        cardCategories.repaint();

        }catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating category buttons: " + e.getMessage());
        }
    }
//finish code for view category
    
//code for view subCategory
    private void createSubCategoryButtons(int categoryId){
        JPanel cardSubCategories = (JPanel) NavigationSubCategories.getComponent(NavigationSubCategories.getComponentCount() - 1);
        
        cardSubCategories.setLayout(new GridLayout(0, 1, 0, 10));
        
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            Map<Integer, String> subCategories = new DataAccessObject(dbConnection).getSubCategoryNames(categoryId);

            // Create buttons for each subcategory
            for (Map.Entry<Integer, String> entry : subCategories.entrySet()) {
                int subCategoryId = entry.getKey();
                String subCategoryName = entry.getValue();

                JButton subCategoryButton = new JButton(subCategoryName);
                subCategoryButton.addActionListener(e -> {
                    ViewProduct.removeAll();
                    cardProducts.show(NewFrame3, "cardViewProducts");
                    ViewProduct(subCategoryId);
                });

                // Add the button to the panel
                cardSubCategories.add(subCategoryButton);
            }
            // Refresh the panel to display new buttons
            cardSubCategories.revalidate();
            cardSubCategories.repaint();

        }catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating subcategory buttons: " + e.getMessage());
        }
    }
//finish code for view subCategory
    
//code for view products
    private void ViewProduct(int subCategoryId){
        JPanel cardProduct = (JPanel) NewFrame3.getComponent(NewFrame3.getComponentCount() - 1);
        JPanel viewProducts = (JPanel) cardProduct.getComponent(0);
        
        viewProducts.setLayout(new GridLayout(0, 2, 0, 5));
        
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            Map<Integer, String> product = new DataAccessObject(dbConnection).getProductNames(subCategoryId);

            // Create buttons for each subcategory
            for (Map.Entry<Integer, String> entry : product.entrySet()) {
                int productId = entry.getKey();
                String productName = entry.getValue();
                JButton subCategoryButton = new JButton(productName);
                subCategoryButton.addActionListener(e -> {
                    loadDataIntoProduct(productId);
                    cardProducts.show(NewFrame3, "cardBuyProduct");
                    
                });

                // Add the button to the panel
                viewProducts.add(subCategoryButton);
            }

            // Refresh the panel to display new buttons
            viewProducts.revalidate();
            viewProducts.repaint();

        }catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating subcategory buttons: " + e.getMessage());
        }
    }
    
    private void loadDataIntoProduct(int productId) {
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            Map<String, String> productDetails = new DataAccessObject(dbConnection).getProductDetails(productId);

            // Set the text fields to product details
            productName.setText(productDetails.getOrDefault("name", "N/A"));
            productDescription.setText(productDetails.getOrDefault("description", "No description available"));
            productStock.setText(productDetails.getOrDefault("stock", "0"));
            ProductPrice.setText(productDetails.getOrDefault("price", "0.00"));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading product details: " + e.getMessage());
        }
    
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainNavegationPanel = new javax.swing.JPanel();
        userNameCustomer = new javax.swing.JLabel();
        Categories = new javax.swing.JButton();
        SettingButton = new javax.swing.JButton();
        NewFrame = new javax.swing.JPanel();
        emptyNewFrame = new javax.swing.JPanel();
        ViewCategoriesFrame = new javax.swing.JPanel();
        NavigationCategories = new javax.swing.JPanel();
        NavigationCateogriesLabel = new javax.swing.JLabel();
        categoriesButtons = new javax.swing.JPanel();
        NewFrame2 = new javax.swing.JPanel();
        emptyNewFrame2 = new javax.swing.JPanel();
        ViewSubCategories = new javax.swing.JPanel();
        NavigationSubCategories = new javax.swing.JPanel();
        NavigationSubCategoriesLabel = new javax.swing.JLabel();
        subCateogriesButtons = new javax.swing.JPanel();
        NewFrame3 = new javax.swing.JPanel();
        emptyNewFrame3 = new javax.swing.JPanel();
        Product = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        productBuyButton = new javax.swing.JButton();
        productName = new javax.swing.JLabel();
        productStock = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        productDescription = new javax.swing.JTextArea();
        ProductPrice = new javax.swing.JLabel();
        productStock1 = new javax.swing.JLabel();
        productStock2 = new javax.swing.JLabel();
        ViewProductFrame = new javax.swing.JPanel();
        ViewProduct = new javax.swing.JPanel();
        SettingsFrame = new javax.swing.JPanel();
        SubNavigationBar = new javax.swing.JPanel();
        SubNavigationLabel = new javax.swing.JLabel();
        barMain2 = new javax.swing.JPanel();
        settingsAddressButton = new javax.swing.JButton();
        SettingsNewFrame = new javax.swing.JPanel();
        empty = new javax.swing.JPanel();
        editAddress = new javax.swing.JPanel();
        profileStreetLabel = new javax.swing.JLabel();
        profileSurnameLabel = new javax.swing.JLabel();
        profileSurnameField = new javax.swing.JTextField();
        profileNameField = new javax.swing.JTextField();
        profileNameLabel = new javax.swing.JLabel();
        profileStreetNumberField = new javax.swing.JTextField();
        profileStreetNumberLabel = new javax.swing.JLabel();
        profileStreetField = new javax.swing.JTextField();
        profilePhoneLabel = new javax.swing.JLabel();
        profilePostCodeField = new javax.swing.JTextField();
        profileTKLabel = new javax.swing.JLabel();
        profileCityField = new javax.swing.JTextField();
        profileCityLabel = new javax.swing.JLabel();
        profilePhoneField = new javax.swing.JTextField();
        profilePhone1Label = new javax.swing.JLabel();
        profileCellPhoneField = new javax.swing.JTextField();
        profileSaveButton = new javax.swing.JButton();
        addAddress = new javax.swing.JPanel();
        profileStreetLabel1 = new javax.swing.JLabel();
        profileSurnameLabel1 = new javax.swing.JLabel();
        profileSurnameField1 = new javax.swing.JTextField();
        profileNameField1 = new javax.swing.JTextField();
        profileNameLabel1 = new javax.swing.JLabel();
        profileStreetNumberField1 = new javax.swing.JTextField();
        profileStreetNumberLabel1 = new javax.swing.JLabel();
        profileStreetField1 = new javax.swing.JTextField();
        profilePhoneLabel1 = new javax.swing.JLabel();
        profilePostCodeField1 = new javax.swing.JTextField();
        profileTKLabel1 = new javax.swing.JLabel();
        profileCityField1 = new javax.swing.JTextField();
        profileCityLabel1 = new javax.swing.JLabel();
        profilePhoneField1 = new javax.swing.JTextField();
        profilePhone1Label1 = new javax.swing.JLabel();
        profileCellPhoneField1 = new javax.swing.JTextField();
        profileSaveButton1 = new javax.swing.JButton();
        profile = new javax.swing.JPanel();
        addressNew = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        MainNavegationPanel.setBackground(new java.awt.Color(0, 153, 153));
        MainNavegationPanel.setPreferredSize(new java.awt.Dimension(147, 236));

        userNameCustomer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userNameCustomer.setText("Customer Name");
        userNameCustomer.setMaximumSize(new java.awt.Dimension(47, 20));
        userNameCustomer.setMinimumSize(new java.awt.Dimension(47, 20));
        userNameCustomer.setPreferredSize(new java.awt.Dimension(47, 20));
        userNameCustomer.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                userNameCustomerAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        Categories.setText("Κατηγορίες");
        Categories.setMaximumSize(new java.awt.Dimension(133, 27));
        Categories.setMinimumSize(new java.awt.Dimension(133, 27));
        Categories.setPreferredSize(new java.awt.Dimension(133, 27));
        Categories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CategoriesActionPerformed(evt);
            }
        });

        SettingButton.setText("Ρύθμισης");
        SettingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SettingButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MainNavegationPanelLayout = new javax.swing.GroupLayout(MainNavegationPanel);
        MainNavegationPanel.setLayout(MainNavegationPanelLayout);
        MainNavegationPanelLayout.setHorizontalGroup(
            MainNavegationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainNavegationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MainNavegationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainNavegationPanelLayout.createSequentialGroup()
                        .addComponent(userNameCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(8, 8, 8))
                    .addGroup(MainNavegationPanelLayout.createSequentialGroup()
                        .addGroup(MainNavegationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(Categories, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(SettingButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        MainNavegationPanelLayout.setVerticalGroup(
            MainNavegationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainNavegationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userNameCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Categories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 587, Short.MAX_VALUE)
                .addComponent(SettingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        NewFrame.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout emptyNewFrameLayout = new javax.swing.GroupLayout(emptyNewFrame);
        emptyNewFrame.setLayout(emptyNewFrameLayout);
        emptyNewFrameLayout.setHorizontalGroup(
            emptyNewFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1203, Short.MAX_VALUE)
        );
        emptyNewFrameLayout.setVerticalGroup(
            emptyNewFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );

        NewFrame.add(emptyNewFrame, "card2");

        NavigationCategories.setBackground(new java.awt.Color(0, 102, 102));

        NavigationCateogriesLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        NavigationCateogriesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NavigationCateogriesLabel.setText("Κατηγορίες");

        categoriesButtons.setBackground(new java.awt.Color(0, 102, 102));

        javax.swing.GroupLayout categoriesButtonsLayout = new javax.swing.GroupLayout(categoriesButtons);
        categoriesButtons.setLayout(categoriesButtonsLayout);
        categoriesButtonsLayout.setHorizontalGroup(
            categoriesButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        categoriesButtonsLayout.setVerticalGroup(
            categoriesButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout NavigationCategoriesLayout = new javax.swing.GroupLayout(NavigationCategories);
        NavigationCategories.setLayout(NavigationCategoriesLayout);
        NavigationCategoriesLayout.setHorizontalGroup(
            NavigationCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NavigationCategoriesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(NavigationCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NavigationCateogriesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                    .addComponent(categoriesButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        NavigationCategoriesLayout.setVerticalGroup(
            NavigationCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavigationCategoriesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NavigationCateogriesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(categoriesButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(678, Short.MAX_VALUE))
        );

        NewFrame2.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout emptyNewFrame2Layout = new javax.swing.GroupLayout(emptyNewFrame2);
        emptyNewFrame2.setLayout(emptyNewFrame2Layout);
        emptyNewFrame2Layout.setHorizontalGroup(
            emptyNewFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1011, Short.MAX_VALUE)
        );
        emptyNewFrame2Layout.setVerticalGroup(
            emptyNewFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 712, Short.MAX_VALUE)
        );

        NewFrame2.add(emptyNewFrame2, "card3");

        NavigationSubCategories.setBackground(new java.awt.Color(0, 102, 102));

        NavigationSubCategoriesLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        NavigationSubCategoriesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NavigationSubCategoriesLabel.setText("Υποκατηγορίες");

        subCateogriesButtons.setBackground(new java.awt.Color(0, 102, 102));

        javax.swing.GroupLayout subCateogriesButtonsLayout = new javax.swing.GroupLayout(subCateogriesButtons);
        subCateogriesButtons.setLayout(subCateogriesButtonsLayout);
        subCateogriesButtonsLayout.setHorizontalGroup(
            subCateogriesButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        subCateogriesButtonsLayout.setVerticalGroup(
            subCateogriesButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout NavigationSubCategoriesLayout = new javax.swing.GroupLayout(NavigationSubCategories);
        NavigationSubCategories.setLayout(NavigationSubCategoriesLayout);
        NavigationSubCategoriesLayout.setHorizontalGroup(
            NavigationSubCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavigationSubCategoriesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(NavigationSubCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(NavigationSubCategoriesLayout.createSequentialGroup()
                        .addComponent(NavigationSubCategoriesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(subCateogriesButtons, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        NavigationSubCategoriesLayout.setVerticalGroup(
            NavigationSubCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavigationSubCategoriesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NavigationSubCategoriesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subCateogriesButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        NewFrame3.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout emptyNewFrame3Layout = new javax.swing.GroupLayout(emptyNewFrame3);
        emptyNewFrame3.setLayout(emptyNewFrame3Layout);
        emptyNewFrame3Layout.setHorizontalGroup(
            emptyNewFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 865, Short.MAX_VALUE)
        );
        emptyNewFrame3Layout.setVerticalGroup(
            emptyNewFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );

        NewFrame3.add(emptyNewFrame3, "card2");

        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        productBuyButton.setText("Προσθήκη στο καλάθι");

        productName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        productName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productName.setText("Name");

        productStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        productStock.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        productStock.setText("Price");

        productDescription.setColumns(20);
        productDescription.setRows(5);
        jScrollPane1.setViewportView(productDescription);

        ProductPrice.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        ProductPrice.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ProductPrice.setText("Price");

        productStock1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        productStock1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productStock1.setText("Price:");

        productStock2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        productStock2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productStock2.setText("Stock:");

        javax.swing.GroupLayout ProductLayout = new javax.swing.GroupLayout(Product);
        Product.setLayout(ProductLayout);
        ProductLayout.setHorizontalGroup(
            ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductLayout.createSequentialGroup()
                .addGroup(ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ProductLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(ProductLayout.createSequentialGroup()
                        .addGroup(ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(productName, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(ProductLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(productStock2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(productStock, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(productStock1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ProductPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                        .addGroup(ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(productBuyButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE))))
                .addContainerGap())
        );
        ProductLayout.setVerticalGroup(
            ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductLayout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ProductLayout.createSequentialGroup()
                        .addComponent(productName, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(100, 100, 100)
                        .addGroup(ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(productStock2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(productStock, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(productStock1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ProductPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ProductLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(productBuyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 310, Short.MAX_VALUE)))
                .addContainerGap())
        );

        NewFrame3.add(Product, "cardBuyProduct");

        javax.swing.GroupLayout ViewProductLayout = new javax.swing.GroupLayout(ViewProduct);
        ViewProduct.setLayout(ViewProductLayout);
        ViewProductLayout.setHorizontalGroup(
            ViewProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 853, Short.MAX_VALUE)
        );
        ViewProductLayout.setVerticalGroup(
            ViewProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 688, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ViewProductFrameLayout = new javax.swing.GroupLayout(ViewProductFrame);
        ViewProductFrame.setLayout(ViewProductFrameLayout);
        ViewProductFrameLayout.setHorizontalGroup(
            ViewProductFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewProductFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ViewProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        ViewProductFrameLayout.setVerticalGroup(
            ViewProductFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewProductFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ViewProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        NewFrame3.add(ViewProductFrame, "cardViewProducts");

        javax.swing.GroupLayout ViewSubCategoriesLayout = new javax.swing.GroupLayout(ViewSubCategories);
        ViewSubCategories.setLayout(ViewSubCategoriesLayout);
        ViewSubCategoriesLayout.setHorizontalGroup(
            ViewSubCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewSubCategoriesLayout.createSequentialGroup()
                .addComponent(NavigationSubCategories, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewFrame3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        ViewSubCategoriesLayout.setVerticalGroup(
            ViewSubCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewSubCategoriesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ViewSubCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NavigationSubCategories, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NewFrame3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        NewFrame2.add(ViewSubCategories, "cardViewSubCategories");

        javax.swing.GroupLayout ViewCategoriesFrameLayout = new javax.swing.GroupLayout(ViewCategoriesFrame);
        ViewCategoriesFrame.setLayout(ViewCategoriesFrameLayout);
        ViewCategoriesFrameLayout.setHorizontalGroup(
            ViewCategoriesFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewCategoriesFrameLayout.createSequentialGroup()
                .addComponent(NavigationCategories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewFrame2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        ViewCategoriesFrameLayout.setVerticalGroup(
            ViewCategoriesFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewCategoriesFrameLayout.createSequentialGroup()
                .addGroup(ViewCategoriesFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NavigationCategories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ViewCategoriesFrameLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(NewFrame2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        NewFrame.add(ViewCategoriesFrame, "cardViewCategories");

        SubNavigationBar.setBackground(new java.awt.Color(0, 102, 102));

        SubNavigationLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        SubNavigationLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SubNavigationLabel.setText("Ρύθμισης");

        barMain2.setBackground(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout barMain2Layout = new javax.swing.GroupLayout(barMain2);
        barMain2.setLayout(barMain2Layout);
        barMain2Layout.setHorizontalGroup(
            barMain2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        barMain2Layout.setVerticalGroup(
            barMain2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        settingsAddressButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        settingsAddressButton.setText("Διεύθυνσης");
        settingsAddressButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsAddressButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SubNavigationBarLayout = new javax.swing.GroupLayout(SubNavigationBar);
        SubNavigationBar.setLayout(SubNavigationBarLayout);
        SubNavigationBarLayout.setHorizontalGroup(
            SubNavigationBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubNavigationBarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SubNavigationBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(settingsAddressButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SubNavigationLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                    .addComponent(barMain2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        SubNavigationBarLayout.setVerticalGroup(
            SubNavigationBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubNavigationBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SubNavigationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(barMain2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(settingsAddressButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        SettingsNewFrame.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout emptyLayout = new javax.swing.GroupLayout(empty);
        empty.setLayout(emptyLayout);
        emptyLayout.setHorizontalGroup(
            emptyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 970, Short.MAX_VALUE)
        );
        emptyLayout.setVerticalGroup(
            emptyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 714, Short.MAX_VALUE)
        );

        SettingsNewFrame.add(empty, "cardNewEmpty");

        profileStreetLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profileStreetLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profileStreetLabel.setText("Οδός:");

        profileSurnameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profileSurnameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profileSurnameLabel.setText("Επώνυμο:");

        profileNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profileNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profileNameLabel.setText("Όνομα:");

        profileStreetNumberLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profileStreetNumberLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profileStreetNumberLabel.setText("Αριθμός Οδού:");

        profilePhoneLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profilePhoneLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profilePhoneLabel.setText("Τηλέφωνο:");

        profileTKLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profileTKLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profileTKLabel.setText("Τ.Κ.:");

        profileCityLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profileCityLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profileCityLabel.setText("Πόλη:");

        profilePhone1Label.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profilePhone1Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profilePhone1Label.setText("Κινητό:");

        profileSaveButton.setText("Ενημέρωση");
        profileSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout editAddressLayout = new javax.swing.GroupLayout(editAddress);
        editAddress.setLayout(editAddressLayout);
        editAddressLayout.setHorizontalGroup(
            editAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editAddressLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(editAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(profilePhoneLabel)
                    .addComponent(profileStreetLabel)
                    .addComponent(profileNameLabel)
                    .addComponent(profileCityLabel))
                .addGap(18, 18, 18)
                .addGroup(editAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editAddressLayout.createSequentialGroup()
                        .addComponent(profilePhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(profilePhone1Label))
                    .addGroup(editAddressLayout.createSequentialGroup()
                        .addComponent(profileCityField, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(profileTKLabel))
                    .addGroup(editAddressLayout.createSequentialGroup()
                        .addComponent(profileNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(profileSurnameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(editAddressLayout.createSequentialGroup()
                        .addComponent(profileStreetField, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(profileStreetNumberLabel)))
                .addGap(18, 18, 18)
                .addGroup(editAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(profileSaveButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(editAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(profileSurnameField)
                        .addComponent(profileStreetNumberField, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                        .addComponent(profilePostCodeField, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                        .addComponent(profileCellPhoneField, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)))
                .addGap(269, 269, 269))
        );
        editAddressLayout.setVerticalGroup(
            editAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editAddressLayout.createSequentialGroup()
                .addGap(158, 158, 158)
                .addGroup(editAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profileSurnameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileSurnameField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(editAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profileStreetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileStreetNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileStreetNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileStreetField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(editAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profileCityField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileTKLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profilePostCodeField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileCityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(editAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profilePhoneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profilePhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profilePhone1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileCellPhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addComponent(profileSaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(295, Short.MAX_VALUE))
        );

        SettingsNewFrame.add(editAddress, "cardEditAddress");

        profileStreetLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profileStreetLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profileStreetLabel1.setText("Οδός:");

        profileSurnameLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profileSurnameLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profileSurnameLabel1.setText("Επώνυμο:");

        profileNameLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profileNameLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profileNameLabel1.setText("Όνομα:");

        profileStreetNumberLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profileStreetNumberLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profileStreetNumberLabel1.setText("Αριθμός Οδού:");

        profilePhoneLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profilePhoneLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profilePhoneLabel1.setText("Τηλέφωνο:");

        profileTKLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profileTKLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profileTKLabel1.setText("Τ.Κ.:");

        profileCityLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profileCityLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profileCityLabel1.setText("Πόλη:");

        profilePhone1Label1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profilePhone1Label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profilePhone1Label1.setText("Κινητό:");

        profileSaveButton1.setText("Αποθήκευση");
        profileSaveButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileSaveButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addAddressLayout = new javax.swing.GroupLayout(addAddress);
        addAddress.setLayout(addAddressLayout);
        addAddressLayout.setHorizontalGroup(
            addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addAddressLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(profilePhoneLabel1)
                    .addComponent(profileStreetLabel1)
                    .addComponent(profileNameLabel1)
                    .addComponent(profileCityLabel1))
                .addGap(18, 18, 18)
                .addGroup(addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addAddressLayout.createSequentialGroup()
                        .addComponent(profilePhoneField1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(profilePhone1Label1))
                    .addGroup(addAddressLayout.createSequentialGroup()
                        .addComponent(profileCityField1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(profileTKLabel1))
                    .addGroup(addAddressLayout.createSequentialGroup()
                        .addComponent(profileNameField1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(profileSurnameLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(addAddressLayout.createSequentialGroup()
                        .addComponent(profileStreetField1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(profileStreetNumberLabel1)))
                .addGap(18, 18, 18)
                .addGroup(addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(profileSurnameField1)
                    .addComponent(profileStreetNumberField1, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(profilePostCodeField1, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(profileCellPhoneField1, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(profileSaveButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(269, 269, 269))
        );
        addAddressLayout.setVerticalGroup(
            addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addAddressLayout.createSequentialGroup()
                .addGap(158, 158, 158)
                .addGroup(addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profileSurnameLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileSurnameField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileNameField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileNameLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profileStreetLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileStreetNumberField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileStreetNumberLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileStreetField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profileCityField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileTKLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profilePostCodeField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileCityLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profilePhoneLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profilePhoneField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profilePhone1Label1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileCellPhoneField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addComponent(profileSaveButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(296, Short.MAX_VALUE))
        );

        SettingsNewFrame.add(addAddress, "cardAddAddress");

        addressNew.setText("Προσθήκη");
        addressNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressNewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout profileLayout = new javax.swing.GroupLayout(profile);
        profile.setLayout(profileLayout);
        profileLayout.setHorizontalGroup(
            profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profileLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(addressNew, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(655, Short.MAX_VALUE))
        );
        profileLayout.setVerticalGroup(
            profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profileLayout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(addressNew, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(539, Short.MAX_VALUE))
        );

        SettingsNewFrame.add(profile, "cardProfile");

        javax.swing.GroupLayout SettingsFrameLayout = new javax.swing.GroupLayout(SettingsFrame);
        SettingsFrame.setLayout(SettingsFrameLayout);
        SettingsFrameLayout.setHorizontalGroup(
            SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SettingsFrameLayout.createSequentialGroup()
                .addComponent(SubNavigationBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(SettingsNewFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 970, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        SettingsFrameLayout.setVerticalGroup(
            SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SettingsFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SubNavigationBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SettingsNewFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        NewFrame.add(SettingsFrame, "cardSettings");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MainNavegationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainNavegationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NewFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SettingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SettingButtonActionPerformed
       cardLayout.show(NewFrame,"cardSettings");
    }//GEN-LAST:event_SettingButtonActionPerformed

    private void userNameCustomerAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_userNameCustomerAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_userNameCustomerAncestorAdded

    private void settingsAddressButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsAddressButtonActionPerformed
        JPanel cardProfile = (JPanel) SettingsNewFrame.getComponent(SettingsNewFrame.getComponentCount() - 1);
        cardProfile.removeAll(); // Clear previous components

        DataAccessObject dao = new DataAccessObject(new DatabaseConnection());
        Map<String, Map<String, String>> profileData = dao.getUserProfiles(SessionManager.getUserId());

        if (profileData != null && !profileData.isEmpty()) {
            System.out.println("Profile Data: " + profileData);
            createProfileButtons(profileData); // Create buttons dynamically
        } else {
            System.out.println("No profile data found.");
            cardSetting.show(SettingsNewFrame, "cardAddAddress");
            return;
        }
        
        cardSetting.show(SettingsNewFrame, "cardProfile"); // Show the profile card
    }//GEN-LAST:event_settingsAddressButtonActionPerformed

    private void CategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CategoriesActionPerformed
        cardLayout.show(NewFrame, "cardViewCategories");
        categoriesButtons.removeAll();
        createCategoriesButton();
    }//GEN-LAST:event_CategoriesActionPerformed

    private void addressNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressNewActionPerformed
        cardSetting.show(SettingsNewFrame,"cardAddAddress");
    }//GEN-LAST:event_addressNewActionPerformed

    private void profileSaveButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileSaveButton1ActionPerformed
        String name = profileNameField1.getText().trim();
        String surname = profileSurnameField1.getText().trim();
        String street = profileStreetField1.getText().trim();
        String streetNumber = profileStreetNumberField1.getText().trim(); // Trim spaces
        String city = profileCityField1.getText().trim();
        String postCode = profilePostCodeField1.getText().trim(); // Trim spaces
        String phone = profilePhoneField1.getText().trim(); // Trim spaces
        String cellPhone = profileCellPhoneField1.getText().trim(); // Trim spaces

        Integer userId = SessionManager.getUserId();

        // Log field contents for debugging
        System.out.println("streetnumber: '" + streetNumber + "'");
        System.out.println("postcode: '" + postCode + "'");
        System.out.println("phone: '" + phone + "'");
        System.out.println("cellPhone: '" + cellPhone + "'");

        Long longStreetNumber, longPostCode, longPhone, longCellPhone;

        // Validate that the fields contain only numeric values
        if (!streetNumber.matches("\\d+") || !postCode.matches("\\d+") || !phone.matches("\\d+") || !cellPhone.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Παρακαλώ εισάγετε έγκυρο αριθμό για την τιμή.");
            return;  // Exit the method if the input is invalid
        }

        // Parse the values
        try {
            longStreetNumber = Long.parseLong(streetNumber);
            longPostCode = Long.parseLong(postCode);
            longPhone = Long.parseLong(phone);
            longCellPhone = Long.parseLong(cellPhone);
        } catch (NumberFormatException e) {
            // Print error details for debugging
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace(); // Log the error for debugging
            return;  // Exit the method if the parsing fails
        }

        try {
            // Create DAO and insert product
            DatabaseConnection dbConnection = new DatabaseConnection();
            DataAccessObject dao = new DataAccessObject(dbConnection);
            dao.insertUserProfile(userId, name, surname, street, longStreetNumber, city, longPostCode, longPhone, longCellPhone );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την προσθήκη του προφίλ: " + e.getMessage());
        }

        settingsAddressButtonActionPerformed(evt);
    }//GEN-LAST:event_profileSaveButton1ActionPerformed

    private void profileSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileSaveButtonActionPerformed
        // Get values from fields and trim them
        String name = profileNameField.getText().trim();
        String surname = profileSurnameField.getText().trim();
        String street = profileStreetField.getText().trim();
        String streetNumber = profileStreetNumberField.getText().trim(); // Trim spaces
        String city = profileCityField.getText().trim();
        String postCode = profilePostCodeField.getText().trim(); // Trim spaces
        String phone = profilePhoneField.getText().trim(); // Trim spaces
        String cellPhone = profileCellPhoneField.getText().trim(); // Trim spaces

        Integer profileId = currentProfileId;

        // Log field contents for debugging
        System.out.println("streetnumber: '" + streetNumber + "'");
        System.out.println("postcode: '" + postCode + "'");
        System.out.println("phone: '" + phone + "'");
        System.out.println("cellPhone: '" + cellPhone + "'");

        Long longStreetNumber, longPostCode, longPhone, longCellPhone;

        // Validate that the fields contain only numeric values
        if (!streetNumber.matches("\\d+") || !postCode.matches("\\d+") || !phone.matches("\\d+") || !cellPhone.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Παρακαλώ εισάγετε έγκυρο αριθμό για την τιμή.");
            return;  // Exit the method if the input is invalid
        }

        // Parse the values
        try {
            longStreetNumber = Long.parseLong(streetNumber);
            longPostCode = Long.parseLong(postCode);
            longPhone = Long.parseLong(phone);
            longCellPhone = Long.parseLong(cellPhone);
        } catch (NumberFormatException e) {
            // Print error details for debugging
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace(); // Log the error for debugging
            return;  // Exit the method if the parsing fails
        }

        try {
            // Create DAO and insert product
            DatabaseConnection dbConnection = new DatabaseConnection();
            DataAccessObject dao = new DataAccessObject(dbConnection);
            dao.editUserProfile(profileId, name, surname, street, longStreetNumber, city, longPostCode, longPhone, longCellPhone );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την προσθήκη του προφίλ: " + e.getMessage());
        }

        settingsAddressButtonActionPerformed(evt);
    }//GEN-LAST:event_profileSaveButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        cardProducts.show(NewFrame3, "cardViewProducts");
    }//GEN-LAST:event_backButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CustomerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CustomerView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Categories;
    private javax.swing.JPanel MainNavegationPanel;
    private javax.swing.JPanel NavigationCategories;
    private javax.swing.JLabel NavigationCateogriesLabel;
    private javax.swing.JPanel NavigationSubCategories;
    private javax.swing.JLabel NavigationSubCategoriesLabel;
    private javax.swing.JPanel NewFrame;
    private javax.swing.JPanel NewFrame2;
    private javax.swing.JPanel NewFrame3;
    private javax.swing.JPanel Product;
    private javax.swing.JLabel ProductPrice;
    private javax.swing.JButton SettingButton;
    private javax.swing.JPanel SettingsFrame;
    private javax.swing.JPanel SettingsNewFrame;
    private javax.swing.JPanel SubNavigationBar;
    private javax.swing.JLabel SubNavigationLabel;
    private javax.swing.JPanel ViewCategoriesFrame;
    private javax.swing.JPanel ViewProduct;
    private javax.swing.JPanel ViewProductFrame;
    private javax.swing.JPanel ViewSubCategories;
    private javax.swing.JPanel addAddress;
    private javax.swing.JButton addressNew;
    private javax.swing.JButton backButton;
    private javax.swing.JPanel barMain2;
    private javax.swing.JPanel categoriesButtons;
    private javax.swing.JPanel editAddress;
    private javax.swing.JPanel empty;
    private javax.swing.JPanel emptyNewFrame;
    private javax.swing.JPanel emptyNewFrame2;
    private javax.swing.JPanel emptyNewFrame3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton productBuyButton;
    private javax.swing.JTextArea productDescription;
    private javax.swing.JLabel productName;
    private javax.swing.JLabel productStock;
    private javax.swing.JLabel productStock1;
    private javax.swing.JLabel productStock2;
    private javax.swing.JPanel profile;
    private javax.swing.JTextField profileCellPhoneField;
    private javax.swing.JTextField profileCellPhoneField1;
    private javax.swing.JTextField profileCityField;
    private javax.swing.JTextField profileCityField1;
    private javax.swing.JLabel profileCityLabel;
    private javax.swing.JLabel profileCityLabel1;
    private javax.swing.JTextField profileNameField;
    private javax.swing.JTextField profileNameField1;
    private javax.swing.JLabel profileNameLabel;
    private javax.swing.JLabel profileNameLabel1;
    private javax.swing.JLabel profilePhone1Label;
    private javax.swing.JLabel profilePhone1Label1;
    private javax.swing.JTextField profilePhoneField;
    private javax.swing.JTextField profilePhoneField1;
    private javax.swing.JLabel profilePhoneLabel;
    private javax.swing.JLabel profilePhoneLabel1;
    private javax.swing.JTextField profilePostCodeField;
    private javax.swing.JTextField profilePostCodeField1;
    private javax.swing.JButton profileSaveButton;
    private javax.swing.JButton profileSaveButton1;
    private javax.swing.JTextField profileStreetField;
    private javax.swing.JTextField profileStreetField1;
    private javax.swing.JLabel profileStreetLabel;
    private javax.swing.JLabel profileStreetLabel1;
    private javax.swing.JTextField profileStreetNumberField;
    private javax.swing.JTextField profileStreetNumberField1;
    private javax.swing.JLabel profileStreetNumberLabel;
    private javax.swing.JLabel profileStreetNumberLabel1;
    private javax.swing.JTextField profileSurnameField;
    private javax.swing.JTextField profileSurnameField1;
    private javax.swing.JLabel profileSurnameLabel;
    private javax.swing.JLabel profileSurnameLabel1;
    private javax.swing.JLabel profileTKLabel;
    private javax.swing.JLabel profileTKLabel1;
    private javax.swing.JButton settingsAddressButton;
    private javax.swing.JPanel subCateogriesButtons;
    private javax.swing.JLabel userNameCustomer;
    // End of variables declaration//GEN-END:variables
}
