
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
        }

        return subCategories;
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
  
}

