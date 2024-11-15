/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package eShopPackage;

import java.awt.CardLayout;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author xrist
 */
public class CustomerView extends javax.swing.JFrame {
    CardLayout cardSetting,cardLayout;
    /**
     * Creates new form CustomerView
     */
    public CustomerView() {
        initComponents();
        String username = SessionManager.getUsername();
        userNameCustomer.setText(" " + username);
        cardLayout = (CardLayout)(NewFrame.getLayout());
        cardSetting = (CardLayout)(NewFrame2.getLayout());
    }

    private void createProfileButtons(Map<String, String> profileData) {   
        JPanel cardProfile = (JPanel) NewFrame2.getComponent(NewFrame2.getComponentCount() - 1);
        System.out.println("Profile data size: " + profileData.size());
        cardProfile.setLayout(new BoxLayout(cardProfile, BoxLayout.Y_AXIS));
        
        // Loop through the profile data and create a button for each piece of data
        for (Map.Entry<String, String> entry : profileData.entrySet()) {
            String profileId = entry.getKey();
            String profileStreet = entry.getValue(); 

            System.out.println("Creating button for: " + profileId + " with value: " + profileStreet);
            JButton button = new JButton(" " + profileStreet);

            // Add action listener to load data into the addaddress card
            button.addActionListener(e -> {
                cardSetting.show(NewFrame2, "cardAddAddress");  // Switch to the "addaddress" card
            });
            // Add the button to the panel
            cardProfile.add(button);
        }
        // Add the button panel to the frame (assuming you have a layout for it)
        
        cardProfile.revalidate();
        cardProfile.repaint();
    }
    
