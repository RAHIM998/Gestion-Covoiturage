package org.example.gestioncovoiturage.Controllers.Users;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.gestioncovoiturage.Models.Compte;
import org.example.gestioncovoiturage.Models.RoleUser;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Repository.CompteRepository;
import org.example.gestioncovoiturage.Repository.UserRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class AddChauffeurController implements Initializable {


    private UserRepository userRepository;
    private CompteRepository compteRepository;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.userRepository = UserRepository.builder().build();
            this.compteRepository = CompteRepository.builder().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Déclaration
    @FXML
    private TextField cnom;

    @FXML
    private TextField cprenom;

    @FXML
    private TextField ctelephone;
    @FXML
    private StackPane stackPane;

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }


    @FXML
    private void clear(ActionEvent event) {
        cnom.clear();
        cprenom.clear();
        ctelephone.clear();
    }

    @FXML
    public void Add(ActionEvent actionEvent) {
        try {
            System.out.println("Vérification des champs...");
            if (cnom.getText().isEmpty() || cprenom.getText().isEmpty() || ctelephone.getText().isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Erreur", "Tous les champs sont obligatoires !");
                return; // Sortir de la méthode si les champs sont vides
            }

            // Création de l'utilisateur
            System.out.println("Création de l'utilisateur...");
            Users chauffeur = new Users( cprenom.getText(), cnom.getText(),ctelephone.getText());
            chauffeur.setRole(RoleUser.CONDUCTEUR);
            System.out.println("Utilisateur créé : " + chauffeur);

            Users savedUser = userRepository.addUser(chauffeur);

            clear(actionEvent);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur et compte créés avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la création de l'utilisateur.");
        }
    }

    // Configuration des alertes
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
