package org.example.gestioncovoiturage.Controllers.Trajet;

import jakarta.persistence.Id;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.example.gestioncovoiturage.Models.Trajet;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Repository.TrajetRepository;
import org.example.gestioncovoiturage.Repository.UserRepository;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class EditTrajetController implements Initializable {

    private UserRepository userRepository;
    private TrajetRepository trajetRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userRepository = UserRepository.builder().build();
        this.trajetRepository = TrajetRepository.builder().build();
        configureChauffeurComboBox();
    }

    @FXML
    private ComboBox<Users> ConducteurCbx;

    @FXML
    private TextField tId;

    @FXML
    private DatePicker tDateDepart;

    @FXML
    private TextField tPrix;

    @FXML
    private TextField tVArrive;

    @FXML
    private TextField tVDepart;

    private StackPane stackPane;

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    public void initializeWithUser(Trajet trajet) {
        if (trajet != null) {
            // Initialisation des champs de texte
            tId.setText(trajet.getId() + "");
            tVDepart.setText(trajet.getVilleDepart());
            tVArrive.setText(trajet.getVilleArrivee());

            // Convertir LocalDate en String pour afficher dans DatePicker
            if (trajet.getDateDepart() != null) {
                tDateDepart.setValue(trajet.getDateDepart().toLocalDate());
            }

            // Convertir Double en String pour afficher dans TextField
            if (trajet.getPrix() != null) {
                tPrix.setText(trajet.getPrix().toString());
            }

            // Sélectionner l'utilisateur dans le ComboBox
            Users utilisateur = trajet.getConducteur();
            if (utilisateur != null) {
                ConducteurCbx.getSelectionModel().select(
                        ConducteurCbx.getItems().stream()
                                .filter(user -> user.getId().equals(utilisateur.getId()))
                                .findFirst()
                                .orElse(null)
                );
            }
        }
    }

    // Configuration du ComboBox
    private void configureChauffeurComboBox() {
        // Récupérer les conducteurs depuis le repository
        ObservableList<Users> conducteurs = userRepository.getAllConducteurs();
        ConducteurCbx.setItems(conducteurs);

        // Configurer l'affichage des éléments dans le ComboBox
        ConducteurCbx.setCellFactory(cell -> new ListCell<Users>() {
            @Override
            protected void updateItem(Users user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? "" : user.getNom() + " " + user.getPrenom());
            }
        });

        // Configurer l'affichage des éléments sélectionnés dans le ComboBox
        ConducteurCbx.setButtonCell(new ListCell<Users>() {
            @Override
            protected void updateItem(Users user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? "" : user.getNom() + " " + user.getPrenom());
            }
        });
    }

    @FXML
    void Update(ActionEvent event) {
        // Récupération des valeurs des champs


        Long Id = Long.valueOf(tId.getText());


        String villeDepartText = tVDepart.getText();
        String villeArriveeText = tVArrive.getText();
        LocalDate dateDepartDate = tDateDepart.getValue();
        String prixText = tPrix.getText();
        Users conducteur = ConducteurCbx.getSelectionModel().getSelectedItem();

        // Vérification si les champs sont vides
        if (villeDepartText.isEmpty() || villeArriveeText.isEmpty() || dateDepartDate == null || prixText.isEmpty() || conducteur == null) {
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

        // Conversion de LocalDate en LocalDateTime
        LocalDateTime dateDepartDateTime = dateDepartDate.atStartOfDay();

        // Création de l'objet Trajet avec l'ID
        Trajet trajet = new Trajet();
        trajet.setId(Id); // Assigner l'ID pour la mise à jour
        trajet.setVilleDepart(villeDepartText);
        trajet.setVilleArrivee(villeArriveeText);
        trajet.setDateDepart(dateDepartDateTime);
        trajet.setPrix(prixInteger);
        trajet.setConducteur(conducteur);

        try {
            boolean success = trajetRepository.UpdateTrajet(trajet); // Mise à jour du trajet dans le repository

            if (success) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Le trajet a été mis à jour avec succès.");
                successAlert.showAndWait();
                Clear(event); // Appel de la méthode Clear pour réinitialiser les champs
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Le trajet n'a pas pu être mis à jour. Vérifiez l'ID et réessayez.");
                errorAlert.showAndWait();
            }
        } catch (RuntimeException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Une erreur est survenue lors de la mise à jour du trajet : " + e.getMessage());
            errorAlert.showAndWait();
        }
    }



    @FXML
    void Clear(ActionEvent event) {
        tId.clear();
        tVDepart.clear();
        tVArrive.clear();
        tDateDepart.setValue(null);
        tPrix.clear();
        ConducteurCbx.setValue(null);
    }
}
