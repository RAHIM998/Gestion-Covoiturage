module org.example.gestioncovoiturage {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires jakarta.persistence;
    requires spring.security.crypto;
    requires spring.security.core;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires javax.mail.api;


    opens org.example.gestioncovoiturage to javafx.fxml;
    exports org.example.gestioncovoiturage;
    opens org.example.gestioncovoiturage.Models;
    exports org.example.gestioncovoiturage.Models;
    exports org.example.gestioncovoiturage.Controllers;
    opens org.example.gestioncovoiturage.Controllers to javafx.fxml;
    opens org.example.gestioncovoiturage.Controllers.Users to javafx.fxml;
    opens org.example.gestioncovoiturage.Controllers.Compte to javafx.fxml;
    opens org.example.gestioncovoiturage.Controllers.Reservations to javafx.fxml;
    opens org.example.gestioncovoiturage.Controllers.Trajet to javafx.fxml;
    opens org.example.gestioncovoiturage.Controllers.Vehicule to javafx.fxml;
}