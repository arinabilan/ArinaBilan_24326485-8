package bank.app.appbank.packages.repositories;

import bank.app.appbank.packages.entities.SavingCapacityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingCapacityRepository extends JpaRepository<SavingCapacityEntity, Long> {

    public SavingCapacityEntity findByClientId(Long clientId); //obtener la cuenta completa de ahorro del cliente por el rut
    public Integer findBalanceByClientId(Long clientId);
    public Integer findWithdrawalsByClientId(Long clientId);
    public Integer findDepositsByClientId(Long clientId);
    public Integer findYearsSavingsByClientId(Long clientId);
    public Integer findRecentWithdrawalsByClientId(Long clientId);

}
