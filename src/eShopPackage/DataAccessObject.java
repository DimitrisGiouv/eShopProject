
package eShopPackage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataAccessObject {
   
 private final DatabaseConnection dbConnection;

    public DataAccessObject(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<String> getCategoryNames() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT name, category_id FROM categories";  // Adjust the table and column names as needed

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                categories.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }
    
  public List<String> getSubCategoryNames() {
        List<String> SubCategories = new ArrayList<>();
        String query = "SELECT name, category_id FROM subcategories";  // Adjust the table and column names as needed

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                SubCategories.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return SubCategories;
  }
  
  
}

