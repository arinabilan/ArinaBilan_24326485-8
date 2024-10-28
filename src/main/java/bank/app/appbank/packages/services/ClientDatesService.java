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
        return clientDatesRepository.save(clientDates);
    }
}
