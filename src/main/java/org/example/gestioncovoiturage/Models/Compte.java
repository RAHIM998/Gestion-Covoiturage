package org.example.gestioncovoiturage.Models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.gestioncovoiturage.util.PasswordUtil;

@Getter
@Setter
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

    // Constructeur avec arguments
    public Compte(String email, String password) {
        this.email = email;
        this.setPassword(password);
    }

    //Constructeur sans argument
    public Compte() {

    }

    // Hashing du password
    public void setPassword(String password) {
        this.password = PasswordUtil.hashPassword(password);
    }

    // Relation entre compte et users
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private Users utilisateur;

    public Users getUser() {
        return utilisateur;
    }

    public void setUser(Users user) {
        this.utilisateur = user;
    }

}
