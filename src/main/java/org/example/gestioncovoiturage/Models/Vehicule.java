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

    // Relation du véhicule à l'utilisateur
    @OneToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Users utilisateur; // singular
}
