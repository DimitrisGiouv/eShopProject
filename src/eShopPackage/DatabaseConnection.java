package eShopPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection connection;
    
    
    public DatabaseConnection(){
        try{
     String URL = "jdbc:postgresql://dblabs.iee.ihu.gr:5432/it185167"; // Replace with your database name
     String USER = "it185167"; // Replace with your database username
     String PASSWORD = "231997"; // Replace with your database password
     connection = DriverManager.getConnection(URL,USER,PASSWORD);
    }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

