package org.example.gestioncovoiturage.Controllers.Vehicule;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import org.example.gestioncovoiturage.Models.RoleUser;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Models.Vehicule;
import org.example.gestioncovoiturage.Repository.UserRepository;
import org.example.gestioncovoiturage.Repository.VehiculeRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class AddVehiculeController implements Initializable {

    //Déclarations
    @FXML
    private TextField cImmatriculation;

    @FXML
    private TextField cMarque;

    @FXML
    private TextField cModel;

    @FXML
    private ComboBox<Users> ChauffeurCbx;

    private UserRepository userRepository;
    private VehiculeRepository vehiculeRepository;

    //Constructeur par défaut
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.userRepository = UserRepository.builder().build();
        this.vehiculeRepository = VehiculeRepository.builder().build();

        configureChauffeurComboBox();
    }

    //Configuration du combo box
    private void configureChauffeurComboBox() {
        // Récupérer les conducteurs depuis le repository
        ObservableList<Users> conducteurs = userRepository.getAllConducteurs();
        ChauffeurCbx.setItems(conducteurs);

        // Configurer l'affichage des éléments dans le ComboBox
        ChauffeurCbx.setCellFactory(cell -> new ListCell<Users>() {
            @Override
            protected void updateItem(Users user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? "" : user.getNom() + " " + user.getPrenom());
            }
        });

        // Configurer l'affichage des éléments sélectionnés dans le ComboBox
        ChauffeurCbx.setButtonCell(new ListCell<Users>() {
            @Override
            protected void updateItem(Users user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? "" : user.getNom() + " " + user.getPrenom());
            }
        });
    }

    //Méthode d'ajout dans la bd
    @FXML
    void Add(ActionEvent event) {

        String marque = cMarque.getText();
        String model = cModel.getText();
        String immatriculation = cImmatriculation.getText();
        Users chauffeur = ChauffeurCbx.getSelectionModel().getSelectedItem();

        // Vérification si les champs sont vides
        if (marque.isEmpty() || model.isEmpty() || immatriculation.isEmpty() || chauffeur == null) {
            Alert noSelectionAlert = new Alert(Alert.AlertType.WARNING);
            noSelectionAlert.setTitle("Champs manquants");
            noSelectionAlert.setHeaderText(null);
            noSelectionAlert.setContentText("Tous les champs sont obligatoires. Veuillez les remplir.");
            noSelectionAlert.showAndWait();
            return;
        }


        // Création du véhicule
        Vehicule vehicule = new Vehicule(marque, model, immatriculation, chauffeur);

        // Appel à addVehicule et gestion des erreurs
        try {
            vehiculeRepository.addVehicule(vehicule);

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

    //Effacer les champs
    @FXML
    void Clear(ActionEvent event) {
        cMarque.clear();
        cModel.clear();
        cImmatriculation.clear();
        ChauffeurCbx.getSelectionModel().clearSelection();
    }

}
