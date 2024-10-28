package bank.app.appbank.packages.services;

import bank.app.appbank.packages.entities.ClientEntity;
import bank.app.appbank.packages.entities.ExecutiveEntity;
import bank.app.appbank.packages.repositories.ExecutiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExecutiveService {
    @Autowired
    private ExecutiveRepository executiveRepository;

    public ExecutiveEntity getExecutive(Long id){
        Optional<ExecutiveEntity> client = executiveRepository.findById(id);
        return client.orElse(null); // Uso seguro de Optional
    }

    public ExecutiveEntity getByEmailAndPassword(String email, String password){
        Optional<ExecutiveEntity> optionalExecutive = executiveRepository.findByEmail(email);

        if(optionalExecutive.isPresent()){
            ExecutiveEntity executive = optionalExecutive.get();

            if(executive.getPassword().equals(password)){
                return executive;
            } else {
                throw new IllegalArgumentException("Constraseña incorrecta");
            }
        } else {
            throw new IllegalArgumentException("Email no encontrado");
        }
    }

    public ExecutiveEntity addExecutive(ExecutiveEntity executive){
        return executiveRepository.save(executive);
    }

    public ExecutiveEntity updateExecutive(ExecutiveEntity executive){
        return executiveRepository.save(executive);
    }

    public Boolean deleteExecutive(Long id) throws Exception{
        try{
            executiveRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
