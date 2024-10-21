package bank.app.appbank.packages.repositories;

import bank.app.appbank.packages.entities.SavingCapacityEntity;
import bank.app.appbank.packages.entities.SolicitudeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudeRepository extends JpaRepository<SolicitudeEntity, Long> {

    @Query(value = "SELECT * FROM solicitudes WHERE solicitudes.id = :id", nativeQuery = true)
    SolicitudeEntity findBySolicitudeId(@Param("id") Long id);

}
