package bank.app.appbank.packages.services;

import bank.app.appbank.packages.entities.ClientDatesEntity;
import bank.app.appbank.packages.entities.SavingCapacityEntity;
import bank.app.appbank.packages.entities.SolicitudeEntity;
import bank.app.appbank.packages.repositories.ClientDatesRepository;
import bank.app.appbank.packages.repositories.SavingCapacityRepository;
import bank.app.appbank.packages.repositories.SolicitudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SavingCapacityService {
    @Autowired
    private SavingCapacityRepository savingCapacityRepository;
    @Autowired
    private SolicitudeRepository solicitudeRepository;
    @Autowired
    private ClientDatesRepository clientDatesRepository;

    public void calculateState(Long clientId, Long solicitudeId){
        int score = 5;
        SavingCapacityEntity capacity = savingCapacityRepository.findByClientId(clientId);
        SolicitudeEntity solicitude = solicitudeRepository.findBySolicitudeId(solicitudeId);
        ClientDatesEntity datesClient =clientDatesRepository.findByClientId(clientId);
        int calculatedAmount = solicitude.getCalculatedAmount();
        int balance = capacity.getBalance(); //saldo de cliente en su cuenta de ahorro
        Boolean withdrawals = capacity.getWithdrawal(); //si tiene o no historial de ahorro consistente
        int deposits = capacity.getDeposits(); //suma de depositos en ultimos 12 meses de cliente
        int salary = datesClient.getMonthSalary(); //ingreso mensual de cliente
        double percent = calculatedAmount * 0.1;

        //R71: Saldo Mínimo Requerido
        if (percent < balance){
            score--;
        }

        //R72: Historial de Ahorro Consistente
        if (!withdrawals){
            score--;
        }

        //R73: Depósitos Periódicos
        if (deposits < salary * 0.05) {
            score--;
        }

        //R74: Relación Saldo/Años de Antigüedad
        if (capacity.getYearsSavings() < 2){
            if (capacity.getBalance() < 0.2 * solicitude.getAmount()){
                score--;
            }
        }else{
            if (capacity.getBalance() < 0.1 * solicitude.getAmount()){
                score--;
            }
        }

        if (score < 2){
            capacity.setState(3); //marcar la capacidad de ahorro como “insuficiente” y proceder a rechazar
        }

        if (score > 2 && score < 5){
            capacity.setState(2); //marcar la capacidad de ahorro como "moderada" e indicar que se requiere una revisión adicional
        }

        if (score == 5){
            capacity.setState(1); //reglas, marcar la capacidad de ahorro como “sólida” y continuar con la evaluación del crédito.
        }


        savingCapacityRepository.save(capacity);

    }
}
