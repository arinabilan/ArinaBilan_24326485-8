package bank.app.appbank.packages.services;

import bank.app.appbank.packages.entities.ClientEntity;
import bank.app.appbank.packages.repositories.ClientRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public double simulateAmount(double amount, double interesRate, double years ) {
        double r = interesRate/12/100;
        double n = years * 12;
        double pow = Math.pow((1+r), n);
        return Math.round(amount * ((r*pow)/(pow-1)));
    }

    public ArrayList<ClientEntity> getClients(){
        return (ArrayList<ClientEntity>) clientRepository.findAll();
    }

    public ClientEntity getById(Long id) {
        Optional<ClientEntity> client = clientRepository.findById(id);
        return client.orElse(null); // Uso seguro de Optional
    }

    public ClientEntity getClientByRut(String rut){
        return clientRepository.findByRut(rut);
    }

    public ArrayList<ClientEntity> getClientByNameAndSurname(String name, String surname){
        return (ArrayList<ClientEntity>) clientRepository.findByNameAndSurname(name, surname);
    }

    public ArrayList<ClientEntity> getClientByName(String name){
        return (ArrayList<ClientEntity>) clientRepository.findByName(name);
    }

    public ArrayList<ClientEntity> getBySurname(String surname){
        return (ArrayList<ClientEntity>) clientRepository.findBySurname(surname);
    }

    //public Optional<ClientEntity> findByEmail(String email){
        //return clientRepository.findByEmail(email);
    //}

    public ArrayList<ClientEntity> getByBirthdayBetween(LocalDate startDate, LocalDate endDate){
        return (ArrayList<ClientEntity>) clientRepository.findByBirthdayBetween(startDate, endDate);
    }


    public ClientEntity findByEmailAndPassword(String email, String password){
        Optional<ClientEntity> optionalClient = clientRepository.findByEmail(email);

        if (optionalClient.isPresent()) {
            ClientEntity client = optionalClient.get();

            // Compara las contraseñas directamente
            if (client.getPassword().equals(password)) {
                return client; // Contraseña correcta
            } else {
                throw new IllegalArgumentException("Contraseña incorrecta");
            }
        } else {
            throw new IllegalArgumentException("Email no encontrado");
        }
    }

    public ClientEntity saveClient(ClientEntity client){
        return clientRepository.save(client);
    }

    public ClientEntity updateClient(ClientEntity client){
        return clientRepository.save(client);
    }

    public Boolean deleteClient(Long id) throws Exception{
        try{
            clientRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
