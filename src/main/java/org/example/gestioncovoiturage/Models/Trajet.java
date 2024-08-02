package org.example.gestioncovoiturage.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "trajets")
public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String villeDepart;

    @Column(nullable = false)
    private String villeArrivee;

    @Column(nullable = false)
    private LocalDateTime dateDepart;

    @Column(nullable = false)
    private Integer prix; //

    // Relation entre un trajet et le conducteur
    @ManyToOne
    @JoinColumn(name = "conducteur_id")
    private Users conducteur;

    // Relations entre un trajet et une réservation
    @OneToMany(mappedBy = "trajet")
    private List<Reservation> reservations;

    // Constructeur avec paramètres
    public Trajet(String villeDepart, String villeArrivee, LocalDate dateDepart, Integer prix, Users conducteur) {
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.dateDepart = dateDepart.atStartOfDay(); // Convertit LocalDate en LocalDateTime
        this.prix = prix;
        this.conducteur = conducteur;
    }

    // Constructeur par défaut
    public Trajet() {
    }

    public Users getConducteur() {
        return conducteur;
    }

}
