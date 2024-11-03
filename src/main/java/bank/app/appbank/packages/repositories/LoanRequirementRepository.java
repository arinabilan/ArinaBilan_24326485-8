package bank.app.appbank.packages.repositories;

import bank.app.appbank.packages.entities.DocumentEntity;
import bank.app.appbank.packages.entities.LoanRequirementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LoanRequirementRepository extends JpaRepository<LoanRequirementEntity, Long> {


    public LoanRequirementEntity findByTypeLoanId(Long id);
    public Integer findMaxMonthsByTypeLoanId(Long id); //Integer para poder manejar valor null
    public BigDecimal findInterestRateByTypeLoanId(Long id);
    public BigDecimal findMaxAmountByTypeLoanId(Long id);

    // Consulta para obtener la lista de documentos por el ID de tipo de préstamo
    @Query("SELECT lr.documents FROM LoanRequirementEntity lr WHERE lr.typeLoan.id = :typeLoanId")
    List<DocumentEntity> findDocumentsByTypeLoanId(Long typeLoanId);

}
