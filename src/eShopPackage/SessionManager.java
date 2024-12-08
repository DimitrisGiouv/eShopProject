
package eShopPackage;

public class SessionManager {
    
    private static int userId;
    private static String username, role;

    // Set user details in the session
    public static void startSession(int id, String name, String userRole) {
        userId = id;
        username = name;
        role = userRole;
    }

    // Get userId from session
    public static int getUserId() {
        return userId;
    }

    // Get username from session
    public static String getUsername() {
        return username;
    }
    
    // Get role from session
    public static String getRole() {
        return role;
    }

    // End the session by clearing the stored data
    public static void endSession() {
        userId = -1;
        username = null;
    }

    // Check if a session exists (optional)
    public static boolean isUserLoggedIn() {
        return username != null;
    }
    
}
