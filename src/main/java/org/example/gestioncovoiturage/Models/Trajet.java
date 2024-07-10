package org.example.gestioncovoiturage.Models;

import jakarta.persistence.*;
import lombok.Data;

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
    private int prix;
    @Column(nullable = false)
    private int placeDisponible;

    // Relation entre un trajet et le conducteur
    @ManyToMany
    @JoinTable(
            name = "trajet_conducteurs",
            joinColumns = @JoinColumn(name = "trajet_id"),
            inverseJoinColumns = @JoinColumn(name = "conducteur_id")
    )
    private List<Users> conducteurs;

    // Relations entre un trajet et une reservation
    @OneToMany(mappedBy = "trajet")
    private List<Reservation> reservations;
}
