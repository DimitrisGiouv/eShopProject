
package eShopPackage;
import java.math.BigDecimal;
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

    DataAccessObject() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
 
    public DataAccessObject(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public Map<String, Object> loginUser(String username, String password) {      
        String query = "SELECT user_id, role FROM authentication WHERE username = ? AND password = ?";
        try (Connection conn = dbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            // If a result is found, login is successful
        if (rs.next()) {
            Map<String, Object> result = new HashMap<>();
            result.put("user_id", rs.getInt("user_id"));
            result.put("role", rs.getString("role"));
            return result;
        } else {
            return null;  // Return null if login fails
        }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Map<String, Map<String, String>> getUserProfiles(int userId) {
        Map<String, Map<String, String>> profileData = new HashMap<>();
        String query = "SELECT profile_id, name, surname, street, street_number, city, postcode, phone, cell_phone " +
                       "FROM user_profile WHERE user_id = ?";

        try (Connection conn = dbConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String profileId = rs.getString("profile_id");
                Map<String, String> profileDetails = new HashMap<>();

                profileDetails.put("name", rs.getString("name"));
                profileDetails.put("surname", rs.getString("surname"));
                profileDetails.put("street", rs.getString("street"));
                profileDetails.put("streetnumber", rs.getString("street_number"));
                profileDetails.put("city", rs.getString("city"));
                profileDetails.put("postcode", rs.getString("postcode"));
                profileDetails.put("phone", rs.getString("phone"));
                profileDetails.put("cellphone", rs.getString("cell_phone"));

                profileData.put(profileId, profileDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return profileData;
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
    
    public Map<String, String> getProductDetails(int productId) {
        Map<String, String> productDetails = new HashMap<>();
        String query = "SELECT name, description, price, stock FROM products WHERE product_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                productDetails.put("name", resultSet.getString("name"));
                productDetails.put("description", resultSet.getString("description"));
                productDetails.put("price", resultSet.getString("price"));
                productDetails.put("stock", String.valueOf(resultSet.getInt("stock")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productDetails;
    }
    
    public Map<Integer, Map<String, String>> getBasketDetails(Integer userId) {
        Map<Integer, Map<String, String>> basketDetails = new HashMap<>();
        String query = "SELECT basket_id, product_id, name, quantity, price FROM basket WHERE user_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Integer basketId = resultSet.getInt("basket_id");
                Map<String, String> productDetails = new HashMap<>();
                productDetails.put("product_id", String.valueOf(resultSet.getInt("product_id")));
                productDetails.put("name", resultSet.getString("name"));
                productDetails.put("quantity", String.valueOf(resultSet.getInt("quantity")));
                productDetails.put("price", resultSet.getString("price"));

                basketDetails.put(basketId, productDetails);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return basketDetails;
    }
    
    public Map<Integer, String> getAddressStreet (Integer userId) {
        Map<Integer, String> addressStreet = new HashMap<>();
        String query = "SELECT profile_id, street FROM user_profile WHERE user_id = ?";  // Adjust the table and column names as needed

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("profile_id");
                String street = resultSet.getString("street");
                addressStreet.put(id, street);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching categories: " + e.getMessage());

        }
        return addressStreet;
    }
     
    public Map<Integer, String> getLastSixProduct () {
        Map<Integer, String> newProduct = new HashMap<>();
        String query = "SELECT * FROM products ORDER BY product_id DESC Limit 6";

        try (Connection connection = dbConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                newProduct.put(id, name);
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newProduct;
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
  
    public void insertUserProfile(Integer userId, String name, String surname, String street, long streetNumber, String city, long postcode, long phone, long cellPhone) {
        String query = "INSERT INTO user_profile (user_id, name, surname, street, street_number, city, postcode, phone, cell_phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();  // Ensure you have your DB connection here
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setString(2, name);
            statement.setString(3, surname);
            statement.setString(4, street);
            statement.setLong(5, streetNumber);  
            statement.setString(6, city);
            statement.setLong(7, postcode);     
            statement.setLong(8, phone);        
            statement.setLong(9, cellPhone);    

            
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "New address added successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Error adding new address.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding new address: " + ex.getMessage());
        }
    }
    
     public void insertProductToBasket(Integer userId, Integer productId, String name, Integer quantity, Double price) {
        String query = "INSERT INTO basket (user_id, product_id, name, quantity, price) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();  // Ensure you have your DB connection here
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.setString(3, name);
            statement.setInt(4, quantity);
            statement.setDouble(5, price);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Προσθήκη στο καλάθι με επιτυχία!");
            } else {
                JOptionPane.showMessageDialog(null, "Αποτυχία στην προσθήκη στο καλάθι.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
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
    
      public void editUserProfile(Integer profileId, String name, String surname, String street, long streetNumber, String city, long postcode, long phone, long cellPhone) {
        String query = "UPDATE user_profile SET name = ?, surname = ?, street = ?, street_number = ?, city = ?, postcode = ?, phone = ?, cell_phone = ? WHERE profile_id = ?";

        try (Connection conn = dbConnection.getConnection(); 
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, surname);
            statement.setString(3, street);
            statement.setLong(4, streetNumber);
            statement.setString(5, city);
            statement.setLong(6, postcode);
            statement.setLong(7, phone);
            statement.setLong(8, cellPhone);
            statement.setInt(9, profileId);  // profileId remains Integer as it's probably used as an identifier

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Η προφίλ ενημερώθηκε επιτυχώς!");
            } else {
                JOptionPane.showMessageDialog(null, "Δεν βρέθηκε το προφίλ.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την ενημερώση του προφίλ: " + ex.getMessage());
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
    
    public boolean delProductBasket(Integer basketId) {
        String query = "DELETE FROM basket WHERE basket_id = ?";
        
        try (Connection conn = dbConnection.getConnection(); 
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, basketId);
            int rowsAffected = statement.executeUpdate();

            // Return true if the row was deleted, false otherwise
            return rowsAffected > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // Return false in case of an error
        }
    }
    
    public boolean delUserBasket(Integer userId) {
        String query = "DELETE FROM basket WHERE user_id = ?";
        
        try(Connection conn = dbConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            
            statement.setInt(1, userId);
            int rowsAffected = statement.executeUpdate();
            
            return rowsAffected >0;
        }catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
  
}
