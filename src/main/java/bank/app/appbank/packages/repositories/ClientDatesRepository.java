package bank.app.appbank.packages.repositories;

import bank.app.appbank.packages.entities.ClientDatesEntity;
import bank.app.appbank.packages.entities.SavingCapacityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDatesRepository extends JpaRepository<ClientDatesEntity, Long> {

    public ClientDatesEntity findByClientId(Long clientId);
}
