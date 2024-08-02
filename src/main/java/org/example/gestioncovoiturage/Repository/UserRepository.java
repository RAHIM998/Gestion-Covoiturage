package org.example.gestioncovoiturage.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import lombok.Builder;
import org.example.gestioncovoiturage.Models.Compte;
import org.example.gestioncovoiturage.Models.RoleUser;
import org.example.gestioncovoiturage.Models.Users;
import org.example.gestioncovoiturage.util.PasswordUtil;

import java.util.List;

@Builder
public class UserRepository {

    //Création des utilisateurs
    public Users addUser(Users user) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            entityManager.persist(user);
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
        return user;
    }

    // Authentification
    public Users authenticateUser(String email, String password) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        Users user = null;

        try {

            String jpql = "SELECT c FROM Compte c JOIN FETCH c.utilisateur u WHERE c.email = :email";
            TypedQuery<Compte> query = entityManager.createQuery(jpql, Compte.class);
            query.setParameter("email", email);

            Compte compte = query.getSingleResult();

            if (compte != null && PasswordUtil.checkPassword(password, compte.getPassword())) {
                user = compte.getUtilisateur();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
        return user;
    }

    //Lister les passagers
    public ObservableList<Users> getAllPassager() {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Users> passagers = FXCollections.observableArrayList();

        try {
            RoleUser role = RoleUser.PASSAGER;

            TypedQuery<Users> query = entityManager.createQuery(
                    "SELECT u FROM Users u JOIN FETCH u.compte c WHERE u.role = :role", Users.class
            );
            query.setParameter("role", role);

            passagers.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return passagers;
    }

    //Lister les administrateurs
    public ObservableList<Users> getAllAdmin() {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Users> passagers = FXCollections.observableArrayList();

        try {
            RoleUser role = RoleUser.ADMIN;

            TypedQuery<Users> query = entityManager.createQuery(
                    "SELECT u FROM Users u JOIN FETCH u.compte c WHERE u.role = :role", Users.class
            );
            query.setParameter("role", role);
            passagers.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return passagers;
    }

    //Lister les chauffeurs
    public ObservableList<Users> getAllConducteurs() {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Users> conducteursObservableList = FXCollections.observableArrayList();

        try {
            RoleUser role = RoleUser.CONDUCTEUR;

            TypedQuery<Users> query = entityManager.createQuery(
                    "SELECT u FROM Users u WHERE u.role = :role", Users.class
            );
            query.setParameter("role", role);
            List<Users> conducteurs = query.getResultList();

            conducteursObservableList.addAll(conducteurs);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return conducteursObservableList;
    }

    //Suppression d'un passager
    public void DeletePassager(Long utilisateurId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();

        Users utilisateur = entityManager.find(Users.class, utilisateurId);

        if (utilisateur != null) {

            Compte compte = utilisateur.getCompte();
            if (compte != null) {
                entityManager.remove(compte);
            }

            entityManager.remove(utilisateur);
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    //Modification dans la bd
    public void UpdateUser(Users user) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();

            Users existingUser = entityManager.find(Users.class, user.getId());

            if (existingUser != null) {
                existingUser.setNom(user.getNom());
                existingUser.setPrenom(user.getPrenom());
                existingUser.setTelephone(user.getTelephone());

                entityManager.getTransaction().commit();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé", "L'utilisateur avec l'ID fourni n'a pas été trouvé.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour", "Une erreur est survenue lors de la mise à jour de l'utilisateur.");
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

    // Méthode pour rechercher des utilisateurs par nom, prénom ou email
    public ObservableList<Users> searchUsers(String searchTerm) {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Users> foundUsers = FXCollections.observableArrayList();

        try {
            RoleUser role = RoleUser.PASSAGER;

            TypedQuery<Users> query = entityManager.createQuery(
                    "SELECT u FROM Users u JOIN u.compte c WHERE " +
                            "(LOWER(u.nom) LIKE LOWER(:searchTerm) " +
                            "OR LOWER(u.prenom) LIKE LOWER(:searchTerm) " +
                            "OR LOWER(c.email) LIKE LOWER(:searchTerm)) " +
                            "AND u.role = :role", Users.class
            );

            query.setParameter("searchTerm", "%" + searchTerm + "%");
            query.setParameter("role", role);
            List<Users> users = query.getResultList();

            foundUsers.addAll(users);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return foundUsers;
    }

    //Recherche des chauffeur
    public ObservableList<Users> searchChauffeurs(String searchTerm) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Users> foundUsers = FXCollections.observableArrayList();

        try {
            RoleUser role = RoleUser.CONDUCTEUR;

            TypedQuery<Users> query = entityManager.createQuery(
                    "SELECT u FROM Users u WHERE " +
                            "(LOWER(u.nom) LIKE LOWER(:searchTerm) " +
                            "OR LOWER(u.prenom) LIKE LOWER(:searchTerm) " +
                            "OR LOWER(u.telephone) LIKE LOWER(:searchTerm)) " +
                            "AND u.role = :role", Users.class
            );

            query.setParameter("searchTerm", "%" + searchTerm + "%");
            query.setParameter("role", role); // Ajout du paramètre rôle
            List<Users> users = query.getResultList();

            foundUsers.addAll(users);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return foundUsers;
    }

    //Recherche des administrateurs
    public ObservableList<Users> searchAdmin(String searchTerm) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Users> foundUsers = FXCollections.observableArrayList();

        try {
            RoleUser role = RoleUser.ADMIN;

            TypedQuery<Users> query = entityManager.createQuery(
                    "SELECT u FROM Users u JOIN u.compte c WHERE " +
                            "(LOWER(u.nom) LIKE LOWER(:searchTerm) " +
                            "OR LOWER(u.prenom) LIKE LOWER(:searchTerm) " +
                            "OR LOWER(u.telephone) LIKE LOWER(:searchTerm) " +
                            "OR LOWER(c.email) LIKE LOWER(:searchTerm)) " +
                            "AND u.role = :role", Users.class
            );

            query.setParameter("searchTerm", "%" + searchTerm + "%");
            query.setParameter("role", role);
            List<Users> users = query.getResultList();

            foundUsers.addAll(users);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return foundUsers;
    }

}