    private void loadDataIntoAddressCard(String key, String value) {
        switch (key) {
            case "name":
                profileNameField.setText(value);
                break;
            case "surname":
                profileSurnameField.setText(value);
                break;
            case "street":
                profileStreetField.setText(value);
                break;
            case "streetnumber":
                profileStreetNumberField.setText(value);
                break;
            case "city":
                profileCityField.setText(value);
                break;
            case "postcode":
                profileTKField.setText(value);
                break;
            case "phone":
                profilePhoneField.setText(value);
                break;
            case "cellphone":
                profilePhone1Field.setText(value);
                break;
            default:
                // Handle unexpected fields
                break;
        }
       
    }
    
private void loadCategories() {
    cardCategories.removeAll(); // Καθαρισμός των υπαρχόντων components

    // Δημιουργία αντικειμένου DataAccessObject
    DataAccessObject dao = new DataAccessObject();
    Map<Integer, String> categories = dao.getCategoryNames(); // Λήψη κατηγοριών από τη βάση

    for (Map.Entry<Integer, String> entry : categories.entrySet()) {
        int categoryId = entry.getKey(); // ID κατηγορίας
        String categoryName = entry.getValue(); // Όνομα κατηγορίας

        // Δημιουργία κουμπιού για την κατηγορία
        JButton categoryButton = new JButton(categoryName);

        // Προσθήκη ActionListener για εμφάνιση υποκατηγοριών
        categoryButton.addActionListener(e -> showSubcategories(categoryId));

        // Προσθήκη του κουμπιού στο panel
        cardCategories.add(categoryButton);
    }

    // Ανανέωση του panel για να εμφανιστούν οι αλλαγές
    cardCategories.revalidate();
    cardCategories.repaint();
}

private void showSubcategories(int categoryId) {
    cardSubcategories.removeAll(); // Καθαρισμός των υπαρχόντων components

    // Δημιουργία αντικειμένου DataAccessObject
    DataAccessObject dao = new DataAccessObject();

    // Λήψη υποκατηγοριών για το συγκεκριμένο categoryId
    Map<Integer, String> subcategories = dao.getSubCategoryNames(categoryId);

    // Δημιουργία κουμπιών για κάθε υποκατηγορία
    for (Map.Entry<Integer, String> entry : subcategories.entrySet()) {
        int subcategoryId = entry.getKey(); // ID υποκατηγορίας
        String subcategoryName = entry.getValue(); // Όνομα υποκατηγορίας

        // Δημιουργία κουμπιού
        JButton subcategoryButton = new JButton(subcategoryName);

        // Προσθήκη ActionListener για φόρτωση προϊόντων
        subcategoryButton.addActionListener(e -> showProducts(subcategoryId));

        // Προσθήκη κουμπιού στο panel
        cardSubcategories.add(subcategoryButton);
    }

    // Ανανέωση του panel για εμφάνιση των αλλαγών
    cardSubcategories.revalidate();
    cardSubcategories.repaint();

    // Εναλλαγή στο panel των υποκατηγοριών μέσω CardLayout
    CardLayout layout = (CardLayout) categoriesPanel.getLayout();
    layout.show(categoriesPanel, "cardSubcategories");
}

private void showProducts(int subcategoryId) {
    cardProducts.removeAll(); // Καθαρισμός των υπαρχόντων components

    // Δημιουργία αντικειμένου DataAccessObject
    DataAccessObject dao = new DataAccessObject();

    // Λήψη προϊόντων για το συγκεκριμένο subcategoryId
    Map<Integer, String> products = dao.getProducts(subcategoryId);

    // Δημιουργία κουμπιών για κάθε προϊόν
    for (Map.Entry<Integer, String> entry : products.entrySet()) {
        int productId = entry.getKey(); // ID προϊόντος
        String productName = entry.getValue(); // Όνομα προϊόντος

        // Δημιουργία κουμπιού
        JButton productButton = new JButton(productName);

        // Προσθήκη ActionListener για εμφάνιση λεπτομερειών προϊόντος
        productButton.addActionListener(e -> showProductDetails(productId));

        // Προσθήκη κουμπιού στο panel
        cardProducts.add(productButton);
    }

    // Ανανέωση του panel για εμφάνιση των αλλαγών
    cardProducts.revalidate();
    cardProducts.repaint();

    // Εναλλαγή στο panel των προϊόντων μέσω CardLayout
    CardLayout layout = (CardLayout) categoriesPanel.getLayout();
    layout.show(categoriesPanel, "cardProducts");
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
        jButton2 = new javax.swing.JButton();
        SettingButton = new javax.swing.JButton();
        NewFrame = new javax.swing.JPanel();
        emptyNewFrame = new javax.swing.JPanel();
        SettingsFrame = new javax.swing.JPanel();
        SubNavigationBar = new javax.swing.JPanel();
        SubNavigationLabel = new javax.swing.JLabel();
        barMain2 = new javax.swing.JPanel();
        settingsAddressButton = new javax.swing.JButton();
        NewFrame2 = new javax.swing.JPanel();
        empty = new javax.swing.JPanel();
        addAddress = new javax.swing.JPanel();
        profileStreetLabel = new javax.swing.JLabel();
        profileSurnameLabel = new javax.swing.JLabel();
        profileSurnameField = new javax.swing.JTextField();
        profileNameField = new javax.swing.JTextField();
        profileNameLabel = new javax.swing.JLabel();
        profileStreetNumberField = new javax.swing.JTextField();
        profileStreetNumberLabel = new javax.swing.JLabel();
        profileStreetField = new javax.swing.JTextField();
        profilePhoneLabel = new javax.swing.JLabel();
        profileTKField = new javax.swing.JTextField();
        profileTKLabel = new javax.swing.JLabel();
        profileCityField = new javax.swing.JTextField();
        profileCityLabel = new javax.swing.JLabel();
        profilePhoneField = new javax.swing.JTextField();
        profilePhone1Label = new javax.swing.JLabel();
        profilePhone1Field = new javax.swing.JTextField();
        profileSaveButton = new javax.swing.JButton();
        profile = new javax.swing.JPanel();
        addressNew = new javax.swing.JButton();
        categoriesPanel = new javax.swing.JPanel();
        cardCategories = new javax.swing.JPanel();
        cardSubcategories = new javax.swing.JPanel();
        cardProducts = new javax.swing.JPanel();

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

        jButton2.setText("jButton2");

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
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(SettingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        NewFrame.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout emptyNewFrameLayout = new javax.swing.GroupLayout(emptyNewFrame);
        emptyNewFrame.setLayout(emptyNewFrameLayout);
        emptyNewFrameLayout.setHorizontalGroup(
            emptyNewFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1117, Short.MAX_VALUE)
        );
        emptyNewFrameLayout.setVerticalGroup(
            emptyNewFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 714, Short.MAX_VALUE)
        );

