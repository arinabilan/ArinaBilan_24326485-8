package bank.app.appbank.packages.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="Clients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String rut;
    private String name;
    private String surname;
    private LocalDate birthday;
    private String email;
    private String password;


}
