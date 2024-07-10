module org.example.gestioncovoiturage {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires jakarta.persistence;
    requires spring.security.crypto;
    requires spring.security.core;
    requires java.sql;
    requires org.hibernate.orm.core;


    opens org.example.gestioncovoiturage to javafx.fxml;
    exports org.example.gestioncovoiturage;
    opens org.example.gestioncovoiturage.Models;
    exports org.example.gestioncovoiturage.Models;
}