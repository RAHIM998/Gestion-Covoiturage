package org.example.gestioncovoiturage.Controllers.Compte;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.example.gestioncovoiturage.Controllers.Users.EditAdministrationController;
import org.example.gestioncovoiturage.HelloApplication;
import org.example.gestioncovoiturage.Models.Compte;
import org.example.gestioncovoiturage.Repository.ComptesRepository;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ComptesControllers implements Initializable {

    ComptesRepository comptesRepository;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.comptesRepository = ComptesRepository.builder().build();

        printAllComptes();

        // Ajouter un écouteur de modifications pour le champ de recherche
        tRechercher.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchCompte(newValue);
            }
        });
    }

    //Declaration
    @FXML
    private TableView<Compte> TabCompte;

    @FXML
    private TableColumn<Compte, String> tEmail;

    @FXML
    private TableColumn<Compte, Long> tId;

    @FXML
    private TextField tRechercher;

    @FXML
    private StackPane stackPane;

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    //Méthodes de la classe
    public void printAllComptes() {
        ObservableList<Compte> list = comptesRepository.getAllComptes();
        tId.setCellValueFactory(new PropertyValueFactory<Compte, Long>("id"));
        tEmail.setCellValueFactory(new PropertyValueFactory<Compte, String>("email"));

        TabCompte.setItems(list);
    }

    //Fonction de recherche
    @FXML
    public void searchCompte(String actionEvent) {
        String searchTerm = tRechercher.getText();
        ObservableList<Compte> foundUsers = comptesRepository.searchComptes(searchTerm);
        TabCompte.setItems(foundUsers);
    }

    @FXML
    void CallFormEditCompte(MouseEvent event) {
        if (event.getClickCount() == 2) {
            try {
                Compte selectedCompte = TabCompte.getSelectionModel().getSelectedItem();
                if (selectedCompte == null) {
                    System.out.println("Aucun compte sélectionné");
                    return;
                }

                // Chargement du fichier FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gestioncovoiturage/Views/Compte/EditCompte.fxml"));
                Parent root = loader.load();

                // Récupération du contrôleur
                EditCompteController editCompteController = loader.getController();
                editCompteController.setStackPane(stackPane);
                editCompteController.initializeWithUser(selectedCompte);

                // Affichage du nouveau contenu
                stackPane.getChildren().clear();
                stackPane.getChildren().add(root);
                stackPane.setPadding(new Insets(10, 20, 10, 50));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void Delete(ActionEvent event) {
        Compte selectedCompte = TabCompte.getSelectionModel().getSelectedItem();
        if (selectedCompte == null) {
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
            Long compteIdId = selectedCompte.getId();
            System.out.println("Tentative de suppression du véhicule avec l'ID : " + compteIdId);
            comptesRepository.DeleteCompte(compteIdId);
            printAllComptes();
        } else {
            System.out.println("Suppression annulée par l'utilisateur.");
        }

    }
}
