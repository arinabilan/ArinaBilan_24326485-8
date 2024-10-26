package bank.app.appbank.packages.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

//entidad de datos de clliente
@Entity
@Table(name = "client_dates")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDatesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private int monthSalary; //salario mensual
    private int date; //cantidad de meses con cuenta en banco, para saber cuanto tiempo lleva de cliente en el banco
    private int initialContract; // meses con contrato laboral
    private Boolean dicom; //buleano para saber si esta en DICOM o no
    private int type; //tipo de trabajador (Independiente o no), rango de valores (1,2) : 1 - independiente, 2: - no independiente
    private int mediaSalary; //promedio de ingresos en ultimos 2 años
    private int monthlyDebt; //deuda mensual

    @OneToOne
    @JoinColumn(name = "client_Id", nullable = false)
    private ClientEntity client; //los datos estan asociados a datos del cliente

}
