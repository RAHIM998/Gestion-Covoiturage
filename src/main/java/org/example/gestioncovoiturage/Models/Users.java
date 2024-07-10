package org.example.gestioncovoiturage.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
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
    public Users() {
        this.role = RoleUser.PASSAGER;
    }

    // S'assurer du remplissage du role avant de persister
    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = RoleUser.PASSAGER;
        }
    }

    // Relations entre les conducteurs et véhicule
    @OneToOne(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private Vehicule vehicule;

    // Relations entre Conducteurs et trajet
    @ManyToMany(mappedBy = "conducteurs", cascade = CascadeType.ALL)
    private List<Trajet> trajets;

    // Relation entre passager et reservation
    @OneToMany(mappedBy = "passager")
    private List<Reservation> reservations;

    // Relation entre users et compte
    @OneToOne(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private Compte compte;
}
