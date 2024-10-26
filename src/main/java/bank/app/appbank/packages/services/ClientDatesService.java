package bank.app.appbank.packages.services;

import bank.app.appbank.packages.entities.ClientDatesEntity;
import bank.app.appbank.packages.repositories.ClientDatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientDatesService {
    @Autowired
    private ClientDatesRepository clientDatesRepository;

    public ClientDatesEntity saveDates(ClientDatesEntity clientDates){
        return clientDatesRepository.save(clientDates);
    }
}
