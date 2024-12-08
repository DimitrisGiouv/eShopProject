/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package eShopPackage;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author xrist
 */
public class CustomerView extends javax.swing.JFrame {
    CardLayout cardSetting,cardLayout, cardSubCategory, cardProducts;
    /**
     * Creates new form CustomerView
     */
    public CustomerView() {
        initComponents();
        createNewProductButtons();
        if("admin".equals(SessionManager.getRole())){
            mangamentButton.setVisible(true);
        }
        else{
            mangamentButton.setVisible(false);
        }
        
        String username = SessionManager.getUsername();
        userNameCustomer.setText(" " + username);
        cardLayout = (CardLayout)(NewFrame.getLayout());
        cardSubCategory = (CardLayout)(NewFrame2.getLayout());
        cardProducts = (CardLayout)(NewFrame3.getLayout());
        cardSetting = (CardLayout)(SettingsNewFrame.getLayout());
        
    }

    
    private void createNewProductButtons(){
        mainFrameNewProductPanel.setLayout(new GridLayout(0, 3, 0, 2));
        
        try {
            // Fetch basket details from the database
            DatabaseConnection dbConnection = new DatabaseConnection();
            Map<Integer, String> NewProducts = new DataAccessObject(dbConnection).getLastSixProduct();
            for (Map.Entry<Integer, String> entry : NewProducts.entrySet()) {
                int productId = entry.getKey();
                String productName = entry.getValue();
                JButton productButton = new JButton(productName);
                productButton.putClientProperty("productId", productId);
                
                productButton.addActionListener(e -> {
                loadDataIntoNewProduct(productId);
                cardLayout.show(NewFrame, "cardBuyProduct1");
               });

               mainFrameNewProductPanel.add(productButton);
            }
        }catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating newProduct buttons: " + e.getMessage());
        }
    }
    
     private void loadDataIntoNewProduct(int productId) {
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            Map<String, String> productDetails = new DataAccessObject(dbConnection).getProductDetails(productId);

            // Set the text fields to product details
            productName1.setText(productDetails.getOrDefault("name", "N/A"));
            productDescription1.setText(productDetails.getOrDefault("description", "No description available"));
            productStock4.setText(productDetails.getOrDefault("stock", "0"));
            ProductPrice1.setText(productDetails.getOrDefault("price", "0.00"));
            productName1.putClientProperty("productId", productId);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading product details: " + e.getMessage());
        }
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
                subCategoryButton.putClientProperty("productId", productId);
                
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
            productName.putClientProperty("productId", productId);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading product details: " + e.getMessage());
        }
    }
//finish code for view products

