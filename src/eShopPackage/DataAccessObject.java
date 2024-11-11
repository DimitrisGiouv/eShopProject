
package eShopPackage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class DataAccessObject {
   
    private final DatabaseConnection dbConnection;
 
    public class Product {
        private String name;
        private int price;
        private int stock;

        public Product(String name, int price, int stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        // Getters for name, price, and stock
        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }

        public int getStock() {
            return stock;
        }
    }

    public DataAccessObject(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public Map<Integer, String> getCategoryNames() {
        Map<Integer, String> categories = new HashMap<>();
        String query = "SELECT category_id, name FROM categories";  // Adjust the table and column names as needed

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("category_id");
                String name = resultSet.getString("name");
                categories.put(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching categories: " + e.getMessage());

        }

        return categories;
    }
    
    public Map<Integer, String> getSubCategoryNames(int categoryId) {
        Map<Integer, String> subCategories = new HashMap<>();
        String query = "SELECT subcategory_id, name FROM subcategories WHERE category_id = ?";  // Adjust the table and column names as needed

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, categoryId); // Set the category ID parameter
                ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("subcategory_id");
                String name = resultSet.getString("name");
                subCategories.put(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching subcategories: " + e.getMessage());

        }

        return subCategories;
  }
  
    public Map<Integer, String> getProductNames(int subCategoryId) {
        Map<Integer, String> products = new HashMap<>();
        String query = "Select product_id, name FROM products WHERE subcategory_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, subCategoryId);
                ResultSet resultSet = statement.executeQuery();
                
            while (resultSet.next()) {
                int id = resultSet.getInt("product_id");
                String name = resultSet.getString("name");        
                products.put(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, " Error Fetching products: "+ e.getMessage());
        }
        return products;
    }
    
    public Map<String, Integer> getProductValues(int productId) {
        Map<String, Integer> values = new HashMap<>();
        String query = "SELECT price, stock FROM products WHERE product_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // Assuming only one product per product_id
                int price = resultSet.getInt("price");
                int stock = resultSet.getInt("stock");
                values.put("price", price);
                values.put("stock", stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error Fetching Values: " + e.getMessage());
        }
        return values;
    }
  
    public void insertCategory(String name){
        String query = "INSERT INTO categories (name) VALUES (?)";
        
        try(Connection conn = dbConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, name);
            
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Το προϊόν προστέθηκε επιτυχώς!");
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την προσθήκη του προϊόντος: " + ex.getMessage());
        }
    }
    
    public void insertSubCategory(Integer categoryId, String name){
        String query = "INSERT INTO subcategories (category_id, name) VALUES (?, ?)";
        
        try (Connection conn = dbConnection.getConnection(); // Ensure you have your DB connection here
             PreparedStatement statement = conn.prepareStatement(query)) {
        
            statement.setInt(1, categoryId);
            statement.setString(2, name);
        
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Το προϊόν προστέθηκε επιτυχώς!");
            }
        
        }catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την προσθήκη του προϊόντος: " + ex.getMessage());
        }
    }
    
    public void insertProduct(int subCategoryId, String name, String description, String price, String stock) {
        String query = "INSERT INTO products (subcategory_id, name, description, price, stock) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection(); // Ensure you have your DB connection here
             PreparedStatement statement = conn.prepareStatement(query)) {
               
            statement.setInt(1, subCategoryId);
            statement.setString(2, name);
            statement.setString(3, description);
            statement.setBigDecimal(4, new java.math.BigDecimal(price)); // Ensure price is a valid decimal
            statement.setInt(5, Integer.parseInt(stock)); // Ensure stock is a valid integer

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Το προϊόν προστέθηκε επιτυχώς!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την προσθήκη του προϊόντος: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Παρακαλώ εισάγετε έγκυρα αριθμητικά στοιχεία για τιμή και απόθεμα.");
        }
    }
  
    public void editCategory(Integer categoryid, String name){
        String query = "UPDATE categories SET name = ? WHERE category_id = ?";
        
        try (Connection conn = dbConnection.getConnection(); // Ensure you have your DB connection here
             PreparedStatement statement = conn.prepareStatement(query)) {
        
            statement.setString(1, name);
            statement.setInt(2, categoryid);
        
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Η κατηγορία ενημερώθηκε επιτυχώς!");
            } else {
            JOptionPane.showMessageDialog(null, "Δεν βρέθηκε η κατηγορία.");
            }
        
        }catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την ενημερώση της κατηγορίας: " + ex.getMessage());
        }
    }
    
    public void editSubCategory(Integer subCategory_id, String name){
        String query = "UPDATE subcategories SET name = ? WHERE subcategory_id = ?";
        
        try (Connection conn = dbConnection.getConnection(); // Ensure you have your DB connection here
             PreparedStatement statement = conn.prepareStatement(query)) {
        
            statement.setString(1, name);
            statement.setInt(2, subCategory_id);
        
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Η υποκατηγορία ενημερώθηκε επιτυχώς!");
            } else {
            JOptionPane.showMessageDialog(null, "Δεν βρέθηκε η κατηγορία.");
            }
        
        }catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την ενημερώση της υποκατηγορίας: " + ex.getMessage());
        }
    }
    
    public void editProduct(Integer product_id, String name, String description) {
        String query = "UPDATE products SET name = ?, description = ? WHERE product_id = ?";
        
        try (Connection conn = dbConnection.getConnection(); // Ensure you have your DB connection here
             PreparedStatement statement = conn.prepareStatement(query)) {
        
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setInt(3, product_id);
        
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "To προϊόν ενημερώθηκε επιτυχώς!");
            } else {
            JOptionPane.showMessageDialog(null, "Δεν βρέθηκε το προϊόν.");
            }
        
        }catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την ενημερώση της υποκατηγορίας: " + ex.getMessage());
        }
    }
    
    public void editProductStock(Integer product_id, Integer stock){
        String query = "UPDATE products SET stock = ? WHERE product_id = ?";
        
        try (Connection conn = dbConnection.getConnection(); // Ensure you have your DB connection here
             PreparedStatement statement = conn.prepareStatement(query)) {
        
            statement.setInt(1, stock);
            statement.setInt(2, product_id);
        
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Το απόθεμα ενημερώθηκε επιτυχώς!");
            } else {
            JOptionPane.showMessageDialog(null, "Δεν βρέθηκε το προϊόν.");
            }
        
        }catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την ενημερώση της απόθηκης: " + ex.getMessage());
        }
    }
    
    public void editProductPrice(Integer product_id, Integer price){
        String query = "UPDATE products SET price = ? WHERE product_id = ?";
        
        try (Connection conn = dbConnection.getConnection(); // Ensure you have your DB connection here
             PreparedStatement statement = conn.prepareStatement(query)) {
        
            statement.setInt(1, price);
            statement.setInt(2, product_id);
        
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Η τιμή ενημερώθηκε επιτυχώς!");
            } else {
            JOptionPane.showMessageDialog(null, "Δεν βρέθηκε το προϊόν.");
            }
        
        }catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την ενημερώση της απόθηκης: " + ex.getMessage());
        }
    }
     
    public void delCategory(Integer categoryid){
        String query = "DELETE FROM categories WHERE category_id = ?";
        
        try (Connection conn = dbConnection.getConnection(); // Ensure you have your DB connection here
             PreparedStatement statement = conn.prepareStatement(query)) {
        
            statement.setInt(1, categoryid);
        
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Η κατηγορία διαγράφηκε επιτυχώς!");
            } else {
            JOptionPane.showMessageDialog(null, "Δεν βρέθηκε η κατηγορία.");
            }
        
        }catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την προσθήκη του προϊόντος: " + ex.getMessage());
        }
    }
    
    public void delSubCategory(Integer subCategoryid){
        String query = "DELETE FROM subcategories WHERE subcategory_id = ?";
        
        try (Connection conn = dbConnection.getConnection(); // Ensure you have your DB connection here
             PreparedStatement statement = conn.prepareStatement(query)) {
        
            statement.setInt(1, subCategoryid);
        
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Η κατηγορία διαγράφηκε επιτυχώς!");
            } else {
            JOptionPane.showMessageDialog(null, "Δεν βρέθηκε η κατηγορία.");
            }
        
        }catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την προσθήκη του προϊόντος: " + ex.getMessage());
        }
    }
}

