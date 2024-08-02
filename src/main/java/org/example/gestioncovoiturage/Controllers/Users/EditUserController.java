package org.example.gestioncovoiturage.Controllers.Users;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.example.gestioncovoiturage.Models.Compte;
import org.example.gestioncovoiturage.Models.RoleUser;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Repository.UserRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class EditUserController implements Initializable {


    @FXML
    private TextField eId;

    @FXML
    private TextField eNom;

    @FXML
    private TextField ePrenom;

    @FXML
    private TextField eTelephone;

    private StackPane stackPane;

    private UserRepository userRepository;
    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userRepository = UserRepository.builder().build();
    }

    public void initializeWithUser(Users user) {
        if (user != null) {
            eId.setText(user.getId()+"");
            eNom.setText(user.getNom());
            ePrenom.setText(user.getPrenom());
            eTelephone.setText(user.getTelephone());
        }
    }

    @FXML
    void ClearPassager(ActionEvent event) {


    }

    @FXML
    void Update(ActionEvent event) {
        try {
            Long Id = Long.valueOf(eId.getText());
            String Nom = eNom.getText();
            String Prenom = ePrenom.getText();
            String Telephone = eTelephone.getText();

            if (Id == 0 || Nom.isEmpty() || Prenom.isEmpty() || Telephone.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Information", "Champs obligatoire", "Tous les champs sont obligatoires !");
                return;
            }

            Users user;
            user = new Users(Nom, Prenom, Telephone, RoleUser.PASSAGER);
            user.setId(Id);

            userRepository.UpdateUser(user);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Mise à jour réussie", "L'utilisateur a été mis à jour avec succès.");

            PassagerController passagerController = new PassagerController();
            passagerController.printAllPassager();

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
}
