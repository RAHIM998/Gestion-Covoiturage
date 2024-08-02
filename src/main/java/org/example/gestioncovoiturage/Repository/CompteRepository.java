package org.example.gestioncovoiturage.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.Builder;
import org.example.gestioncovoiturage.Models.Compte;
import org.example.gestioncovoiturage.Models.Users;

@Builder
public class CompteRepository {
    public void AddCompte(Compte compte){

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        //save
        entityManager.persist(compte);
        entityManager.getTransaction().commit();
        entityManager.close();
    }


}
