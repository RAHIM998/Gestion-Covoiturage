module org.example.gestioncovoiturage {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.gestioncovoiturage to javafx.fxml;
    exports org.example.gestioncovoiturage;
}