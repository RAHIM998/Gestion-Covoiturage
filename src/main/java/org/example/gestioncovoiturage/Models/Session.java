package org.example.gestioncovoiturage.Models;

public class Session {
    private static Session instance;
    private Users currentUser;

    private Session() {
        // Constructeur privé pour implémenter le singleton
    }

    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }
}
