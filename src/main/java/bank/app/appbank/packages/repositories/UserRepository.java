package bank.app.appbank.packages.repositories;

import bank.app.appbank.packages.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public UserEntity findByRut(String rut);
    List<UserEntity> findByName(String name);
    List<UserEntity> findByEmail(String email);
}
