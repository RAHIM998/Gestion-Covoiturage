package org.example.gestioncovoiturage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.gestioncovoiturage.Controllers.Users.ListUsersController;
import org.example.gestioncovoiturage.Controllers.WindowPrincipalController;
import org.example.gestioncovoiturage.Models.Compte;
import org.example.gestioncovoiturage.Models.RoleUser;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Models.Session;
import org.example.gestioncovoiturage.Repository.CompteRepository;
import org.example.gestioncovoiturage.Repository.UserRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

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
    private TextField cEmail;

    @FXML
    private TextField cPassword;

    @FXML
    private TextField cemail;

    @FXML
    private TextField cnom;

    @FXML
    private PasswordField cpassword;

    @FXML
    private TextField cprenom;

    @FXML
    private TextField ctelephone;
    @FXML
    private StackPane stackPane;

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    // Implémentation des méthodes
    @FXML
    void Login(ActionEvent event) {
        String email = cEmail.getText();
        String password = cPassword.getText();

        Users user = userRepository.authenticateUser(email, password);

        if (user != null) {
            // Connexion réussie
            Session.getInstance().setCurrentUser(user);

            // Charger la nouvelle vue
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gestioncovoiturage/WindowPrincipal.fxml"));
                Parent root = loader.load();

                WindowPrincipalController windowPrincipalController = loader.getController();
                windowPrincipalController.setCurrentUser(user);

                // Afficher la nouvelle fenêtre
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Page Principale");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la nouvelle fenêtre");
            }
        } else {
            // Échec de la connexion
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Email et/ou mot de passe incorrects");
        }
    }

    //Configuration des alertes
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Alerte d'information
    @FXML
    public void Signin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gestioncovoiturage/Views/Users/AddPassagers.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void Add(ActionEvent event) {
        try {
            // Création de l'utilisateur
            Users newUser = new Users(cnom.getText(), cprenom.getText(), ctelephone.getText());
            Users savedUser = userRepository.addUser(newUser);

            // Création du compte associé à l'utilisateur
            Compte newCompte = new Compte(cemail.getText(), cpassword.getText());
            newCompte.setUtilisateur(savedUser);
            compteRepository.AddCompte(newCompte);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur et compte créés avec succès.");

            // Fermer la fenêtre
            closeWindow(event);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la création de l'utilisateur.");
        }
    }

    private void closeWindow(ActionEvent event) {
        // Récupérer le bouton source de l'événement
        Node source = (Node) event.getSource();

        // Récupérer la fenêtre contenant le bouton source
        Stage stage = (Stage) source.getScene().getWindow();

        // Fermer la fenêtre
        stage.close();
    }

    @FXML
    void clear(ActionEvent event) {
        cnom.setText("");
        cprenom.setText("");
        ctelephone.setText("");
        cemail.setText("");
        cpassword.setText("");
    }

}
