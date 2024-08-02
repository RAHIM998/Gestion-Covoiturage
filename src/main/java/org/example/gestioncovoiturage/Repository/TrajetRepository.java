package org.example.gestioncovoiturage.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import lombok.Builder;
import org.example.gestioncovoiturage.Models.Trajet;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Models.Vehicule;

import java.util.List;

@Builder
public class TrajetRepository {

    // Ajout d'un trajet
    public Trajet addTrajet(Trajet trajet) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            entityManager.persist(trajet);
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
        return trajet;
    }


    //Lister les données de la table Trajets
    public ObservableList<Trajet> getAllTraject() {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Trajet> trajetObservableList = FXCollections.observableArrayList();

        try {

            TypedQuery<Trajet> query = entityManager.createQuery(
                    "SELECT v FROM Trajet v", Trajet.class
            );
            List<Trajet> trajets = query.getResultList();

            trajetObservableList.addAll(trajets);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return trajetObservableList;
    }

    //Recherche de trajets
    public ObservableList<Trajet> searchTraject(String searchTerm) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Trajet> foundTraject = FXCollections.observableArrayList();

        try {
            // Création de la requête avec une condition adaptée pour les dates
            String queryStr = "SELECT t FROM Trajet t " +
                    "WHERE LOWER(t.villeDepart) LIKE LOWER(:searchTerm) " +
                    "OR LOWER(t.villeArrivee) LIKE LOWER(:searchTerm) " +
                    "OR CAST(t.dateDepart AS string) LIKE LOWER(:searchTerm) " +
                    "OR LOWER(t.conducteur.nom) LIKE LOWER(:searchTerm)"; // Exemple pour conducteur

            TypedQuery<Trajet> query = entityManager.createQuery(queryStr, Trajet.class);

            query.setParameter("searchTerm", "%" + searchTerm.toLowerCase() + "%");

            List<Trajet> trajets = query.getResultList();
            foundTraject.addAll(trajets);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return foundTraject;
    }

    //Modification des trajets
    public boolean UpdateTrajet(Trajet trajet) {
        if (trajet == null || trajet.getId() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "ID manquant", "Le trajet à mettre à jour n'a pas d'ID valide.");
            return false;
        }

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        boolean updated = false;

        try {
            entityManager.getTransaction().begin();

            // Rechercher l'objet Trajet à mettre à jour
            Trajet existingTrajet = entityManager.find(Trajet.class, trajet.getId());

            if (existingTrajet != null) {
                // Mise à jour des champs du trajet existant
                existingTrajet.setVilleDepart(trajet.getVilleDepart());
                existingTrajet.setVilleArrivee(trajet.getVilleArrivee());
                existingTrajet.setDateDepart(trajet.getDateDepart());
                existingTrajet.setPrix(trajet.getPrix());
                existingTrajet.setConducteur(trajet.getConducteur());

                // Commit des changements
                entityManager.getTransaction().commit();
                updated = true;
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Trajet non trouvé", "Le trajet avec l'ID fourni n'a pas été trouvé.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour", "Une erreur est survenue lors de la mise à jour du trajet.");
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return updated;
    }

    // Méthode pour afficher les alertes
    public void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //Suppression des trajets
    public void DeleteTrajet(Long trajetId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();

        Trajet trajet = entityManager.find(Trajet.class, trajetId);

        if (trajet != null) {
            entityManager.remove(trajet);
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

}
