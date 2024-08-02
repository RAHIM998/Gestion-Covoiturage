package org.example.gestioncovoiturage.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.gestioncovoiturage.Controllers.Compte.ComptesControllers;
import org.example.gestioncovoiturage.Controllers.Reservations.ReservationController;
import org.example.gestioncovoiturage.Controllers.Trajet.TrajetController;
import org.example.gestioncovoiturage.Controllers.Users.ChauffeurController;
import org.example.gestioncovoiturage.Controllers.Users.ListAdministrationController;
import org.example.gestioncovoiturage.Controllers.Users.ListUsersController;
import org.example.gestioncovoiturage.Controllers.Users.PassagerController;
import org.example.gestioncovoiturage.Controllers.Vehicule.VehiculeController;
import org.example.gestioncovoiturage.HelloApplication;
import org.example.gestioncovoiturage.Models.Session;
import org.example.gestioncovoiturage.Models.Users;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class WindowPrincipalController implements Initializable {

    @FXML
    private StackPane stackPane;
    public StackPane getStackPane() {
        return stackPane;
    }


    @FXML
    private Pane ContentApp;

    @FXML
    private Button btnAccueil;

    @FXML
    private Button btnChauffeurs;

    @FXML
    private Button btnPassagers;

    @FXML
    private Button btnReservations;

    @FXML
    private Button btnTrajets;

    @FXML
    private Button btnVehicules;

    @FXML
    private Button btnRapport;

    @FXML
    private Button btnAdministration;

    @FXML
    private Button btnAddUser;

    @FXML
    private Button btnDeconnexion;

    private Users currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configuration initiale
    }



    public void setCurrentUser(Users user) {
        this.currentUser = user;
        configureAccess();
    }

    // Configuration des accès
    private void configureAccess() {
        if (currentUser != null) {

            String role = String.valueOf(currentUser.getRole());

            if (role.equals("PASSAGER")) {
                setButtonsVisibility(false);
                btnAccueil.setVisible(true);
                btnReservations.setVisible(true);
                btnTrajets.setVisible(true);
            }
            // Gérer la visibilité pour les administrateurs
            else if (role.equals("ADMIN")) {
                setButtonsVisibility(true);
            }
        } else {
            setButtonsVisibility(false);
        }
    }

    private void setButtonsVisibility(boolean visible) {
        btnAccueil.setVisible(visible);
        btnChauffeurs.setVisible(visible);
        btnPassagers.setVisible(visible);
        btnReservations.setVisible(visible);
        btnTrajets.setVisible(visible);
        btnVehicules.setVisible(visible);
        btnRapport.setVisible(visible);
        btnAdministration.setVisible(visible);
        btnAddUser.setVisible(visible);

        btnAccueil.setManaged(visible);
        btnChauffeurs.setManaged(visible);
        btnPassagers.setManaged(visible);
        btnReservations.setManaged(visible);
        btnTrajets.setManaged(visible);
        btnVehicules.setManaged(visible);
        btnRapport.setManaged(visible);
        btnAdministration.setManaged(visible);
        btnAddUser.setManaged(visible);
    }

    @FXML
    void OpenAccueil(MouseEvent event) {
        // Implémentation pour ouvrir l'accueil
    }

    @FXML
    private void OpenChauffeurs() {
        try {
            // Assurez-vous que le chemin est correct et que le fichier FXML existe
            URL fxmlLocation = getClass().getResource("/org/example/gestioncovoiturage/Views/Users/ListUsers.fxml");
            if (fxmlLocation == null) {
                throw new IllegalStateException("FXML file not found: /org/example/gestioncovoiturage/Views/Users/ListUsers.fxml");
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            ChauffeurController controller = loader.getController();
            controller.setStackPane(stackPane);
            stackPane.getChildren().clear();
            stackPane.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OpenPassagers(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/org/example/gestioncovoiturage/Views/Users/Passagers.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et passer le StackPane
            PassagerController passagerController = loader.getController();
            passagerController.setStackPane(stackPane);

            stackPane.getChildren().clear();
            stackPane.getChildren().add(root);
            stackPane.setPadding(new Insets(10, 10, 10, 40));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OpenReservations(MouseEvent event) {
        try {
            URL fxmlLocation = getClass().getResource("/org/example/gestioncovoiturage/Views/Reservations/Reservations.fxml");
            if (fxmlLocation == null) {
                throw new IllegalStateException("FXML file not found: /org/example/gestioncovoiturage/Views/Reservations/Reservations.fxml");
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            ReservationController controller = loader.getController();
            controller.setStackPane(stackPane);
            stackPane.getChildren().clear();
            stackPane.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OpenTrajets(MouseEvent event) {
        try {
            URL fxmlLocation = getClass().getResource("/org/example/gestioncovoiturage/Views/Trajets/Trajets.fxml");
            if (fxmlLocation == null) {
                throw new IllegalStateException("FXML file not found: /org/example/gestioncovoiturage/Views/Trajets/Trajets.fxml");
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            TrajetController controller = loader.getController();
            controller.setStackPane(stackPane);
            stackPane.getChildren().clear();
            stackPane.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }       }

    @FXML
    void OpenVehicules(MouseEvent event) {
        try {
            // Assurez-vous que le chemin est correct et que le fichier FXML existe
            URL fxmlLocation = getClass().getResource("/org/example/gestioncovoiturage/Views/Vehicule/Vehicule.fxml");
            if (fxmlLocation == null) {
                throw new IllegalStateException("FXML file not found: /org/example/gestioncovoiturage/Views/Vehicule/Vehicule.fxml");
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            VehiculeController controller = loader.getController();
            controller.setStackPane(stackPane);
            stackPane.getChildren().clear();
            stackPane.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OpenRapport(MouseEvent event) {
        // Implémentation pour ouvrir le rapport
    }


    @FXML
    void OpenAdministration(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/org/example/gestioncovoiturage/Views/Users/ListAdministration.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et passer le StackPane
            ListAdministrationController listAdministrationController = loader.getController();
            listAdministrationController.setStackPane(stackPane);

            stackPane.getChildren().clear();
            stackPane.getChildren().add(root);
            stackPane.setPadding(new Insets(10, 10, 10, 40));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OpenComptes(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/org/example/gestioncovoiturage/Views/Compte/Comptes.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et passer le StackPane
            ComptesControllers comptesControllers = loader.getController();
            comptesControllers.setStackPane(stackPane);

            stackPane.getChildren().clear();
            stackPane.getChildren().add(root);
            stackPane.setPadding(new Insets(10, 10, 10, 40));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OpenAddUser(MouseEvent event) {
        loadView("/org/example/gestioncovoiturage/Views/Users/AddUser.fxml", new Insets(10, 30, 5, 30));
    }


    private void loadView(String fxmlPath, Insets padding) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlPath));
            Parent root = loader.load();
            stackPane.getChildren().clear();
            stackPane.getChildren().add(root);
            stackPane.setPadding(padding);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Logout(MouseEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de déconnexion");
            alert.setContentText("Vous êtes sûr de vouloir vous déconnecter ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Session.getInstance().setCurrentUser(null);

                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) stackPane.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Connexion");
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
