package org.example.gestioncovoiturage.Models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vehicules")
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private String modele;

    @Column(nullable = false, unique = true)
    private String immatriculation;
    
    //Constructeurs 
    public Vehicule(String marque, String modele, String immatriculation, Users utilisateur) {
        this.marque = marque;
        this.modele = modele;
        this.immatriculation = immatriculation;
        this.utilisateur = utilisateur;
    }

    // Relation du véhicule à l'utilisateur
    @OneToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Users utilisateur;

    public Vehicule() {

    }


    public Users getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Users utilisateur) {
        this.utilisateur = utilisateur;
    }
}
