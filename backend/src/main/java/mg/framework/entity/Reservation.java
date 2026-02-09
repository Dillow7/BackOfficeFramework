package mg.framework.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "idClient", length = 4, nullable = false)
    @NotNull(message = "Le client ne peut pas être null")
    private String idClient;
    
    @Column(name = "nbPassager", nullable = false)
    @Min(value = 1, message = "Le nombre de passagers doit être au moins 1")
    @NotNull(message = "Le nombre de passagers ne peut pas être null")
    private Integer nbPassager;
    
    @Column(name = "dateHeureArrive", nullable = false)
    @NotNull(message = "La date et heure d'arrivée ne peuvent pas être nulles")
    private LocalDateTime dateHeureArrive;
    
    @Column(name = "idHotel", nullable = false)
    @NotNull(message = "L'hôtel ne peut pas être null")
    private Integer idHotel;
}
