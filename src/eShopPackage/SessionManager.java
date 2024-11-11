
package eShopPackage;

public class SessionManager {
    
    private static int userId;
    private static String username;

    // Set user details in the session
    public static void startSession(int id, String name) {
        userId = id;
        username = name;
    }

    // Get userId from session
    public static int getUserId() {
        return userId;
    }

    // Get username from session
    public static String getUsername() {
        return username;
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
