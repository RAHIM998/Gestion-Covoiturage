package org.example.gestioncovoiturage.Controllers.Users;

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
import org.example.gestioncovoiturage.HelloApplication;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Repository.UserRepository;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChauffeurController implements Initializable {

    private UserRepository userRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.userRepository = UserRepository.builder().build();

        printAllChauffeur();

        // Ajouter un écouteur de modifications pour le champ de recherche
        tRechercher.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchPassager(newValue);
            }
        });

    }

    @FXML
    private TableView<Users> TabChauffeur;

    @FXML
    private TableColumn<Users, Long> tId;

    @FXML
    private TableColumn<Users, String> tNom;

    @FXML
    private TableColumn<Users, String> tPrenom;

    @FXML
    private TextField tRechercher;

    @FXML
    private TableColumn<Users, String> tTelephone;

    @FXML
    private StackPane stackPane;

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }
    @FXML
    private void CallFormAddChauffeur() {
        loadView("/org/example/gestioncovoiturage/Views/Users/AddChauffeur.fxml");
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

    public void printAllChauffeur() {
        ObservableList<Users> list = userRepository.getAllConducteurs();
        tId.setCellValueFactory(new PropertyValueFactory<Users, Long>("id"));
        tNom.setCellValueFactory(new PropertyValueFactory<Users, String>("nom"));
        tPrenom.setCellValueFactory(new PropertyValueFactory<Users, String>("prenom"));
        tTelephone.setCellValueFactory(new PropertyValueFactory<Users, String>("telephone"));

        TabChauffeur.setItems(list);
    }

    @FXML
    void Delete(ActionEvent event) {

        if (TabChauffeur.getSelectionModel().getSelectedItem() == null) {
            Alert noSelectionAlert = new Alert(Alert.AlertType.WARNING);
            noSelectionAlert.setTitle("Aucun élément sélectionné");
            noSelectionAlert.setHeaderText(null);
            noSelectionAlert.setContentText("Veuillez sélectionner un chauffeur avant de tenter de le supprimer.");
            noSelectionAlert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Suppression !");
        alert.setContentText("Voulez-vous supprimer ce chauffeur ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            int id = Math.toIntExact(TabChauffeur.getSelectionModel().getSelectedItem().getId());
            if (id != 0){
                userRepository.DeletePassager(Long.valueOf(id));
                printAllChauffeur();
            }else {
                alert.setAlertType(Alert.AlertType.ERROR);
            }
        } else {
            System.out.println("Suppression non confirmé !");
        }
    }

    //Chargment des données du formumlaire de modification
    @FXML
    void LaodEditChauffeur(MouseEvent event) {
        if (event.getClickCount() == 2) {
            try {
                Users selectedUser = TabChauffeur.getSelectionModel().getSelectedItem();
                if (selectedUser == null) {
                    System.out.println("Aucun utilisateur sélectionné");
                    return;
                }

                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource
                        ("/org/example/gestioncovoiturage/Views/Users/EditChauffeur.fxml"));
                Parent root = loader.load();

                EditChauffeurController editChauffeurController = loader.getController();
                editChauffeurController.setStackPane(stackPane);

                editChauffeurController.initializeWithUser(selectedUser);

                stackPane.getChildren().clear();
                stackPane.getChildren().add(root);
                stackPane.setPadding(new Insets(10, 20, 10, 50));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Fonction de recherche
    @FXML
    public void searchPassager(String actionEvent) {
        String searchTerm = tRechercher.getText();
        ObservableList<Users> foundUsers = userRepository.searchChauffeurs(searchTerm);
        TabChauffeur.setItems(foundUsers);
    }
}
