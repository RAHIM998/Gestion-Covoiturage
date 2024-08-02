package org.example.gestioncovoiturage.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Builder;
import org.example.gestioncovoiturage.Models.Reservation;
import org.example.gestioncovoiturage.Models.Trajet;
import org.example.gestioncovoiturage.Models.Users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public class ReservationRepository {

    // Ajout d'une reservation

    // Ajout d'une réservation
    public void AddReservation(Reservation reservation) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            entityManager.persist(reservation);
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    // Vérifier si un passager a déjà une réservation pour un trajet
    public Reservation findReservationByPassagerAndTrajetAndDate(Users passager, Trajet trajet, LocalDate date) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager.createQuery("SELECT r FROM Reservation r WHERE r.passager = :passager AND r.trajet = :trajet AND DATE(r.dateReservation) = :date", Reservation.class)
                    .setParameter("passager", passager)
                    .setParameter("trajet", trajet)
                    .setParameter("date", date)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            entityManager.close();
        }
    }

    public ObservableList<Reservation> getAllReservation() {

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Reservation> reservationObservableList = FXCollections.observableArrayList();

        try {
            TypedQuery<Reservation> query = entityManager.createQuery(
                    "SELECT r FROM Reservation r", Reservation.class
            );
            List<Reservation> reservations = query.getResultList();
            reservationObservableList.addAll(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return reservationObservableList;
    }

    //Suppression des trajets
    public void DeleteReservation(Long reservationId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();

        Reservation reservation = entityManager.find(Reservation.class, reservationId);

        if (reservation != null) {
            entityManager.remove(reservation);
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    // Récupérer les réservations par date
    public ObservableList<Reservation> getReservationsByDate(LocalDateTime startDate, LocalDateTime endDate) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Reservation> reservationObservableList = FXCollections.observableArrayList();

        try {
            TypedQuery<Reservation> query = entityManager.createQuery(
                    "SELECT r FROM Reservation r WHERE r.dateReservation BETWEEN :startDate AND :endDate", Reservation.class
            );
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            List<Reservation> reservations = query.getResultList();
            reservationObservableList.addAll(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return reservationObservableList;
    }

    // Récupérer les réservations passées
    public ObservableList<Reservation> getReservationsBeforeDate(LocalDateTime date) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Reservation> reservationObservableList = FXCollections.observableArrayList();

        try {
            TypedQuery<Reservation> query = entityManager.createQuery(
                    "SELECT r FROM Reservation r WHERE r.dateReservation < :date", Reservation.class
            );
            query.setParameter("date", date);
            List<Reservation> reservations = query.getResultList();
            reservationObservableList.addAll(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return reservationObservableList;
    }

    // Récupérer les réservations futures
    public ObservableList<Reservation> getReservationsAfterDate(LocalDateTime date) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        ObservableList<Reservation> reservationObservableList = FXCollections.observableArrayList();

        try {
            TypedQuery<Reservation> query = entityManager.createQuery(
                    "SELECT r FROM Reservation r WHERE r.dateReservation > :date", Reservation.class
            );
            query.setParameter("date", date);
            List<Reservation> reservations = query.getResultList();
            reservationObservableList.addAll(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return reservationObservableList;
    }
}
