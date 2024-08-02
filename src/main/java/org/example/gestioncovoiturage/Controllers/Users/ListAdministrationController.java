package org.example.gestioncovoiturage.Controllers.Users;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.util.Callback;
import org.example.gestioncovoiturage.HelloApplication;
import org.example.gestioncovoiturage.Models.Trajet;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Repository.UserRepository;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListAdministrationController implements Initializable {

    private UserRepository userRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.userRepository = UserRepository.builder().build();

        printAllAdministrateur();

        // Ajouter un écouteur de modifications pour le champ de recherche
        tRechercher.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchAdmin(newValue);
            }
        });
    }

    @FXML
    private TableView<Users> TabAdmin;

    @FXML
    private TableColumn<Users, String> tEmail;

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

    //Méthodes de la classe
    public void printAllAdministrateur() {
        ObservableList<Users> list = userRepository.getAllAdmin();
        tId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        tNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        tEmail.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Users, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Users, String> param) {
                // Utilisez la méthode getEmail() de Users pour obtenir l'email
                return new SimpleStringProperty(param.getValue().getEmail());
            }
        });
        TabAdmin.setItems(list);
    }

    //Fonction de recherche
    @FXML
    public void searchAdmin(String actionEvent) {
        String searchTerm = tRechercher.getText();
        ObservableList<Users> foundUsers = userRepository.searchAdmin(searchTerm);
        TabAdmin.setItems(foundUsers);
    }

    @FXML
    void CallFormModifAdmin(MouseEvent event) {
        if (event.getClickCount() == 2) {
            try {
                Users selectedAdmin = TabAdmin.getSelectionModel().getSelectedItem();
                if (selectedAdmin == null) {
                    System.out.println("Aucun utilisateur sélectionné");
                    return;
                }

                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource
                        ("/org/example/gestioncovoiturage/Views/Users/EditAdministration.fxml"));
                Parent root = loader.load();

                EditAdministrationController editAdministrationController = loader.getController();
                editAdministrationController.setStackPane(stackPane);

                editAdministrationController.initializeWithUser(selectedAdmin);

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
        Users selectedAdmin = TabAdmin.getSelectionModel().getSelectedItem();
        if (selectedAdmin == null) {
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
            Long adminId = selectedAdmin.getId();
            System.out.println("Tentative de suppression du véhicule avec l'ID : " + adminId);
            userRepository.DeletePassager(adminId);
            printAllAdministrateur();
        } else {
            System.out.println("Suppression annulée par l'utilisateur.");
        }
    }

}
