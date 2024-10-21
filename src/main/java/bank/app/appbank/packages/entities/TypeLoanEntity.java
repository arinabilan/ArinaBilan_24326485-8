package bank.app.appbank.packages.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="typeloan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeLoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;
    private String type; //el nombre de tipo de prestamo
}
