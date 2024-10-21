package bank.app.appbank.packages.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Savings_capacity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingCapacityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;
    private int balance; //saldo enn cuenta ahorro que sera para R71:saldo minimo requerido
    private int withdrawals; //suma de retiros en ultimos 12 meses R72:Historial de ahorro consistente
    private Boolean withdrawal; //historial de ahorro consistente: ¿Tiene Usted ahorro consistente en ultimos 12 meses? - (True o False) Adjunta documento que lo acredita
    private int deposits; //suma de depositos mensuales de ultimos 12 meses R73:Depositos periodicos
    private int yearsSavings; //años de antiguidad de la cuenta de ahorro R74:Relacion saldo/Años antiguidad
    private int recentWithdrawals; //suma de retiros en ultimos 6 meses R75:Retiros recientes
    private int state; //resultado de evaluacion (1,2,3) 1-Aprobacion:Solido, 2-Revision Adicional:Moderado, 3-Rechazo:Insuficiente

    @OneToOne
    @JoinColumn(nullable = false)
    private ClientEntity client; //esta asociado a id de usuario, cada usuario que va a registrarse tendran sus datos de
    //capacidad de ahorro

}
