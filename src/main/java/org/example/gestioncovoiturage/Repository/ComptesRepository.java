package org.example.gestioncovoiturage.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import lombok.Builder;
import org.example.gestioncovoiturage.Models.Compte;

import java.util.List;

@Builder
public class ComptesRepository {

    //Lister les chauffeurs
    public ObservableList<Compte> getAllComptes() {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Compte> comptesObservableList = FXCollections.observableArrayList();

        try {

            TypedQuery<Compte> query = entityManager.createQuery(
                    "SELECT u FROM Compte u ", Compte.class
            );

            List<Compte> comptes = query.getResultList();

            comptesObservableList.addAll(comptes);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return comptesObservableList;
    }

    // Méthode pour rechercher des comptes par email
    public ObservableList<Compte> searchComptes(String searchTerm) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Compte> foundComptes = FXCollections.observableArrayList();

        try {

            TypedQuery<Compte> query = entityManager.createQuery(
                    "SELECT u FROM Compte u WHERE " +
                            "LOWER(u.email) LIKE LOWER(:searchTerm) ", Compte.class
            );

            query.setParameter("searchTerm", "%" + searchTerm + "%");
            List<Compte> comptes = query.getResultList();

            foundComptes.addAll(comptes);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return foundComptes;
    }

    //Suppression d'un compte
    public void DeleteCompte(Long compteId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();

        Compte compte = entityManager.find(Compte.class, compteId);

        if (compte != null) {

            entityManager.remove(compte);

        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    //Modification dans la bd
    public void UpdateCompte(Compte compte) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();

            Compte existingCompte = entityManager.find(Compte.class, compte.getId());

            if (existingCompte != null) {

                existingCompte.setEmail(compte.getEmail());

                entityManager.getTransaction().commit();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Compte non trouvé", "L'compte avec l'ID fourni n'a pas été trouvé.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour", "Une erreur est survenue lors de la mise à jour du compte.");
        } finally {
            entityManager.close();
        }
    }

    public void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