        NewFrame.add(emptyNewFrame, "card2");

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

        NewFrame2.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout emptyLayout = new javax.swing.GroupLayout(empty);
        empty.setLayout(emptyLayout);
        emptyLayout.setHorizontalGroup(
            emptyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 970, Short.MAX_VALUE)
        );
        emptyLayout.setVerticalGroup(
            emptyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 708, Short.MAX_VALUE)
        );

        NewFrame2.add(empty, "cardNewEmpty");

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

        profileSaveButton.setText("Αποθήκευση");

        javax.swing.GroupLayout addAddressLayout = new javax.swing.GroupLayout(addAddress);
        addAddress.setLayout(addAddressLayout);
        addAddressLayout.setHorizontalGroup(
            addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addAddressLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(profilePhoneLabel)
                    .addComponent(profileStreetLabel)
                    .addComponent(profileNameLabel)
                    .addComponent(profileCityLabel))
                .addGap(18, 18, 18)
                .addGroup(addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addAddressLayout.createSequentialGroup()
                        .addComponent(profilePhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(profilePhone1Label))
                    .addGroup(addAddressLayout.createSequentialGroup()
                        .addComponent(profileCityField, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(profileTKLabel))
                    .addGroup(addAddressLayout.createSequentialGroup()
                        .addComponent(profileNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(profileSurnameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(addAddressLayout.createSequentialGroup()
                        .addComponent(profileStreetField, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(profileStreetNumberLabel)))
                .addGap(18, 18, 18)
                .addGroup(addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(profileSurnameField)
                    .addComponent(profileStreetNumberField, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(profileTKField, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(profilePhone1Field, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(profileSaveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(269, 269, 269))
        );
        addAddressLayout.setVerticalGroup(
            addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addAddressLayout.createSequentialGroup()
                .addGap(158, 158, 158)
                .addGroup(addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profileSurnameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileSurnameField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profileStreetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileStreetNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileStreetNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileStreetField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profileCityField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileTKLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileTKField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileCityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profilePhoneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profilePhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profilePhone1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profilePhone1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addComponent(profileSaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(290, Short.MAX_VALUE))
        );

        NewFrame2.add(addAddress, "cardAddAddress");

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
                .addContainerGap(533, Short.MAX_VALUE))
        );

        NewFrame2.add(profile, "cardProfile");

        javax.swing.GroupLayout SettingsFrameLayout = new javax.swing.GroupLayout(SettingsFrame);
        SettingsFrame.setLayout(SettingsFrameLayout);
        SettingsFrameLayout.setHorizontalGroup(
            SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SettingsFrameLayout.createSequentialGroup()
                .addComponent(SubNavigationBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(NewFrame2, javax.swing.GroupLayout.PREFERRED_SIZE, 970, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        SettingsFrameLayout.setVerticalGroup(
            SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SettingsFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SubNavigationBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NewFrame2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        NewFrame.add(SettingsFrame, "cardSettings");

        categoriesPanel.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout cardCategoriesLayout = new javax.swing.GroupLayout(cardCategories);
        cardCategories.setLayout(cardCategoriesLayout);
        cardCategoriesLayout.setHorizontalGroup(
            cardCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        cardCategoriesLayout.setVerticalGroup(
            cardCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        categoriesPanel.add(cardCategories, "card2");

        javax.swing.GroupLayout cardSubcategoriesLayout = new javax.swing.GroupLayout(cardSubcategories);
        cardSubcategories.setLayout(cardSubcategoriesLayout);
        cardSubcategoriesLayout.setHorizontalGroup(
            cardSubcategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        cardSubcategoriesLayout.setVerticalGroup(
            cardSubcategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        categoriesPanel.add(cardSubcategories, "card3");

        javax.swing.GroupLayout cardProductsLayout = new javax.swing.GroupLayout(cardProducts);
        cardProducts.setLayout(cardProductsLayout);
        cardProductsLayout.setHorizontalGroup(
            cardProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        cardProductsLayout.setVerticalGroup(
            cardProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        categoriesPanel.add(cardProducts, "card4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MainNavegationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NewFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 1117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(categoriesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainNavegationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(NewFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(categoriesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
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
        JPanel cardProfile = (JPanel) NewFrame2.getComponent(NewFrame2.getComponentCount() - 1);
        cardProfile.removeAll();
        DataAccessObject dao = new DataAccessObject(new DatabaseConnection());
        Map<String, String> profileData = dao.getUserProfile(SessionManager.getUserId());
        if (profileData != null && !profileData.isEmpty()) {
            // Dynamically create buttons based on the profile data
             System.out.println(profileData);
            createProfileButtons(profileData);
        } else {
            System.out.println("No profile data found.");
        }
        cardSetting.show(NewFrame2,"cardProfile");
    }//GEN-LAST:event_settingsAddressButtonActionPerformed

    private void addressNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressNewActionPerformed
        cardSetting.show(NewFrame2,"cardAddAddress");
    }//GEN-LAST:event_addressNewActionPerformed

    private void CategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CategoriesActionPerformed
        cardLayout.show(NewFrame, "categoriesPanel");
        loadCategories();
    }//GEN-LAST:event_CategoriesActionPerformed

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
    private javax.swing.JPanel NewFrame;
    private javax.swing.JPanel NewFrame2;
    private javax.swing.JButton SettingButton;
    private javax.swing.JPanel SettingsFrame;
    private javax.swing.JPanel SubNavigationBar;
    private javax.swing.JLabel SubNavigationLabel;
    private javax.swing.JPanel addAddress;
    private javax.swing.JButton addressNew;
    private javax.swing.JPanel barMain2;
    private javax.swing.JPanel cardCategories;
    private javax.swing.JPanel cardProducts;
    private javax.swing.JPanel cardSubcategories;
    private javax.swing.JPanel categoriesPanel;
    private javax.swing.JPanel empty;
    private javax.swing.JPanel emptyNewFrame;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel profile;
    private javax.swing.JTextField profileCityField;
    private javax.swing.JLabel profileCityLabel;
    private javax.swing.JTextField profileNameField;
    private javax.swing.JLabel profileNameLabel;
    private javax.swing.JTextField profilePhone1Field;
    private javax.swing.JLabel profilePhone1Label;
    private javax.swing.JTextField profilePhoneField;
    private javax.swing.JLabel profilePhoneLabel;
    private javax.swing.JButton profileSaveButton;
    private javax.swing.JTextField profileStreetField;
    private javax.swing.JLabel profileStreetLabel;
    private javax.swing.JTextField profileStreetNumberField;
    private javax.swing.JLabel profileStreetNumberLabel;
    private javax.swing.JTextField profileSurnameField;
    private javax.swing.JLabel profileSurnameLabel;
    private javax.swing.JTextField profileTKField;
    private javax.swing.JLabel profileTKLabel;
    private javax.swing.JButton settingsAddressButton;
    private javax.swing.JLabel userNameCustomer;
    // End of variables declaration//GEN-END:variables
}
