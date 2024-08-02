package org.example.gestioncovoiturage.Controllers.Vehicule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.example.gestioncovoiturage.Controllers.Users.PassagerController;
import org.example.gestioncovoiturage.Models.RoleUser;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Models.Vehicule;
import org.example.gestioncovoiturage.Repository.UserRepository;
import org.example.gestioncovoiturage.Repository.VehiculeRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class EditVehiculeController implements Initializable {

    private UserRepository userRepository;
    private VehiculeRepository vehiculeRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userRepository = UserRepository.builder().build();
        this.vehiculeRepository = VehiculeRepository.builder().build();

        configureChauffeurComboBox();
    }

    private StackPane stackPane;

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    // Déclarations
    @FXML
    private TextField cId;

    @FXML
    private ComboBox<Users> ChauffeurCbx;

    @FXML
    private TextField cImmatriculation;

    @FXML
    private TextField cMarque;

    @FXML
    private TextField cModele;

    @FXML
    void Clear(ActionEvent event) {
        cMarque.clear();
        cModele.clear();
        cImmatriculation.clear();
        ChauffeurCbx.getSelectionModel().clearSelection();    }

    @FXML
    void Update(ActionEvent event) {
        try {
            Long Id = Long.valueOf(cId.getText());
            String marque = cMarque.getText();
            String model = cModele.getText();
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
            vehicule.setId(Id);

            // Appel à UpdateVehicule et vérification du succès
            boolean success = vehiculeRepository.UpdateVehicule(vehicule);

            if (success) {
                Clear(event);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Les informations ont été mises à jour avec succès.");
                successAlert.showAndWait();
            }

        } catch (NumberFormatException e) {
            vehiculeRepository.showAlert(Alert.AlertType.ERROR, "Erreur", "Format ID invalide", "L'ID doit être un nombre valide.");
        }
    }

    // Configuration du ComboBox
    private void configureChauffeurComboBox() {
        // Récupérer les conducteurs depuis le repository
        ObservableList<Users> conducteurs = FXCollections.observableArrayList(userRepository.getAllConducteurs());
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

    public void initializeWithUser(Vehicule vehicule) {
        if (vehicule != null) {
            // Initialisation des champs de texte
            cId.setText(vehicule.getId() + "");
            cMarque.setText(vehicule.getMarque());
            cModele.setText(vehicule.getModele());
            cImmatriculation.setText(vehicule.getImmatriculation());

            // Sélectionner l'utilisateur dans le ComboBox
            Users utilisateur = vehicule.getUtilisateur();
            if (utilisateur != null) {
                ChauffeurCbx.getSelectionModel().select(
                        ChauffeurCbx.getItems().stream()
                                .filter(user -> user.getId().equals(utilisateur.getId()))
                                .findFirst()
                                .orElse(null)
                );
            }
        }
    }

}
