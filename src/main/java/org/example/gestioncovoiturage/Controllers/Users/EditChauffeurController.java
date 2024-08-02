package org.example.gestioncovoiturage.Controllers.Users;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.example.gestioncovoiturage.Models.RoleUser;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Repository.UserRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class EditChauffeurController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userRepository = UserRepository.builder().build();
    }

    @FXML
    private TextField cid;

    @FXML
    private TextField cnom;

    @FXML
    private TextField cprenom;

    @FXML
    private TextField ctelephone;

    private StackPane stackPane;

    private UserRepository userRepository;
    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    public void initializeWithUser(Users user) {
        if (user != null) {
            cid.setText(user.getId()+"");
            cnom.setText(user.getNom());
            cprenom.setText(user.getPrenom());
            ctelephone.setText(user.getTelephone());
        }
    }

    @FXML
    void Update(ActionEvent event) {
        try {
            Long Id = Long.valueOf(cid.getText());
            String Nom = cnom.getText();
            String Prenom = cprenom.getText();
            String Telephone = ctelephone.getText();

            if (Id == 0 || Nom.isEmpty() || Prenom.isEmpty() || Telephone.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Information", "Champs obligatoire", "Tous les champs sont obligatoires !");
                return;
            }

            Users user;
            user = new Users(Prenom, Nom, Telephone, RoleUser.CONDUCTEUR);
            user.setId(Id);

            userRepository.UpdateUser(user);
            clear(event);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Mise à jour réussie", "L'utilisateur a été mis à jour avec succès.");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format ID invalide", "L'ID doit être un nombre valide.");
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
    void clear(ActionEvent event) {
        cnom.clear();
        cprenom.clear();
        ctelephone.clear();

    }

}
