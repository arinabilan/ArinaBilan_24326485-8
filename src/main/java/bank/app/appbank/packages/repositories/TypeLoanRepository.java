package bank.app.appbank.packages.repositories;

import bank.app.appbank.packages.entities.TypeLoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeLoanRepository extends JpaRepository<TypeLoanEntity, Long> {
    public TypeLoanEntity getLoanByType(String type);
}
