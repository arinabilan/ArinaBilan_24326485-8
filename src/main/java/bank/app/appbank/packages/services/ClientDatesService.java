package bank.app.appbank.packages.services;

import bank.app.appbank.packages.entities.ClientDatesEntity;
import bank.app.appbank.packages.entities.ClientEntity;
import bank.app.appbank.packages.repositories.ClientDatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientDatesService {
    @Autowired
    private ClientDatesRepository clientDatesRepository;

    public ClientDatesEntity getByClientId(Long id) {
        return clientDatesRepository.findByClientId(id);
    }

    public ClientDatesEntity saveDates(ClientDatesEntity clientDates){
        int yearsBank = clientDates.getDate();
        int yearsContract = clientDates.getInitialContract();
        int monthsBank = calculateMonths(yearsBank);
        int monthsContract = calculateMonths(yearsContract);
        clientDates.setDate(monthsBank);
        clientDates.setInitialContract(monthsContract);
        return clientDatesRepository.save(clientDates);
    }

    public int calculateMonths(int years){
        return years * 12;
    }
}
