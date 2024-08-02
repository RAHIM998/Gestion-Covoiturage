package org.example.gestioncovoiturage.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import lombok.Builder;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.Models.Vehicule;

import java.util.List;

@Builder
public class VehiculeRepository {

    // Création des véhicules
    public Vehicule addVehicule(Vehicule vehicule) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        boolean success = false;

        try {
            entityTransaction.begin();

            // Vérifiez si l'utilisateur a déjà un véhicule
            String hql = "SELECT COUNT(v) FROM Vehicule v WHERE v.utilisateur = :chauffeur";
            Long count = (Long) entityManager.createQuery(hql)
                    .setParameter("chauffeur", vehicule.getUtilisateur())
                    .getSingleResult();

            if (count > 0) {
                // Si l'utilisateur a déjà un véhicule, affichez une alerte ou gérez l'erreur
                throw new Exception("L'utilisateur sélectionné a déjà un véhicule.");
            }

            // Ajoutez le véhicule
            entityManager.persist(vehicule);
            entityTransaction.commit();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            // Gestion de l'exception et affichage de l'alerte
            throw new RuntimeException("Erreur lors de l'ajout du véhicule : " + e.getMessage());
        } finally {
            entityManager.close();
        }

        if (!success) {
            throw new RuntimeException("L'ajout du véhicule a échoué.");
        }
        return vehicule;
    }


    //Lister les données de la table vehcule
    public ObservableList<Vehicule> getAllVehicule() {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Vehicule> vehiculeObservableList = FXCollections.observableArrayList();

        try {

            TypedQuery<Vehicule> query = entityManager.createQuery(
                    "SELECT v FROM Vehicule v", Vehicule.class
            );
            List<Vehicule> vehicules = query.getResultList();

            vehiculeObservableList.addAll(vehicules);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return vehiculeObservableList;
    }

    //Recherche de véhicule
    public ObservableList<Vehicule> searchVehicule(String searchTerm) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Vehicule> foundVehicule = FXCollections.observableArrayList();

        try {

            TypedQuery<Vehicule> query = entityManager.createQuery(
                    "SELECT u FROM Vehicule u  WHERE " +
                            "LOWER(u.marque) LIKE LOWER(:searchTerm) " +
                            "OR LOWER(u.modele) LIKE LOWER(:searchTerm) " +
                            "OR LOWER(u.immatriculation) LIKE LOWER(:searchTerm) ", Vehicule.class
            );
            query.setParameter("searchTerm", "%" + searchTerm + "%");

            List<Vehicule> vehicules = query.getResultList();

            foundVehicule.addAll(vehicules);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return foundVehicule;
    }

    //Suppression des véhicule
    public void DeleteVehicule(Long vehiculeId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();

        Vehicule vehicule = entityManager.find(Vehicule.class, vehiculeId);

        if (vehicule != null) {
            entityManager.remove(vehicule);
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    //Modification des véhicues
    public boolean UpdateVehicule(Vehicule vehicule) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        boolean success = false;
        try {
            entityManager.getTransaction().begin();

            // Vérifiez si le véhicule à mettre à jour existe
            Vehicule existingVehicule = entityManager.find(Vehicule.class, vehicule.getId());

            if (existingVehicule != null) {
                // Vérifiez si l'utilisateur sélectionné a déjà un véhicule
                String hql = "SELECT COUNT(v) FROM Vehicule v WHERE v.utilisateur = :chauffeur AND v.id != :id";
                Long count = (Long) entityManager.createQuery(hql)
                        .setParameter("chauffeur", vehicule.getUtilisateur())
                        .setParameter("id", vehicule.getId())
                        .getSingleResult();

                if (count > 0) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur avec voiture", "L'utilisateur sélectionné a déjà une voiture. Veuillez en sélectionner un autre.");
                    entityManager.getTransaction().rollback();
                    return false;
                }

                // Mise à jour des informations du véhicule
                existingVehicule.setMarque(vehicule.getMarque());
                existingVehicule.setModele(vehicule.getModele());
                existingVehicule.setImmatriculation(vehicule.getImmatriculation());
                existingVehicule.setUtilisateur(vehicule.getUtilisateur());

                entityManager.getTransaction().commit();
                success = true;
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Véhicule non trouvé", "Le véhicule avec l'ID fourni n'a pas été trouvé.");
                entityManager.getTransaction().rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour", "Une erreur est survenue lors de la mise à jour du véhicule.");
        } finally {
            entityManager.close();
        }
        return success;
    }

    public void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
