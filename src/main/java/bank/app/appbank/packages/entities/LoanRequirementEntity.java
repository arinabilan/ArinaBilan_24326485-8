package bank.app.appbank.packages.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="Requirements")
@Data
@AllArgsConstructor
@NoArgsConstructor
//entidad de requirimiento para prestamos
public class LoanRequirementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private int maxMonths; //plazo maximo en meses
    private BigDecimal interestRate; //tasa de interes
    private BigDecimal maxAmount; //monto maximo en decimales (ej: 0.7 son 70%)

    @OneToOne
    @JoinColumn(nullable = false)
    private TypeLoanEntity typeLoan; //para saber a que tipo de prestamo esta asociado este requisito

    //@ManyToMany
    //@JoinColumn(name = "Document_Id", nullable = false)
    //private DocumentEntity document; //una lista asociada a los documentos que se requieren en este requisito
    @ManyToMany
    @JoinTable(
            name = "requirement_documents",
            joinColumns = @JoinColumn(name = "requirement_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    //@JsonManagedReference
    private List<DocumentEntity> documents; // Lista asociada a los documentos requeridos en este requisito


}
