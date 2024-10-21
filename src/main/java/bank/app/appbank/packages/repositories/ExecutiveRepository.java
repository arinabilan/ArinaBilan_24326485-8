package bank.app.appbank.packages.repositories;

import bank.app.appbank.packages.entities.ExecutiveEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutiveRepository extends JpaRepository<ExecutiveEntity, Long> {

}
