package org.example.gestioncovoiturage.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Users passager;

    @ManyToOne
    @JoinColumn(name = "trajet_id", nullable = false)

    private Trajet trajet;
    @Column
    private LocalDateTime dateReservation;
    @Column
    private int nbPlaceReservation;


    public Users getPassager() {
        return passager;
    }
}
