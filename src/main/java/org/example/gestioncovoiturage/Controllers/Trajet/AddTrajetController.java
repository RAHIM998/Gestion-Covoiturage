package org.example.gestioncovoiturage.Controllers.Trajet;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.gestioncovoiturage.Models.Trajet;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Repository.TrajetRepository;
import org.example.gestioncovoiturage.Repository.UserRepository;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddTrajetController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userRepository = UserRepository.builder().build();
        this.trajetRepository = TrajetRepository.builder().build();
        configureChauffeurComboBox();
    }

    UserRepository userRepository;
    TrajetRepository trajetRepository;

    @FXML
    private ComboBox<Users> conducteurCbx;

    @FXML
    private DatePicker dateDepart;

    @FXML
    private TextField prix;

    @FXML
    private TextField villeArrive;

    @FXML
    private TextField villeDepart;

    // Configuration du combo box
    private void configureChauffeurComboBox() {
        // Récupérer les conducteurs depuis le repository
        ObservableList<Users> conducteurs = userRepository.getAllConducteurs();
        conducteurCbx.setItems(conducteurs);

        // Configurer l'affichage des éléments dans le ComboBox
        conducteurCbx.setCellFactory(cell -> new ListCell<Users>() {
            @Override
            protected void updateItem(Users user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? "" : user.getNom() + " " + user.getPrenom());
            }
        });

        // Configurer l'affichage des éléments sélectionnés dans le ComboBox
        conducteurCbx.setButtonCell(new ListCell<Users>() {
            @Override
            protected void updateItem(Users user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? "" : user.getNom() + " " + user.getPrenom());
            }
        });
    }

    @FXML
    void Add(ActionEvent event) {
        String villeDepartText = this.villeDepart.getText();
        String villeArriveeText = villeArrive.getText();
        LocalDate dateDepartDate = this.dateDepart.getValue();
        String prixText =  this.prix.getText();
        Users conducteur = this.conducteurCbx.getSelectionModel().getSelectedItem();

        // Vérification si les champs sont vides
        if (villeDepartText.isEmpty() || villeArriveeText.isEmpty() || dateDepartDate == null || prixText == null || conducteur == null) {
            Alert noSelectionAlert = new Alert(Alert.AlertType.WARNING);
            noSelectionAlert.setTitle("Champs manquants");
            noSelectionAlert.setHeaderText(null);
            noSelectionAlert.setContentText("Tous les champs sont obligatoires. Veuillez les remplir.");
            noSelectionAlert.showAndWait();
            return;
        }

        // Conversion du prix en Integer
        Integer prixInteger;
        try {
            prixInteger = Integer.parseInt(prixText);
        } catch (NumberFormatException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Le prix doit être un nombre entier valide.");
            errorAlert.showAndWait();
            return;
        }

        // Création de l'objet Trajet
        Trajet trajet = new Trajet(villeDepartText, villeArriveeText, dateDepartDate, prixInteger, conducteur);

        try {
            trajetRepository.addTrajet(trajet);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Les informations ont été ajoutées avec succès.");
            successAlert.showAndWait();
            Clear(event);
        } catch (RuntimeException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }

    @FXML
    void Clear(ActionEvent event) {
        villeDepart.clear();
        villeArrive.clear();
        prix.clear();
        dateDepart.setValue(null);
        conducteurCbx.getSelectionModel().clearSelection();
    }
}
