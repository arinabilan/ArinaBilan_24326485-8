package bank.app.appbank.repositories;

import bank.app.appbank.packages.entities.ClientEntity;
import bank.app.appbank.packages.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ClientRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void whenFindByRut_thenReturnClient() {
        //given
        ClientEntity client = new ClientEntity(
                null,
                "24326475-8",    // Cambiar a comillas dobles para String
                "Dekabrina",     // Cambiar a comillas dobles para String
                "Yacenko",       // Cambiar a comillas dobles para String
                LocalDate.of(1991, 12, 4), // Crear un LocalDate para la fecha de nacimiento
                "dekabrina.yacenko@gmail.com", // Cambiar a comillas dobles para String
                "123"
        );
        entityManager.persistAndFlush(client);

        //when
        ClientEntity found = clientRepository.findByRut(client.getRut());

        //then
        assertThat(found.getRut()).isEqualTo(client.getRut());
    }

    @Test
    public void whenFindByName_thenReturnClients() {
        // given
        ClientEntity client1 = new ClientEntity(null, "12345678-9", "John", "Doe", LocalDate.of(1990, 1, 1), "john.doe@example.com", "password");
        ClientEntity client2 = new ClientEntity(null, "98765432-1", "John", "Smith", LocalDate.of(1992, 2, 2), "john.smith@example.com", "password");
        entityManager.persistAndFlush(client1);
        entityManager.persistAndFlush(client2);

        // when
        List<ClientEntity> foundClients = clientRepository.findByName("John");

        // then
        assertThat(foundClients).hasSize(2).extracting(ClientEntity::getName).containsExactlyInAnyOrder("John", "John");
    }

    @Test
    public void whenFindBySurname_thenReturnClients() {
        // given
        ClientEntity client1 = new ClientEntity(null, "11111111-1", "Jane", "Doe", LocalDate.of(1995, 5, 5), "jane.doe@example.com", "password");
        ClientEntity client2 = new ClientEntity(null, "22222222-2", "Mike", "Doe", LocalDate.of(1988, 8, 8), "mike.doe@example.com", "password");
        entityManager.persistAndFlush(client1);
        entityManager.persistAndFlush(client2);

        // when
        List<ClientEntity> foundClients = clientRepository.findBySurname("Doe");

        // then
        assertThat(foundClients).hasSize(2).extracting(ClientEntity::getSurname).containsExactlyInAnyOrder("Doe", "Doe");
    }

    @Test
    public void whenFindByNameAndSurname_thenReturnClient() {
        // given
        ClientEntity client = new ClientEntity(null, "33333333-3", "Emily", "Clark", LocalDate.of(1993, 3, 3), "emily.clark@example.com", "password");
        entityManager.persistAndFlush(client);

        // when
        List<ClientEntity> foundClients = clientRepository.findByNameAndSurname("Emily", "Clark");

        // then
        assertThat(foundClients).hasSize(1).extracting(ClientEntity::getName).contains("Emily");
    }

    @Test
    public void whenFindByEmail_thenReturnClient() {
        // given
        ClientEntity client = new ClientEntity(null, "44444444-4", "Alice", "Wonders", LocalDate.of(1989, 9, 9), "alice.wonders@example.com", "password");
        entityManager.persistAndFlush(client);

        // when
        Optional<ClientEntity> foundClient = clientRepository.findByEmail("alice.wonders@example.com");

        // then
        assertThat(foundClient).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getEmail()).isEqualTo(client.getEmail());
        });
    }

    @Test
    public void whenFindByEmailAndPassword_thenReturnClient() {
        // given
        ClientEntity client = new ClientEntity(null, "55555555-5", "Bob", "Builder", LocalDate.of(1994, 4, 4), "bob.builder@example.com", "builderpassword");
        entityManager.persistAndFlush(client);

        // when
        ClientEntity foundClient = clientRepository.findByEmailAndPassword("bob.builder@example.com", "builderpassword");

        // then
        assertThat(foundClient).isNotNull();
        assertThat(foundClient.getEmail()).isEqualTo(client.getEmail());
    }
}
