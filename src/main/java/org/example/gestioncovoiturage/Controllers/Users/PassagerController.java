package org.example.gestioncovoiturage.Controllers.Users;

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
import javafx.util.Callback;
import org.example.gestioncovoiturage.HelloApplication;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Repository.UserRepository;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PassagerController implements Initializable {

    private UserRepository userRepository;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.userRepository = UserRepository.builder().build();

        printAllPassager();

        // Ajouter un écouteur de modifications pour le champ de recherche
        tRechercher.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchPassager(newValue);
            }
        });

    }

    @FXML
    private TableView<Users> TabPassagers;

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

    private StackPane stackPane;

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    public void printAllPassager() {
        ObservableList<Users> list = userRepository.getAllPassager();
        tId.setCellValueFactory(new PropertyValueFactory<Users, Long>("id"));
        tNom.setCellValueFactory(new PropertyValueFactory<Users, String>("nom"));
        tPrenom.setCellValueFactory(new PropertyValueFactory<Users, String>("prenom"));
        tTelephone.setCellValueFactory(new PropertyValueFactory<Users, String>("telephone"));
        tEmail.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Users, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Users, String> param) {
                // Utilisez la méthode getEmail() de Users pour obtenir l'email
                return new SimpleStringProperty(param.getValue().getEmail());
            }
        });
        TabPassagers.setItems(list);
    }

    @FXML
    void Delete(MouseEvent event) {

        if (TabPassagers.getSelectionModel().getSelectedItem() == null) {
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
        alert.setContentText("Voulez-vous supprimer ce passager ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            int id = Math.toIntExact(TabPassagers.getSelectionModel().getSelectedItem().getId());
            userRepository.DeletePassager(Long.valueOf(id));
            printAllPassager();
        } else {
            // ... user chose CANCEL or closed the dialog
        }

    }

    //Formulaire de modification
    @FXML
    void LaodEditPassager(MouseEvent event) {
        if (event.getClickCount() == 2) {
            try {
                Users selectedUser = TabPassagers.getSelectionModel().getSelectedItem();
                if (selectedUser == null) {
                    System.out.println("Aucun utilisateur sélectionné");
                    return;
                }

                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource
                        ("/org/example/gestioncovoiturage/Views/Users/EditUser.fxml"));
                Parent root = loader.load();

                EditUserController editUserController = loader.getController();
                editUserController.setStackPane(stackPane);

                editUserController.initializeWithUser(selectedUser);

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
        ObservableList<Users> foundUsers = userRepository.searchUsers(searchTerm);
        TabPassagers.setItems(foundUsers);
    }

}
