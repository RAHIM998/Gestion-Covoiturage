package org.example.gestioncovoiturage.Controllers.Compte;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.example.gestioncovoiturage.Models.Compte;
import org.example.gestioncovoiturage.Repository.ComptesRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCompteController implements Initializable {

    ComptesRepository comptesRepository;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.comptesRepository = ComptesRepository.builder().build();
    }

    @FXML
    private TextField cEmail;

    @FXML
    private TextField tId;


    @FXML
    private StackPane stackPane;

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    //Méthodes
    public void initializeWithUser(Compte compte) {
        if (compte != null) {
            tId.setText(compte.getId()+"");
            cEmail.setText(compte.getEmail());
        }
    }

    @FXML
    void Clear(ActionEvent event) {
        cEmail.setText("");
    }

    @FXML
    void Update(ActionEvent event) {
        try {
            Long Id = Long.valueOf(tId.getText());
            String Email = cEmail.getText();

            if (Id == 0 || Email.isEmpty() ){
                comptesRepository.showAlert(Alert.AlertType.INFORMATION, "Information", "Champs obligatoire", "Tous les champs sont obligatoires !");
                return;
            }

            Compte compte;
            compte = new Compte(Email);
            compte.setId(Id);

            comptesRepository.UpdateCompte(compte);
            Clear(event);

            comptesRepository.showAlert(Alert.AlertType.INFORMATION, "Succès", "Mise à jour réussie", "L'utilisateur a été mis à jour avec succès.");

        } catch (NumberFormatException e) {
            comptesRepository.showAlert(Alert.AlertType.ERROR, "Erreur", "Format ID invalide", "L'ID doit être un nombre valide.");
        }

    }

}
