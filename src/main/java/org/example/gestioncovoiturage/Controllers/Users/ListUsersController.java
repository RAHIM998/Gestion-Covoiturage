package org.example.gestioncovoiturage.Controllers.Users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.example.gestioncovoiturage.HelloApplication;
import org.example.gestioncovoiturage.Models.Compte;
import org.example.gestioncovoiturage.Models.RoleUser;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Repository.CompteRepository;
import org.example.gestioncovoiturage.Repository.UserRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ListUsersController implements Initializable {

    //Constructeur par défaut
    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    //Déclarations
    @FXML
    private StackPane stackPane;

    @FXML
    private ComboBox<RoleUser> RoleCbx;

    @FXML
    private TextField cEmail;

    @FXML
    private TextField cNom;

    @FXML
    private TextField cPassword;

    @FXML
    private TextField cPrenom;

    @FXML
    private TextField cTelephone;

    private UserRepository userRepository;
    private CompteRepository compteRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.userRepository = UserRepository.builder().build();
            this.compteRepository = CompteRepository.builder().build();
            ObservableList<RoleUser> roleUsers = FXCollections.observableArrayList(RoleUser.values());
            RoleCbx.setItems(roleUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Méthodes pour les utilisateurs
    @FXML
    void AddUser(ActionEvent event) {
        try {
            // Création de  l'utilisateur
            Users newUser = new Users(cPrenom.getText(), cNom.getText(), cTelephone.getText(), RoleCbx.getValue());
            Users savedUser = userRepository.addUser(newUser);

            // Création du compte associer à l'utilisateur
            Compte newCompte = new Compte(cEmail.getText(), cPassword.getText());
            newCompte.setUtilisateur(savedUser);
            compteRepository.AddCompte(newCompte);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur et compte créés avec succès.");
            Clear(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Alerte de confirmation
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void Clear(ActionEvent event) {
        cPrenom.setText("");
        cTelephone.setText("");
        cEmail.setText("");
        cPassword.setText("");
        RoleCbx.getSelectionModel().clearSelection();

        // Mettre le focus sur le premier champ
        cNom.requestFocus();
    }

}
