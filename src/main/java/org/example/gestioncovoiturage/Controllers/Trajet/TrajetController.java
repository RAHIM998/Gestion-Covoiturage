package org.example.gestioncovoiturage.Controllers.Trajet;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.example.gestioncovoiturage.Controllers.Vehicule.EditVehiculeController;
import org.example.gestioncovoiturage.HelloApplication;
import org.example.gestioncovoiturage.Models.Trajet;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Models.Vehicule;
import org.example.gestioncovoiturage.Repository.TrajetRepository;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class TrajetController implements Initializable {

    //Constructeur par défaut
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.trajetRepository = TrajetRepository.builder().build();

        printAllTraject();

        // Ajouter un écouteur de modifications pour le champ de recherche
        tRechercher.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchPassager(newValue);
            }
        });
    }

    //Déclarations
    TrajetRepository trajetRepository;
    @FXML
    private TableView<Trajet> TabTrajet;

    @FXML
    private TableColumn<Trajet, String> tConducteur;

    @FXML
    private TableColumn<Trajet, LocalDateTime> tDateDepart;

    @FXML
    private TableColumn<Trajet, Long> tId;

    @FXML
    private TableColumn<Trajet, Integer> tPrix;

    @FXML
    private TextField tRechercher;

    @FXML
    private TableColumn<Trajet, String> tVilleArrive;

    @FXML
    private TableColumn<Trajet, String> tVilleDepart;

    @FXML
    private StackPane stackPane;

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    //Méthodes des trajets
    @FXML
    void CallFormAddTrajet(MouseEvent event) {
        loadView("/org/example/gestioncovoiturage/Views/Trajets/AddTrajet.fxml");
    }

    private void loadView(String fxml) {
        try {
            System.out.println("Loading FXML: " + fxml);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
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


    //Méthode de recherche
    @FXML
    public void searchPassager(String actionEvent) {
        String searchTerm = tRechercher.getText();
        ObservableList<Trajet> foundTrajet = trajetRepository.searchTraject(searchTerm);
        TabTrajet.setItems(foundTrajet);
    }


    // Affichage des véhicules
    public void printAllTraject() {
        ObservableList<Trajet> list = trajetRepository.getAllTraject();

        tId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tVilleDepart.setCellValueFactory(new PropertyValueFactory<>("villeDepart"));
        tVilleArrive.setCellValueFactory(new PropertyValueFactory<>("villeArrivee"));
        tDateDepart.setCellValueFactory(new PropertyValueFactory<>("dateDepart"));
        tPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));

        // Configurer l'affichage du nom complet du conducteur dans tConducteur
        tConducteur.setCellValueFactory(cellData -> {
            Users user = cellData.getValue().getConducteur();
            return new SimpleStringProperty(user != null ? user.getFullName() : "N/A");
        });

        TabTrajet.setItems(list);
    }


    @FXML
    void Delete(MouseEvent event) {
        Trajet selectedtrajet = TabTrajet.getSelectionModel().getSelectedItem();
        if (selectedtrajet == null) {
            Alert noSelectionAlert = new Alert(Alert.AlertType.WARNING);
            noSelectionAlert.setTitle("Aucun élément sélectionné");
            noSelectionAlert.setHeaderText(null);
            noSelectionAlert.setContentText("Veuillez sélectionner un élément à supprimer.");
            noSelectionAlert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Suppression !");
        alert.setContentText("Voulez-vous supprimer cet élément ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Long trajetId = selectedtrajet.getId();
            System.out.println("Tentative de suppression du véhicule avec l'ID : " + trajetId);
            trajetRepository.DeleteTrajet(trajetId);
            printAllTraject();
        } else {
            System.out.println("Suppression annulée par l'utilisateur.");
        }
    }

    @FXML
    void LoadFormModifTrajet(MouseEvent event) {
        if (event.getClickCount() == 2) {
            try {
                Trajet selectedTrajet = TabTrajet.getSelectionModel().getSelectedItem();
                if (selectedTrajet == null) {
                    System.out.println("Aucun trajet sélectionné");
                    return;
                }

                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource
                        ("/org/example/gestioncovoiturage/Views/Trajets/EditTrajet.fxml"));
                Parent root = loader.load();

                EditTrajetController editTrajetController = loader.getController();
                editTrajetController.setStackPane(stackPane);

                editTrajetController.initializeWithUser(selectedTrajet);

                stackPane.getChildren().clear();
                stackPane.getChildren().add(root);
                stackPane.setPadding(new Insets(10, 20, 10, 50));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
