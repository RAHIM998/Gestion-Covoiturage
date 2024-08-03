package org.example.gestioncovoiturage.Controllers.Reservations;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.example.gestioncovoiturage.Models.Reservation;
import org.example.gestioncovoiturage.Models.Trajet;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Repository.ReservationRepository;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.List;

public class ReservationController implements Initializable {

    private ReservationRepository reservationRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.reservationRepository = ReservationRepository.builder().build();
        printAllReservations();
    }

    // Déclarations
    @FXML
    private TableView<Reservation> TabReservation;

    @FXML
    private TableColumn<Reservation, Long> tId;

    @FXML
    private TableColumn<Reservation, LocalDateTime> tDateReservation;

    @FXML
    private TableColumn<Reservation, Integer> tNbPlace;

    @FXML
    private TableColumn<Reservation, String> tPassager;

    @FXML
    private TableColumn<Reservation, String> tTrajet;

    @FXML
    private StackPane stackPane;

    @FXML
    private Button btnReservationsDuJour;

    @FXML
    private Button btnReservationsPassees;

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    // Affichage de toutes les réservations
    public void printAllReservations() {
        ObservableList<Reservation> list = reservationRepository.getAllReservation();
        updateTableView(list);
    }


    // Mise à jour de la TableView
    private void updateTableView(ObservableList<Reservation> list) {
        tId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tDateReservation.setCellValueFactory(new PropertyValueFactory<>("dateReservation"));
        tNbPlace.setCellValueFactory(new PropertyValueFactory<>("nbPlaceReservation"));

        tPassager.setCellValueFactory(cellData -> {
            Users user = cellData.getValue().getPassager();
            return new SimpleStringProperty(user != null ? user.getNom() + " " + user.getPrenom() : "N/A");
        });

        tTrajet.setCellValueFactory(cellData -> {
            Trajet trajet = cellData.getValue().getTrajet();
            return new SimpleStringProperty(trajet != null ? trajet.getVilleDepart() + " -- " + trajet.getVilleArrivee() : "N/A");
        });

        TabReservation.setItems(list);
    }

@FXML
    void AnnulerReservation(ActionEvent event) {
        Reservation selectedreservation = TabReservation.getSelectionModel().getSelectedItem();
        if (selectedreservation == null) {
            Alert noSelectionAlert = new Alert(Alert.AlertType.WARNING);
            noSelectionAlert.setTitle("Aucun élément sélectionné");
            noSelectionAlert.setHeaderText(null);
            noSelectionAlert.setContentText("Veuillez sélectionner la reservation à annuler.");
            noSelectionAlert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Suppression !");
        alert.setContentText("Voulez-vous supprimer cet élément ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Long trajetId = selectedreservation.getId();
            System.out.println("Tentative de suppression du véhicule avec l'ID : " + trajetId);
            reservationRepository.DeleteReservation(trajetId);
            printAllReservations();
        } else {
            System.out.println("Suppression annulée par l'utilisateur.");
        }
    }

    @FXML
    void CallFormReservation(ActionEvent event) {
        loadView("/org/example/gestioncovoiturage/Views/Reservations/AddReservation.fxml");
    }

    private void loadView(String fxml) {
        try {
            System.out.println("Loading FXML: " + fxml);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root;
            root = loader.load();
            if (stackPane == null) {
                System.out.println("stackPane is null");
            } else {
                stackPane.getChildren().clear();
                stackPane.getChildren().add(root);
            }
        } catch (IOException e) {
            e.printStackTrace();

            showAlert(Alert.AlertType.ERROR, "Erreur", "Chargement échoué", "Impossible de charger la vue.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    void ReservationActuelle(ActionEvent event) {
        LocalDate today = LocalDate.now();
        ObservableList<Reservation> reservationsDuJour = reservationRepository.getReservationsByDate(today.atStartOfDay(), today.plusDays(1).atStartOfDay());
        updateTableView(reservationsDuJour);
    }

    @FXML
    void ReservationPassee(MouseEvent event) {
        LocalDateTime now = LocalDateTime.now();
        ObservableList<Reservation> reservationsPassees = reservationRepository.getReservationsBeforeDate(now);
        updateTableView(reservationsPassees);
    }

    @FXML
    void ReservationFutur(ActionEvent event) {
        LocalDateTime now = LocalDateTime.now();
        ObservableList<Reservation> reservationsFutures = reservationRepository.getReservationsAfterDate(now);
        updateTableView(reservationsFutures);
    }




}
