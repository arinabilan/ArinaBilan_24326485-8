package bank.app.appbank.packages.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Loans")
@Data
@AllArgsConstructor
@NoArgsConstructor

//esta clase(entidad) es sobre prestamo que estara asociado a solicitud de usuario
//esta entidad se va a relacionar con el usuario, el usuario que solicito dicho prestamo
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id; //id asosiado al dicho prestamo}

    private int monthlyFee; //cuota mensual
    private int insuranceFee; //cuota seguro
    private int comision; //comision
    private int monthlyCost; //costo mensual
    private int totalCost; //costo total
    private Boolean approved; //estado de credito, si esta aprobado o no por el cliente

    @ManyToOne
    @JoinColumn(name = "Type_id", nullable = false)
    private TypeLoanEntity type; //el dicho prestamo esta asociado a entidad tipo prestamo, a un body entero de tipo prestamo

    @OneToOne
    @JoinColumn(name = "Solicitude_id", nullable = false)
    private SolicitudeEntity solicitude; //el dicho prestamo esta asociado a entidad solicitus,
    // a un body entero de solicitud
}
