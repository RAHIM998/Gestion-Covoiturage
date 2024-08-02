package org.example.gestioncovoiturage.Controllers.Reservations;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.gestioncovoiturage.Models.Reservation;
import org.example.gestioncovoiturage.Models.Trajet;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Repository.ReservationRepository;
import org.example.gestioncovoiturage.Repository.TrajetRepository;
import org.example.gestioncovoiturage.Repository.UserRepository;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddReservationController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userRepository = UserRepository.builder().build();
        this.reservationRepository = ReservationRepository.builder().build();
        this.trajetRepository = TrajetRepository.builder().build();

        configurePassagerComboBox();
        configureTrajetComboBox();
    }

    UserRepository userRepository;
    ReservationRepository reservationRepository;
    TrajetRepository trajetRepository;


    @FXML
    private ComboBox<Users> PassagerCbx;

    @FXML
    private ComboBox<Trajet> TrjetCbx;

    @FXML
    private DatePicker tDateReservation;

    @FXML
    private TextField tnbPlace;



    // Configuration du combobox passager
    private void configurePassagerComboBox() {
        // Récupérer les conducteurs depuis le repository
        ObservableList<Users> passager = userRepository.getAllPassager();
        PassagerCbx.setItems(passager);

        // Configurer l'affichage des éléments dans le ComboBox
        PassagerCbx.setCellFactory(cell -> new ListCell<Users>() {
            @Override
            protected void updateItem(Users user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? "" : user.getNom() + " " + user.getPrenom());
            }
        });

        // Configurer l'affichage des éléments sélectionnés dans le ComboBox
        PassagerCbx.setButtonCell(new ListCell<Users>() {
            @Override
            protected void updateItem(Users user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? "" : user.getNom() + " " + user.getPrenom());
            }
        });
    }
    // Configuration du combobox trajet
    private void configureTrajetComboBox() {
        // Récupérer les trajets depuis le repository
        ObservableList<Trajet> trajets = trajetRepository.getAllTraject();
        TrjetCbx.setItems(trajets);

        // Configurer l'affichage des éléments dans le ComboBox
        TrjetCbx.setCellFactory(cell -> new ListCell<Trajet>() {
            @Override
            protected void updateItem(Trajet trajet, boolean empty) {
                super.updateItem(trajet, empty);
                setText(empty ? "" : trajet.getVilleDepart() + " -- " + trajet.getVilleArrivee());
            }
        });

        // Configurer l'affichage des éléments sélectionnés dans le ComboBox
        TrjetCbx.setButtonCell(new ListCell<Trajet>() {
            @Override
            protected void updateItem(Trajet trajet, boolean empty) {
                super.updateItem(trajet, empty);
                setText(empty ? "" : trajet.getVilleDepart() + " -- " + trajet.getVilleArrivee());
            }
        });
    }

    @FXML
    void Add(ActionEvent event) {
        // Récupération des valeurs des champs
        Users passager = PassagerCbx.getSelectionModel().getSelectedItem();
        Trajet trajet = TrjetCbx.getSelectionModel().getSelectedItem();
        LocalDate dateReservation = tDateReservation.getValue();
        String nbPlaceText = tnbPlace.getText();

        // Vérification si les champs sont remplis
        if (passager == null || trajet == null || dateReservation == null || nbPlaceText.isEmpty()) {
            Alert noSelectionAlert = new Alert(Alert.AlertType.WARNING);
            noSelectionAlert.setTitle("Champs manquants");
            noSelectionAlert.setHeaderText(null);
            noSelectionAlert.setContentText("Tous les champs sont obligatoires. Veuillez les remplir.");
            noSelectionAlert.showAndWait();
            return;
        }

        // Conversion du nombre de places en entier
        int nbPlace;
        try {
            nbPlace = Integer.parseInt(nbPlaceText);
        } catch (NumberFormatException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Le nombre de places doit être un entier valide.");
            errorAlert.showAndWait();
            return;
        }

        // Vérification si le passager a déjà une réservation pour ce trajet à la même date
        Reservation existingReservation = reservationRepository.findReservationByPassagerAndTrajetAndDate(passager, trajet, dateReservation);
        if (existingReservation != null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Réservation déjà existante");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Vous avez déjà une réservation pour ce trajet à cette date.");
            errorAlert.showAndWait();
            return;
        }

        // Création de la nouvelle réservation
        Reservation reservation = new Reservation();
        reservation.setPassager(passager);
        reservation.setTrajet(trajet);
        reservation.setDateReservation(dateReservation.atStartOfDay()); // Convertir LocalDate en LocalDateTime
        reservation.setNbPlaceReservation(nbPlace);

        try {
            reservationRepository.AddReservation(reservation);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText(null);
            successAlert.setContentText("La réservation a été ajoutée avec succès.");
            successAlert.showAndWait();

            Clear(event); // Réinitialiser les champs après ajout
        } catch (RuntimeException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Une erreur est survenue lors de l'ajout de la réservation : " + e.getMessage());
            errorAlert.showAndWait();
        }
    }


    @FXML
    void Clear(ActionEvent event) {
        // Réinitialiser le ComboBox Passager
        PassagerCbx.getSelectionModel().clearSelection();

        // Réinitialiser le ComboBox Trajet
        TrjetCbx.getSelectionModel().clearSelection();

        // Réinitialiser le DatePicker
        tDateReservation.setValue(null);

        // Réinitialiser le TextField du nombre de places
        tnbPlace.clear();
    }

}
