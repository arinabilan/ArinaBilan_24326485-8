package bank.app.appbank.packages.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="Documents")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String title;
    private Boolean minimunRequirements = false;

    @ManyToMany(mappedBy = "documents")
   // @JsonBackReference
    @JsonIgnore
    private List<LoanRequirementEntity> loanRequirements; // Requisitos de préstamo asociados

}
