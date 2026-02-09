package mg.framework.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    
    @Id
    @Column(name = "id", length = 4)
    private String id;
    
    @Column(name = "nom", length = 100, nullable = false)
    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;
    
    @Column(name = "prenom", length = 100, nullable = false)
    @NotBlank(message = "Le prénom ne peut pas être vide")
    private String prenom;
    
    @Column(name = "telephone", length = 20, nullable = false, unique = true)
    @NotBlank(message = "Le téléphone ne peut pas être vide")
    private String telephone;
    
    @Column(name = "email", length = 150, unique = true)
    @Email(message = "L'email doit être valide")
    private String email;
}
