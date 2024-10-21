package bank.app.appbank.packages.repositories;

import bank.app.appbank.packages.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

    List<LoanEntity> findBySolicitudeClientRut(String clientRut);
    List<LoanEntity> findBySolicitudeClientRutAndApprovedTrue(String clientRut);
    public Long countByTypeIdAndApproved(Long typeId, Boolean approved);
 }
