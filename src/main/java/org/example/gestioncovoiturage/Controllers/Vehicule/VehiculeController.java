package org.example.gestioncovoiturage.Controllers.Vehicule;

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
import org.example.gestioncovoiturage.Controllers.Users.EditUserController;
import org.example.gestioncovoiturage.HelloApplication;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Models.Vehicule;
import org.example.gestioncovoiturage.Repository.UserRepository;
import org.example.gestioncovoiturage.Repository.VehiculeRepository;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class VehiculeController implements Initializable {

    //Constructeur par défault
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.userRepository = UserRepository.builder().build();
        this.vehiculeRepository = VehiculeRepository.builder().build();

        printAllVehicule();

        // Ajouter un écouteur de modifications pour le champ de recherche
        cRecheerche.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchPassager(newValue);
            }
        });
    }

    //Déclarations

    private UserRepository userRepository;
    private VehiculeRepository vehiculeRepository;

    @FXML
    private TableView<Vehicule> TabVehicule;

    @FXML
    private TextField cRecheerche;

    @FXML
    private TableColumn<Vehicule, String> tConducteur;

    @FXML
    private TableColumn<Vehicule, Long> tId;

    @FXML
    private TableColumn<Vehicule, String> tImmatriculation;

    @FXML
    private TableColumn<Vehicule, String> tMarque;

    @FXML
    private TableColumn<Vehicule, String> tModel;

    @FXML
    private StackPane stackPane;

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    //Méthodes d'appel du formualire du véhicule
    @FXML
    void CallFormAddVehicule(MouseEvent event) {
        loadView("/org/example/gestioncovoiturage/Views/Vehicule/AddVehicule.fxml");
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

    //Fonction de recherche
    @FXML
    public void searchPassager(String actionEvent) {
        String searchTerm = cRecheerche.getText();
        ObservableList<Vehicule> foundVehicule = vehiculeRepository.searchVehicule(searchTerm);
        TabVehicule.setItems(foundVehicule);
    }

    // Affichage des véhicules
    public void printAllVehicule() {
        ObservableList<Vehicule> list = vehiculeRepository.getAllVehicule();

        tId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        tModel.setCellValueFactory(new PropertyValueFactory<>("modele"));
        tImmatriculation.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));

        // Configurer l'affichage du nom complet du conducteur dans tConducteur
        tConducteur.setCellValueFactory(cellData -> {
            Users user = cellData.getValue().getUtilisateur();
            return new SimpleStringProperty(user.getFullName());
        });

        TabVehicule.setItems(list);
    }

    //Suppression des véhicules
    @FXML
    void Delete(MouseEvent event) {
        Vehicule selectedVehicule = TabVehicule.getSelectionModel().getSelectedItem();
        if (selectedVehicule == null) {
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
            Long vehiculeId = selectedVehicule.getId();
            System.out.println("Tentative de suppression du véhicule avec l'ID : " + vehiculeId);
            vehiculeRepository.DeleteVehicule(vehiculeId);
            printAllVehicule();
        } else {
            System.out.println("Suppression annulée par l'utilisateur.");
        }
    }

    //Chargement du formulaire de modification
    @FXML
    void LoadFormEditVéhicule(MouseEvent event) {
        if (event.getClickCount() == 2) {
            try {
                Vehicule selectedVehicule = TabVehicule.getSelectionModel().getSelectedItem();
                if (selectedVehicule == null) {
                    System.out.println("Aucun utilisateur sélectionné");
                    return;
                }

                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource
                        ("/org/example/gestioncovoiturage/Views/Vehicule/EditVehicule.fxml"));
                Parent root = loader.load();

                EditVehiculeController  editVehiculeController = loader.getController();
                editVehiculeController.setStackPane(stackPane);

                editVehiculeController.initializeWithUser(selectedVehicule);

                stackPane.getChildren().clear();
                stackPane.getChildren().add(root);
                stackPane.setPadding(new Insets(10, 20, 10, 50));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
