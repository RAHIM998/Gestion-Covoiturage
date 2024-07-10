package org.example.gestioncovoiturage.Models;

import jakarta.persistence.*;
import lombok.Data;
import org.example.gestioncovoiturage.util.PasswordUtil;

@Data
@Entity
@Table(name = "compte")
public class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    // Hashage du password
    public void setPassword(String password) {
        this.password = PasswordUtil.hashPassword(password);
    }

    // Relation entre compte et users
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private Users utilisateur;
}
