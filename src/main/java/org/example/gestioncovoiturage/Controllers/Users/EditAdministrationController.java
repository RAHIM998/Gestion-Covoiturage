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

public class EditAdministrationController implements Initializable {

    private UserRepository userRepository;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userRepository = UserRepository.builder().build();
    }

    @FXML
    private TextField cNom;

    @FXML
    private TextField tId;

    @FXML
    private TextField cPrenom;

    @FXML
    private TextField cTelephone;

    @FXML
    private StackPane stackPane;

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    //Méthodes
    public void initializeWithUser(Users user) {
        if (user != null) {
            tId.setText(user.getId()+"");
            cNom.setText(user.getNom());
            cPrenom.setText(user.getPrenom());
            cTelephone.setText(user.getTelephone());
        }
    }

    @FXML
    void Clear(ActionEvent event) {
        cNom.setText("");
        cPrenom.setText("");
        cTelephone.setText("");
        cNom.requestFocus();
    }

    @FXML
    void Update(ActionEvent event) {
        try {
            Long Id = Long.valueOf(tId.getText());
            String Nom = cNom.getText();
            String Prenom = cPrenom.getText();
            String Telephone = cTelephone.getText();

            if (Id == 0 || Nom.isEmpty() || Prenom.isEmpty() || Telephone.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Information", "Champs obligatoire", "Tous les champs sont obligatoires !");
                return;
            }

            Users user;
            user = new Users(Nom, Prenom, Telephone, RoleUser.ADMIN);
            user.setId(Id);

            userRepository.UpdateUser(user);
            Clear(event);

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



}