//code for buyProduct
    private void ViewDetailsBuyProduct(int userId) {
        // Set the layout for each panel
        basketNameDetails.setLayout(new GridLayout(0, 1, 0, 15)); // Vertical layout with 15 pixels between rows
        basketQuantityDetails.setLayout(new GridLayout(0, 1, 0, 1)); // No space between rows for quantity
        basketPriceDetails.setLayout(new GridLayout(0, 1, 0, 15)); // Vertical layout with 15 pixels between rows for price
        basketTotalPriceDetails.setLayout(new GridLayout(0, 1, 0, 15));
        basketDelProduct.setLayout(new GridLayout(0, 1, 0, 2));
        
        double totalPriceSum = 0;
        
        try {
            // Fetch basket details from the database
            DatabaseConnection dbConnection = new DatabaseConnection();
            Map<Integer, Map<String, String>> basket = new DataAccessObject(dbConnection).getBasketDetails(userId);

            // Create labels and text fields for each product in the basket
            for (Map.Entry<Integer, Map<String, String>> entry : basket.entrySet()) {
                int basketId = entry.getKey();
                Map<String, String> productDetails = entry.getValue();

                // Retrieve product details
                String productName = productDetails.get("name");
                String quantity = productDetails.get("quantity");
                String price = productDetails.get("price");
                
                int quantityPrice = Integer.parseInt(quantity);
                double priceUnit = Double.parseDouble(price);
                double totalPrice = quantityPrice * priceUnit;
                totalPriceSum += totalPrice;
                
                // Convert total price to string (with two decimal places)
                String totalPriceString = String.format("%.2f", totalPrice);
                
                // Create product label
                JLabel productNameLabel = new JLabel(productName);
                JTextField productQuantityField = new JTextField(quantity);
                JLabel productPriceLabel = new JLabel(price);
                JLabel productTotalPriceLabel = new JLabel(totalPriceString);
                JButton delProductButton = new JButton("Del");

                
                // Set the JTextField as non-editable
                productQuantityField.setEditable(false);
                
                productPriceLabel.setHorizontalAlignment(JLabel.RIGHT);
                productQuantityField.setHorizontalAlignment(JTextField.RIGHT);
                productTotalPriceLabel.setHorizontalAlignment(JTextField.RIGHT);
                
                // Add the components to the corresponding panels
                basketNameDetails.add(productNameLabel);
                basketQuantityDetails.add(productQuantityField);
                basketPriceDetails.add(productPriceLabel);
                basketTotalPriceDetails.add(productTotalPriceLabel);
                basketDelProduct.add(delProductButton);

                // Optionally, add mouse listener to the product name label to handle clicks
                delProductButton.addActionListener(evt -> {
                    boolean isDeleted = deleteProductFromBasket(basketId);
                    if (isDeleted) {
                        JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                        // Refresh the basket view
                        ViewDetailsBuyProduct(SessionManager.getUserId());
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete product.");
                    }
                });
            }
            
            // Refresh the panels to display the new components
            String totalPriceDisplay = "Σύνολο: " + String.format("%.2f", totalPriceSum);
            productsPriceLabel.setText(totalPriceDisplay);
            basketNameDetails.revalidate();
            basketNameDetails.repaint();
            basketQuantityDetails.revalidate();
            basketQuantityDetails.repaint();
            basketPriceDetails.revalidate();
            basketPriceDetails.repaint();
            basketTotalPriceDetails.revalidate();
            basketTotalPriceDetails.repaint();
            basketDelProduct.revalidate();
            basketDelProduct.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error displaying basket details: " + e.getMessage());
        }
    }
    
    public boolean deleteProductFromBasket(int basketId) {
    // Call the delProductBasket method to delete the product from the database
        DatabaseConnection dbConnection = new DatabaseConnection();
        DataAccessObject dao = new DataAccessObject(dbConnection);
        basketNameDetails.removeAll();
        basketQuantityDetails.removeAll();
        basketPriceDetails.removeAll();
        basketTotalPriceDetails.removeAll();
        basketDelProduct.removeAll();
        return dao.delProductBasket(basketId);  // returns true if deleted successfully
    }
    
      private void populateBasketStreet(JComboBox<String> comboBox) {  
        comboBox.removeAllItems();
        comboBox.addItem("");
        DatabaseConnection dbConnection = new DatabaseConnection();
        DataAccessObject categoryDAO = new DataAccessObject(dbConnection);
        Map<Integer, String> addressStreet = categoryDAO.getAddressStreet(SessionManager.getUserId());

        for (Map.Entry<Integer, String> entry : addressStreet.entrySet()) {
            comboBox.addItem(entry.getValue());
            comboBox.putClientProperty(entry.getValue(), entry.getKey()); // Store the ID as a property
        }
        dbConnection.closeConnection();
    }

    private double parsePriceFromLabel(String labelText) {
        try {
            // Extract the numeric part from the label text
            String priceString = labelText.replaceAll("[^0-9.]", ""); // Remove non-numeric characters
            return Double.parseDouble(priceString); // Convert to double
        } catch (NumberFormatException e) {
        return 0; // Return 0 if parsing fails
        }
    }
    
    private void calculateTotalPrice(JLabel productsPriceLabel, JLabel transferPriceLabel, JLabel transferMethodPriceLabel) {
        try {
            // Parse and sum the numerical values from all labels
            double productsPrice = parsePriceFromLabel(productsPriceLabel.getText());
            double transferPrice = parsePriceFromLabel(transferPriceLabel.getText());
            double transferMethodPrice = parsePriceFromLabel(transferMethodPriceLabel.getText());
            double totalPrice = productsPrice + transferPrice + transferMethodPrice;
            totalPriceLabel.setText("Σύνολο: " + String.format("%.2f", totalPrice));
        } catch (NumberFormatException e) {
            e.printStackTrace();
             totalPriceLabel.setText("Total Price: Error");
        }
    }
