package bank.app.appbank.packages.repositories;

import bank.app.appbank.packages.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    public ClientEntity findByRut(String rut);
    List<ClientEntity> findByName(String name);
    List<ClientEntity> findBySurname(String surname);
    //List<ClientEntity> findByEmail(String email);
    List<ClientEntity> findByNameAndSurname(String name, String surname);
    List<ClientEntity> findByBirthdayBetween(LocalDate startDate, LocalDate endDate);

    public Optional<ClientEntity> findByEmail(String email);
    public ClientEntity findByEmailAndPassword(String email, String password);
}
