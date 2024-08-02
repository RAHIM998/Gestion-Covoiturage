package org.example.gestioncovoiturage.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String telephone;

    @Enumerated(EnumType.STRING)
    private RoleUser role;

    // Constructeur pour user
    public Users(String prenom, String nom, String telephone, RoleUser role) {
        this.prenom = prenom;
        this.nom = nom;
        this.telephone = telephone;
        this.role = role != null ? role : RoleUser.PASSAGER;
    }

    //Constructeur pour la création d'un passager
    public Users(String prenom, String nom, String telephone) {
        this.prenom = prenom;
        this.nom = nom;
        this.telephone = telephone;
    }

    public Users() {
    }

    // S'assurer du remplissage du role avant de persister
    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = RoleUser.PASSAGER;
        }
    }

    //Recupération de l'email de l'utilisateur
    public String getEmail() {
        if (compte != null) {
            return compte.getEmail();
        }
        return null;
    }

    // Relations entre les conducteurs et véhicule
    @OneToOne(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private Vehicule vehicule;

    // Relations entre Conducteurs et trajet
    @OneToMany(mappedBy = "conducteur", cascade = CascadeType.ALL)
    private List<Trajet> trajets;

    // Relation entre passager et reservation
    @OneToMany(mappedBy = "passager")
    private List<Reservation> reservations;

    // Relation entre users et compte
    @OneToOne(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private Compte compte;

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public String getFullName() {
        return this.nom + " " + this.prenom;
    }

}