//finish code for buyProduct
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
        BuyButton = new javax.swing.JButton();
        mangamentButton = new javax.swing.JButton();
        userNameCustomer1 = new javax.swing.JLabel();
        logoutButton1 = new javax.swing.JButton();
        Categories1 = new javax.swing.JButton();
        NewFrame = new javax.swing.JPanel();
        emptyNewFrame = new javax.swing.JPanel();
        mainFrameNewProductPanel = new javax.swing.JPanel();
        mainFrameNewPorductLabel = new javax.swing.JLabel();
        Product1 = new javax.swing.JPanel();
        backButton1 = new javax.swing.JButton();
        productBuyButton1 = new javax.swing.JButton();
        productName1 = new javax.swing.JLabel();
        productStock4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        productDescription1 = new javax.swing.JTextArea();
        ProductPrice1 = new javax.swing.JLabel();
        productStock5 = new javax.swing.JLabel();
        productStock6 = new javax.swing.JLabel();
        StockInputTextField1 = new javax.swing.JTextField();
        productStock7 = new javax.swing.JLabel();
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
        StockInputTextField = new javax.swing.JTextField();
        productStock3 = new javax.swing.JLabel();
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
        basketFrame = new javax.swing.JPanel();
        basketNameDetails = new javax.swing.JPanel();
        transferMethodPriceLabel = new javax.swing.JLabel();
        StreetBuyComboBox = new javax.swing.JComboBox<>();
        PayMethodCheckBox = new javax.swing.JCheckBox();
        SubmitBuyButton = new javax.swing.JButton();
        basketLabelName = new javax.swing.JLabel();
        transferPriceLabel = new javax.swing.JLabel();
        totalPriceLabel = new javax.swing.JLabel();
        productsPriceLabel = new javax.swing.JLabel();
        PayMethodCheckBox1 = new javax.swing.JCheckBox();
        basketQuantityDetails = new javax.swing.JPanel();
        basketPriceDetails = new javax.swing.JPanel();
        basketLabel1 = new javax.swing.JLabel();
        basketLabelPrice = new javax.swing.JLabel();
        basketLabelStock1 = new javax.swing.JLabel();
        basketTotalPriceDetails = new javax.swing.JPanel();
        basketLabelTotalPrice = new javax.swing.JLabel();
        basketDelProduct = new javax.swing.JPanel();

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

        BuyButton.setText("Kαλάθι");
        BuyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuyButtonActionPerformed(evt);
            }
        });

        mangamentButton.setText("Management");
        mangamentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mangamentButtonActionPerformed(evt);
            }
        });

        userNameCustomer1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userNameCustomer1.setText("2024-2025");
        userNameCustomer1.setMaximumSize(new java.awt.Dimension(47, 20));
        userNameCustomer1.setMinimumSize(new java.awt.Dimension(47, 20));
        userNameCustomer1.setPreferredSize(new java.awt.Dimension(47, 20));
        userNameCustomer1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                userNameCustomer1AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        logoutButton1.setText("Logout");
        logoutButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButton1ActionPerformed(evt);
            }
        });

        Categories1.setText("Αρχική");
        Categories1.setMaximumSize(new java.awt.Dimension(133, 27));
        Categories1.setMinimumSize(new java.awt.Dimension(133, 27));
        Categories1.setPreferredSize(new java.awt.Dimension(133, 27));
        Categories1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Categories1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MainNavegationPanelLayout = new javax.swing.GroupLayout(MainNavegationPanel);
        MainNavegationPanel.setLayout(MainNavegationPanelLayout);
        MainNavegationPanelLayout.setHorizontalGroup(
            MainNavegationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainNavegationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MainNavegationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainNavegationPanelLayout.createSequentialGroup()
                        .addGroup(MainNavegationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userNameCustomer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(mangamentButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BuyButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(SettingButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(logoutButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainNavegationPanelLayout.createSequentialGroup()
                        .addComponent(userNameCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainNavegationPanelLayout.createSequentialGroup()
                        .addGroup(MainNavegationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Categories, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                            .addComponent(Categories1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        MainNavegationPanelLayout.setVerticalGroup(
            MainNavegationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainNavegationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userNameCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Categories1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Categories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mangamentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BuyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SettingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(logoutButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(userNameCustomer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        NewFrame.setPreferredSize(new java.awt.Dimension(1270, 720));
        NewFrame.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout mainFrameNewProductPanelLayout = new javax.swing.GroupLayout(mainFrameNewProductPanel);
        mainFrameNewProductPanel.setLayout(mainFrameNewProductPanelLayout);
        mainFrameNewProductPanelLayout.setHorizontalGroup(
            mainFrameNewProductPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        mainFrameNewProductPanelLayout.setVerticalGroup(
            mainFrameNewProductPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 427, Short.MAX_VALUE)
        );

        mainFrameNewPorductLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        mainFrameNewPorductLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        mainFrameNewPorductLabel.setText("Νέα Προϊόντα:");
        mainFrameNewPorductLabel.setMaximumSize(new java.awt.Dimension(47, 20));
        mainFrameNewPorductLabel.setMinimumSize(new java.awt.Dimension(47, 20));
        mainFrameNewPorductLabel.setPreferredSize(new java.awt.Dimension(47, 20));
        mainFrameNewPorductLabel.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                mainFrameNewPorductLabelAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout emptyNewFrameLayout = new javax.swing.GroupLayout(emptyNewFrame);
        emptyNewFrame.setLayout(emptyNewFrameLayout);
        emptyNewFrameLayout.setHorizontalGroup(
            emptyNewFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(emptyNewFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(emptyNewFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(emptyNewFrameLayout.createSequentialGroup()
                        .addComponent(mainFrameNewPorductLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 887, Short.MAX_VALUE))
                    .addComponent(mainFrameNewProductPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        emptyNewFrameLayout.setVerticalGroup(
            emptyNewFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(emptyNewFrameLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(mainFrameNewPorductLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainFrameNewProductPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(276, Short.MAX_VALUE))
        );

        NewFrame.add(emptyNewFrame, "cardEmptyNewFrame");

        backButton1.setText("Πίσω");
        backButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButton1ActionPerformed(evt);
            }
        });

        productBuyButton1.setText("Προσθήκη στο καλάθι");
        productBuyButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productBuyButton1ActionPerformed(evt);
            }
        });

        productName1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        productName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productName1.setText("Name");

        productStock4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        productStock4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        productStock4.setText("number");

        productDescription1.setColumns(20);
        productDescription1.setLineWrap(true);
        productDescription1.setRows(5);
        jScrollPane2.setViewportView(productDescription1);

        ProductPrice1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        ProductPrice1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ProductPrice1.setText("number");

        productStock5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        productStock5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productStock5.setText("Τιμή");

        productStock6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        productStock6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productStock6.setText("Απόθεμα:");

        StockInputTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        productStock7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        productStock7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productStock7.setText("Ποσότητα:");

        javax.swing.GroupLayout Product1Layout = new javax.swing.GroupLayout(Product1);
        Product1.setLayout(Product1Layout);
        Product1Layout.setHorizontalGroup(
            Product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Product1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(Product1Layout.createSequentialGroup()
                .addGroup(Product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Product1Layout.createSequentialGroup()
                        .addGroup(Product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Product1Layout.createSequentialGroup()
                                .addGap(215, 215, 215)
                                .addGroup(Product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(productStock5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(productStock7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(productStock6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(Product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(productStock4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ProductPrice1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(StockInputTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(productName1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(Product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(productBuyButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE))
                .addGap(62, 62, 62))
        );
        Product1Layout.setVerticalGroup(
            Product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Product1Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(Product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Product1Layout.createSequentialGroup()
                        .addComponent(productName1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(Product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(productStock6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(productStock4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(Product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(productStock5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ProductPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(Product1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addGroup(Product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(productBuyButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(StockInputTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(productStock7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 332, Short.MAX_VALUE)
                .addComponent(backButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        NewFrame.add(Product1, "cardBuyProduct1");

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        NewFrame2.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout emptyNewFrame2Layout = new javax.swing.GroupLayout(emptyNewFrame2);
        emptyNewFrame2.setLayout(emptyNewFrame2Layout);
        emptyNewFrame2Layout.setHorizontalGroup(
            emptyNewFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 919, Short.MAX_VALUE)
        );
        emptyNewFrame2Layout.setVerticalGroup(
            emptyNewFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );

        NewFrame2.add(emptyNewFrame2, "card3");

        NavigationSubCategories.setBackground(new java.awt.Color(0, 102, 102));
        NavigationSubCategories.setPreferredSize(new java.awt.Dimension(150, 48));

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
                        .addGap(0, 16, Short.MAX_VALUE))
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
            .addGap(0, 757, Short.MAX_VALUE)
        );
        emptyNewFrame3Layout.setVerticalGroup(
            emptyNewFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 748, Short.MAX_VALUE)
        );

        NewFrame3.add(emptyNewFrame3, "card2");

        backButton.setText("Πίσω");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        productBuyButton.setText("Προσθήκη στο καλάθι");
        productBuyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productBuyButtonActionPerformed(evt);
            }
        });

        productName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        productName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productName.setText("Name");

        productStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        productStock.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        productStock.setText("number");

        productDescription.setColumns(20);
        productDescription.setLineWrap(true);
        productDescription.setRows(5);
        jScrollPane1.setViewportView(productDescription);

        ProductPrice.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        ProductPrice.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ProductPrice.setText("number");

        productStock1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        productStock1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productStock1.setText("Τιμή");

        productStock2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        productStock2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productStock2.setText("Απόθεμα:");

        StockInputTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        productStock3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        productStock3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productStock3.setText("Ποσότητα:");

        javax.swing.GroupLayout ProductLayout = new javax.swing.GroupLayout(Product);
        Product.setLayout(ProductLayout);
        ProductLayout.setHorizontalGroup(
            ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ProductLayout.createSequentialGroup()
                .addGroup(ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ProductLayout.createSequentialGroup()
                        .addGroup(ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ProductLayout.createSequentialGroup()
                                .addGap(215, 215, 215)
                                .addGroup(ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(productStock1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(productStock3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(productStock2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(productStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ProductPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(StockInputTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(productName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(productBuyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE))
                .addGap(62, 62, 62))
        );
        ProductLayout.setVerticalGroup(
            ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductLayout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ProductLayout.createSequentialGroup()
                        .addComponent(productName, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(productStock2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(productStock, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(productStock1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ProductPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(ProductLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addGroup(ProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(productBuyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(StockInputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(productStock3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 308, Short.MAX_VALUE)
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        NewFrame3.add(Product, "cardBuyProduct");

        javax.swing.GroupLayout ViewProductLayout = new javax.swing.GroupLayout(ViewProduct);
        ViewProduct.setLayout(ViewProductLayout);
        ViewProductLayout.setHorizontalGroup(
            ViewProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 745, Short.MAX_VALUE)
        );
        ViewProductLayout.setVerticalGroup(
            ViewProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 736, Short.MAX_VALUE)
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
                .addComponent(NavigationSubCategories, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewFrame3, javax.swing.GroupLayout.PREFERRED_SIZE, 757, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        ViewSubCategoriesLayout.setVerticalGroup(
            ViewSubCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(NavigationSubCategories, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
            .addGroup(ViewSubCategoriesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NewFrame3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        NewFrame2.add(ViewSubCategories, "cardViewSubCategories");

        javax.swing.GroupLayout ViewCategoriesFrameLayout = new javax.swing.GroupLayout(ViewCategoriesFrame);
        ViewCategoriesFrame.setLayout(ViewCategoriesFrameLayout);
        ViewCategoriesFrameLayout.setHorizontalGroup(
            ViewCategoriesFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewCategoriesFrameLayout.createSequentialGroup()
                .addComponent(NavigationCategories, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewFrame2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        ViewCategoriesFrameLayout.setVerticalGroup(
            ViewCategoriesFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(NavigationCategories, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(ViewCategoriesFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NewFrame2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
            .addGap(0, 961, Short.MAX_VALUE)
        );
        emptyLayout.setVerticalGroup(
            emptyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(342, Short.MAX_VALUE))
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
                .addContainerGap(646, Short.MAX_VALUE))
        );
        profileLayout.setVerticalGroup(
            profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profileLayout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(addressNew, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(585, Short.MAX_VALUE))
        );

        SettingsNewFrame.add(profile, "cardProfile");

        javax.swing.GroupLayout SettingsFrameLayout = new javax.swing.GroupLayout(SettingsFrame);
        SettingsFrame.setLayout(SettingsFrameLayout);
        SettingsFrameLayout.setHorizontalGroup(
            SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SettingsFrameLayout.createSequentialGroup()
                .addComponent(SubNavigationBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(SettingsNewFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 961, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        SettingsFrameLayout.setVerticalGroup(
            SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SubNavigationBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(SettingsFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SettingsNewFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        NewFrame.add(SettingsFrame, "cardSettings");

        javax.swing.GroupLayout basketNameDetailsLayout = new javax.swing.GroupLayout(basketNameDetails);
        basketNameDetails.setLayout(basketNameDetailsLayout);
        basketNameDetailsLayout.setHorizontalGroup(
            basketNameDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        basketNameDetailsLayout.setVerticalGroup(
            basketNameDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        transferMethodPriceLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        transferMethodPriceLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        transferMethodPriceLabel.setText("Αντικαταβολή:");

        StreetBuyComboBox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        StreetBuyComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        StreetBuyComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StreetBuyComboBoxActionPerformed(evt);
            }
        });

        PayMethodCheckBox.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        PayMethodCheckBox.setText("Αντικαταβολή");
        PayMethodCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PayMethodCheckBoxActionPerformed(evt);
            }
        });

        SubmitBuyButton.setText("Ολοκλήρωση");
        SubmitBuyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubmitBuyButtonActionPerformed(evt);
            }
        });

        basketLabelName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        basketLabelName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        basketLabelName.setText("Όνομα προϊόντος:");

        transferPriceLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        transferPriceLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        transferPriceLabel.setText("Μεταφορικα:");

        totalPriceLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        totalPriceLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        totalPriceLabel.setText("Σύνολο:");

        productsPriceLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        productsPriceLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        productsPriceLabel.setText("Αξία Προϊόντων:");

        PayMethodCheckBox1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        PayMethodCheckBox1.setText("Mastercard");

        basketQuantityDetails.setAutoscrolls(true);
        basketQuantityDetails.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        basketQuantityDetails.setName(""); // NOI18N

        javax.swing.GroupLayout basketQuantityDetailsLayout = new javax.swing.GroupLayout(basketQuantityDetails);
        basketQuantityDetails.setLayout(basketQuantityDetailsLayout);
        basketQuantityDetailsLayout.setHorizontalGroup(
            basketQuantityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        basketQuantityDetailsLayout.setVerticalGroup(
            basketQuantityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout basketPriceDetailsLayout = new javax.swing.GroupLayout(basketPriceDetails);
        basketPriceDetails.setLayout(basketPriceDetailsLayout);
        basketPriceDetailsLayout.setHorizontalGroup(
            basketPriceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        basketPriceDetailsLayout.setVerticalGroup(
            basketPriceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        basketLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        basketLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        basketLabel1.setText("Το καλάθι σου");

        basketLabelPrice.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        basketLabelPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        basketLabelPrice.setText("Τιμή μονάδας:");

        basketLabelStock1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        basketLabelStock1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        basketLabelStock1.setText("Ποσότητα:");

        javax.swing.GroupLayout basketTotalPriceDetailsLayout = new javax.swing.GroupLayout(basketTotalPriceDetails);
        basketTotalPriceDetails.setLayout(basketTotalPriceDetailsLayout);
        basketTotalPriceDetailsLayout.setHorizontalGroup(
            basketTotalPriceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        basketTotalPriceDetailsLayout.setVerticalGroup(
            basketTotalPriceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        basketLabelTotalPrice.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        basketLabelTotalPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        basketLabelTotalPrice.setText("Σύνολο:");

        javax.swing.GroupLayout basketDelProductLayout = new javax.swing.GroupLayout(basketDelProduct);
        basketDelProduct.setLayout(basketDelProductLayout);
        basketDelProductLayout.setHorizontalGroup(
            basketDelProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        basketDelProductLayout.setVerticalGroup(
            basketDelProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout basketFrameLayout = new javax.swing.GroupLayout(basketFrame);
        basketFrame.setLayout(basketFrameLayout);
        basketFrameLayout.setHorizontalGroup(
            basketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basketFrameLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(basketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(basketFrameLayout.createSequentialGroup()
                        .addGroup(basketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(basketNameDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(basketLabelName, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(basketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(basketQuantityDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(basketLabelStock1, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(basketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(basketPriceDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(basketLabelPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(basketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(basketLabelTotalPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(basketTotalPriceDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(basketDelProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(basketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(basketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(transferMethodPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, basketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(transferPriceLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(productsPriceLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(PayMethodCheckBox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(PayMethodCheckBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                                    .addComponent(StreetBuyComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(totalPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(SubmitBuyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16))
                    .addGroup(basketFrameLayout.createSequentialGroup()
                        .addComponent(basketLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 658, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        basketFrameLayout.setVerticalGroup(
            basketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basketFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(basketLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addGroup(basketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(basketLabelTotalPrice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(basketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(basketLabelName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(basketLabelStock1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(basketLabelPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(basketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(basketDelProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(basketFrameLayout.createSequentialGroup()
                        .addComponent(StreetBuyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(78, 78, 78)
                        .addComponent(PayMethodCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(PayMethodCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(productsPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(transferPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(transferMethodPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(totalPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(SubmitBuyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(basketTotalPriceDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(basketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(basketNameDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(basketQuantityDetails, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(basketPriceDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        NewFrame.add(basketFrame, "cardBasket");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MainNavegationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 1111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainNavegationPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NewFrame, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
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

    private void BuyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuyButtonActionPerformed
        basketNameDetails.removeAll();
        basketQuantityDetails.removeAll();
        basketPriceDetails.removeAll();
        basketTotalPriceDetails.removeAll();
        basketDelProduct.removeAll();
        transferPriceLabel.setText("0");
        populateBasketStreet(StreetBuyComboBox);
        Integer userId = SessionManager.getUserId();
        ViewDetailsBuyProduct(userId);        
        cardLayout.show(NewFrame,"cardBasket");
        
    }//GEN-LAST:event_BuyButtonActionPerformed

    private void SubmitBuyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubmitBuyButtonActionPerformed
        Object selectedStreet = StreetBuyComboBox.getSelectedItem();
        boolean isStreetValid = selectedStreet != null && !selectedStreet.toString().trim().isEmpty();

        boolean isPayMethodChecked = PayMethodCheckBox.isSelected();

        if (!isStreetValid) {
           JOptionPane.showMessageDialog(null, "Παρακαλώ επιλέξτε διεύθυνση!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (!isPayMethodChecked) {
           JOptionPane.showMessageDialog(null, "Παρακαλώ επιλέξτε τρόπο πληρωμής!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JOptionPane.showMessageDialog(null, "Η παραγγελία Ολοκληρώθηκε:");

        Integer userId = SessionManager.getUserId();
        DatabaseConnection dbConnection = new DatabaseConnection();
        DataAccessObject dao = new DataAccessObject(dbConnection);
        dao.delUserBasket(userId);

        // Clear basket details
        basketNameDetails.removeAll();
        basketQuantityDetails.removeAll();
        basketPriceDetails.removeAll();
        basketTotalPriceDetails.removeAll();
        basketDelProduct.removeAll();
        cardLayout.show(NewFrame,"cardEmptyNewFrame");
        // Refresh basket view
        ViewDetailsBuyProduct(userId);
    }//GEN-LAST:event_SubmitBuyButtonActionPerformed

    private void productBuyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productBuyButtonActionPerformed
        String name = (String) productName.getText();
        String quantity = (String) StockInputTextField.getText();
        String price = (String) ProductPrice.getText();
        
        Integer userId = SessionManager.getUserId();
        Integer productId = (Integer) productName.getClientProperty("productId");
        
        Integer integerQuantity;
        double doublePrice = Double.parseDouble(price);

        // Validate that the fields contain only numeric values
        if (!quantity.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Παρακαλώ εισάγετε έγκυρο αριθμό προϊόντων.");
            return;  // Exit the method if the input is invalid
        }
        
        try {
            integerQuantity = Integer.parseInt(quantity);
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
            dao.insertProductToBasket(userId, productId, name, integerQuantity, doublePrice);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την προσθήκη του προφίλ: " + e.getMessage());
        }
        cardProducts.show(NewFrame3, "cardViewProducts");
    }//GEN-LAST:event_productBuyButtonActionPerformed

    private void userNameCustomer1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_userNameCustomer1AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_userNameCustomer1AncestorAdded
    
    private void mangamentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mangamentButtonActionPerformed
        ManagementView managment = new ManagementView();
        managment.setVisible(true);
        
        this.dispose();
    }//GEN-LAST:event_mangamentButtonActionPerformed

    private void logoutButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButton1ActionPerformed
        SessionManager.endSession();
        Login login = new Login();
        login.setVisible(true);
    
        // Close the current frame
        this.dispose();
    }//GEN-LAST:event_logoutButton1ActionPerformed

    private void StreetBuyComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StreetBuyComboBoxActionPerformed
        Object selectedItem = StreetBuyComboBox.getSelectedItem();
        if (selectedItem == null || selectedItem.toString().trim().isEmpty()) {
            transferPriceLabel.setText("Μεταφορικά: "); // Set the default text
        } else {
            double price = 3; // The fixed price to display
            String PriceDisplay = "Μεταφορικά: " + String.format("%.2f", price); // Format the price
            transferPriceLabel.setText(PriceDisplay); // Update the label with the formatted price
            calculateTotalPrice(productsPriceLabel, transferPriceLabel, transferMethodPriceLabel);
    }
    }//GEN-LAST:event_StreetBuyComboBoxActionPerformed

    private void mainFrameNewPorductLabelAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_mainFrameNewPorductLabelAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_mainFrameNewPorductLabelAncestorAdded

    private void Categories1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Categories1ActionPerformed
        cardLayout.show(NewFrame,"cardEmptyNewFrame");
    }//GEN-LAST:event_Categories1ActionPerformed

    private void PayMethodCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PayMethodCheckBoxActionPerformed
        if (PayMethodCheckBox.isSelected()) { // Check if the checkbox is selected
            double price = 3; // The fixed price
            String PriceDisplay = "Αντικαταβολή: " + String.format("%.2f", price); // Format the price
            transferMethodPriceLabel.setText(PriceDisplay); // Update the checkbox text
            calculateTotalPrice(productsPriceLabel, transferPriceLabel, transferMethodPriceLabel);
        } else {
            transferMethodPriceLabel.setText("Αντικαταβολή: "); // Set a different text if not selected
    }
    }//GEN-LAST:event_PayMethodCheckBoxActionPerformed

    private void backButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButton1ActionPerformed
        cardLayout.show(NewFrame,"cardEmptyNewFrame");
    }//GEN-LAST:event_backButton1ActionPerformed

    private void productBuyButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productBuyButton1ActionPerformed
        String name = (String) productName1.getText();
        String quantity = (String) StockInputTextField1.getText();
        String price = (String) ProductPrice1.getText();
        String dataQuantity = (String) productStock4.getText();
        
        int userId = SessionManager.getUserId();
        int productId = (int) productName1.getClientProperty("productId");
        
        int integerQuantity;
        int availableQuantity;
        double doublePrice = Double.parseDouble(price);

        // Validate that the fields contain only numeric values
        if (!quantity.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Παρακαλώ εισάγετε έγκυρο αριθμό προϊόντων.");
            return;  // Exit the method if the input is invalid
        }
        
        try {
            integerQuantity = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            // Print error details for debugging
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace(); // Log the error for debugging
            return;  // Exit the method if the parsing fails
        }
        
        try {
            availableQuantity = Integer.parseInt(dataQuantity);
        } catch (NumberFormatException e) {
            // Handle invalid stock data
            JOptionPane.showMessageDialog(null, "Σφάλμα στα δεδομένα του αποθέματος.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Log the error for debugging purposes
            return; // Exit the method if parsing fails
        }
        
        if (integerQuantity > availableQuantity) {
        JOptionPane.showMessageDialog(null, "Η ποσότητα που ζητήσατε υπερβαίνει το διαθέσιμο απόθεμα.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
        }
        try {
            // Create DAO and insert product
            DatabaseConnection dbConnection = new DatabaseConnection();
            DataAccessObject dao = new DataAccessObject(dbConnection);
            dao.insertProductToBasket(userId, productId, name, integerQuantity, doublePrice);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την προσθήκη του προφίλ: " + e.getMessage());
        }
        cardLayout.show(NewFrame, "cardEmptyNewFrame");
    }//GEN-LAST:event_productBuyButton1ActionPerformed

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
    private javax.swing.JButton BuyButton;
    private javax.swing.JButton Categories;
    private javax.swing.JButton Categories1;
    private javax.swing.JPanel MainNavegationPanel;
    private javax.swing.JPanel NavigationCategories;
    private javax.swing.JLabel NavigationCateogriesLabel;
    private javax.swing.JPanel NavigationSubCategories;
    private javax.swing.JLabel NavigationSubCategoriesLabel;
    private javax.swing.JPanel NewFrame;
    private javax.swing.JPanel NewFrame2;
    private javax.swing.JPanel NewFrame3;
    private javax.swing.JCheckBox PayMethodCheckBox;
    private javax.swing.JCheckBox PayMethodCheckBox1;
    private javax.swing.JPanel Product;
    private javax.swing.JPanel Product1;
    private javax.swing.JLabel ProductPrice;
    private javax.swing.JLabel ProductPrice1;
    private javax.swing.JButton SettingButton;
    private javax.swing.JPanel SettingsFrame;
    private javax.swing.JPanel SettingsNewFrame;
    private javax.swing.JTextField StockInputTextField;
    private javax.swing.JTextField StockInputTextField1;
    private javax.swing.JComboBox<String> StreetBuyComboBox;
    private javax.swing.JPanel SubNavigationBar;
    private javax.swing.JLabel SubNavigationLabel;
    private javax.swing.JButton SubmitBuyButton;
    private javax.swing.JPanel ViewCategoriesFrame;
    private javax.swing.JPanel ViewProduct;
    private javax.swing.JPanel ViewProductFrame;
    private javax.swing.JPanel ViewSubCategories;
    private javax.swing.JPanel addAddress;
    private javax.swing.JButton addressNew;
    private javax.swing.JButton backButton;
    private javax.swing.JButton backButton1;
    private javax.swing.JPanel barMain2;
    private javax.swing.JPanel basketDelProduct;
    private javax.swing.JPanel basketFrame;
    private javax.swing.JLabel basketLabel1;
    private javax.swing.JLabel basketLabelName;
    private javax.swing.JLabel basketLabelPrice;
    private javax.swing.JLabel basketLabelStock1;
    private javax.swing.JLabel basketLabelTotalPrice;
    private javax.swing.JPanel basketNameDetails;
    private javax.swing.JPanel basketPriceDetails;
    private javax.swing.JPanel basketQuantityDetails;
    private javax.swing.JPanel basketTotalPriceDetails;
    private javax.swing.JPanel categoriesButtons;
    private javax.swing.JPanel editAddress;
    private javax.swing.JPanel empty;
    private javax.swing.JPanel emptyNewFrame;
    private javax.swing.JPanel emptyNewFrame2;
    private javax.swing.JPanel emptyNewFrame3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton logoutButton1;
    private javax.swing.JLabel mainFrameNewPorductLabel;
    private javax.swing.JPanel mainFrameNewProductPanel;
    private javax.swing.JButton mangamentButton;
    private javax.swing.JButton productBuyButton;
    private javax.swing.JButton productBuyButton1;
    private javax.swing.JTextArea productDescription;
    private javax.swing.JTextArea productDescription1;
    private javax.swing.JLabel productName;
    private javax.swing.JLabel productName1;
    private javax.swing.JLabel productStock;
    private javax.swing.JLabel productStock1;
    private javax.swing.JLabel productStock2;
    private javax.swing.JLabel productStock3;
    private javax.swing.JLabel productStock4;
    private javax.swing.JLabel productStock5;
    private javax.swing.JLabel productStock6;
    private javax.swing.JLabel productStock7;
    private javax.swing.JLabel productsPriceLabel;
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
    private javax.swing.JLabel totalPriceLabel;
    private javax.swing.JLabel transferMethodPriceLabel;
    private javax.swing.JLabel transferPriceLabel;
    private javax.swing.JLabel userNameCustomer;
    private javax.swing.JLabel userNameCustomer1;
    // End of variables declaration//GEN-END:variables
}
