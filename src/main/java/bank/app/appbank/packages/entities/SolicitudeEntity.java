package bank.app.appbank.packages.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="Solicitudes")
@Data
@AllArgsConstructor
@NoArgsConstructor
//entidad solicitud, que será solicitud para prestamo hecha por cliente
public class SolicitudeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private float maxMonths; //plazo maximo en meses
    private double interestRate; //tasa de interes
    private float maxAmount; //monto maximo en porcentaje (entre 0 y 100)
    private int deadline; //plazo solicitado en meses
    private float amount; //monto solicitado
    private LocalDate date; //fecha de solicitud
    private float calculatedAmount; //calculo entre monto solicitado y el % de monto maximo - sera cantidad maxima
    // de plata que banco puede pasar a cliente, depende de tipo de prestamo(ej: si compras primera vivienda,
    // te pueden financiar solo 80% de esa vivienda)
    private int state; //estado en entero (del 0 al 9) : 0-Sin revisar, 1-En revision inicial, 2-Pendiente de documentación, 3-rechazado

    @ManyToOne(optional = true)
    @JoinColumn(name = "Executive_Id", nullable = true)
    private ExecutiveEntity executive; //para saber que ejecutivo vio la solicitud.

    @ManyToOne
    @JoinColumn(name = "Client_Id", nullable = false)
    private ClientEntity client; //para saber a que cliente pertenece dicha solicitud

    @ManyToOne
    @JoinColumn(name = "loan_type_Id", nullable = false)
    private TypeLoanEntity loanType; //para saber que tipo de prestamo esta dicha solicitud
}
