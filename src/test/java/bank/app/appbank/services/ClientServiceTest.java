package bank.app.appbank.services;

import bank.app.appbank.packages.entities.ClientEntity;
import bank.app.appbank.packages.repositories.ClientRepository;
import bank.app.appbank.packages.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService; // Asegúrate de que ClientService es el nombre correcto de tu clase de servicio

    private ClientEntity clientEnt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientEnt = new ClientEntity();
        clientEnt.setRut("12345678-9");
        clientEnt.setName("John");
        clientEnt.setSurname("Doe");
        clientEnt.setBirthday(LocalDate.of(1990, 1, 1));
        clientEnt.setEmail("john.doe@example.com");
        clientEnt.setPassword("password");
    }

    public ClientServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetByRut_thenCorrect() {
        // given
        ClientEntity clientEnt = new ClientEntity();
        clientEnt.setRut("12345678-9");
        clientEnt.setName("Rut");
        clientEnt.setEmail("rut@gmail.com");
        clientEnt.setPassword("password");
        clientEnt.setBirthday(LocalDate.of(1991, 12, 4));
        clientEnt.setSurname("Surname");

        when(clientRepository.findByRut("12345678-9")).thenReturn(clientEnt);

        // when
        ClientEntity foundClient = clientService.getClientByRut("12345678-9");

        // then
        assertThat(foundClient).isNotNull();
        assertThat(foundClient.getRut()).isEqualTo(clientEnt.getRut());
        assertThat(foundClient.getName()).isEqualTo(clientEnt.getName());
        assertThat(foundClient.getEmail()).isEqualTo(clientEnt.getEmail());
        assertThat(foundClient.getSurname()).isEqualTo(clientEnt.getSurname());
        assertThat(foundClient.getBirthday()).isEqualTo(clientEnt.getBirthday());
    }

    @Test
    public void whenSimulateAmount_thenCorrectValueReturned() {
        double amount = 1000.0;
        double interestRate = 5.0; // 5%
        double years = 10.0; // 10 years

        double expected = 11.00;

        double result = clientService.simulateAmount(amount, interestRate, years);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void whenSimulateAmountWithZeroInterestRate_thenReturnOriginalAmount() {
        double amount = 100000000.0;
        double interestRate = 4.5; // 0%
        double years = 20.0; // 10 years

        double expected = 632649.0; // Mismo monto esperado

        double result = clientService.simulateAmount(amount, interestRate, years);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void whenSimulateAmount1_thenCorrectValueReturned() {
        double amount = 70000000.0;
        double interestRate = 5.0; // 5%
        double years = 5.0; // 10 years

        double expected = 1320986;

        double result = clientService.simulateAmount(amount, interestRate, years);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void whenGetClients_thenAllClientsReturned() {
        // given
        ArrayList<ClientEntity> clients = new ArrayList<>();
        clients.add(clientEnt); // clientEnt debería estar inicializado previamente con un ID
        clients.add(new ClientEntity(2L, "98765432-1", "Jane", "Doe", LocalDate.of(1992, 2, 2), "jane.doe@example.com", "password"));

        when(clientRepository.findAll()).thenReturn(clients);

        // when
        ArrayList<ClientEntity> result = clientService.getClients();

        // then
        assertThat(result).hasSize(2).containsExactlyInAnyOrderElementsOf(clients);
    }

    @Test
    public void whenGetById_thenClientReturned() {
        // given
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clientEnt));

        // when
        ClientEntity result = clientService.getById(clientId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo(clientEnt.getRut());
    }

    @Test
    public void whenGetClientByRut_thenClientReturned() {
        // given
        String rut = "12345678-9";
        when(clientRepository.findByRut(rut)).thenReturn(clientEnt);

        // when
        ClientEntity result = clientService.getClientByRut(rut);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo(rut);
    }

    @Test
    public void whenGetClientByNameAndSurname_thenClientsReturned() {
        // given
        ArrayList<ClientEntity> clients = new ArrayList<>();
        clients.add(clientEnt);
        clients.add(new ClientEntity(2L, "98765432-1", "Jane", "Doe", LocalDate.of(1992, 2, 2), "jane.doe@example.com", "password"));
        when(clientRepository.findByNameAndSurname("John", "Doe")).thenReturn(clients);

        // when
        ArrayList<ClientEntity> result = clientService.getClientByNameAndSurname("John", "Doe");

        // then
        assertThat(result).hasSize(2).containsExactlyInAnyOrderElementsOf(clients);
    }

    @Test
    public void whenGetClientByName_thenClientsReturned() {
        // given
        ArrayList<ClientEntity> clients = new ArrayList<>();
        clients.add(clientEnt);
        when(clientRepository.findByName("John")).thenReturn(clients);

        // when
        ArrayList<ClientEntity> result = clientService.getClientByName("John");

        // then
        assertThat(result).hasSize(1).containsExactly(clientEnt);
    }

    @Test
    public void whenGetBySurname_thenClientsReturned() {
        // given
        ArrayList<ClientEntity> clients = new ArrayList<>();
        clients.add(clientEnt);
        when(clientRepository.findBySurname("Doe")).thenReturn(clients);

        // when
        ArrayList<ClientEntity> result = clientService.getBySurname("Doe");

        // then
        assertThat(result).hasSize(1).containsExactly(clientEnt);
    }

    @Test
    public void whenGetByBirthdayBetween_thenClientsReturned() {
        // given
        LocalDate startDate = LocalDate.of(1980, 1, 1);
        LocalDate endDate = LocalDate.of(2000, 1, 1);
        ArrayList<ClientEntity> clients = new ArrayList<>();
        clients.add(clientEnt);
        when(clientRepository.findByBirthdayBetween(startDate, endDate)).thenReturn(clients);

        // when
        ArrayList<ClientEntity> result = clientService.getByBirthdayBetween(startDate, endDate);

        // then
        assertThat(result).hasSize(1).containsExactly(clientEnt);
    }

    @Test
    public void whenFindByEmailAndPassword_thenClientReturned() {
        // given
        String email = "john.doe@example.com";
        String password = "password";
        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(clientEnt));

        // when
        ClientEntity result = clientService.findByEmailAndPassword(email, password);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @Test
    public void whenSaveClient_thenClientReturned() {
        // given
        when(clientRepository.save(clientEnt)).thenReturn(clientEnt);

        // when
        ClientEntity result = clientService.saveClient(clientEnt);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo(clientEnt.getRut());
    }

    @Test
    public void whenDeleteClient_thenTrueReturned() throws Exception {
        // given
        Long clientId = 1L;
        doNothing().when(clientRepository).deleteById(clientId);

        // when
        Boolean result = clientService.deleteClient(clientId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void whenFindByEmailAndPasswordWithCorrectCredentials_thenClientReturned() {
        // given
        String email = "john.doe@example.com";
        String password = "password";
        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(clientEnt));

        // when
        ClientEntity result = clientService.findByEmailAndPassword(email, password);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @Test
    public void whenFindByEmailAndPasswordWithNonExistentEmail_thenExceptionThrown() {
        // given
        String email = "non.existent@example.com";
        String password = "password";
        when(clientRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            clientService.findByEmailAndPassword(email, password);
        });

        assertThat(thrown.getMessage()).isEqualTo("Email no encontrado");
    }

    @Test
    public void whenFindByEmailAndPasswordWithIncorrectPassword_thenExceptionThrown() {
        // given
        String email = "john.doe@example.com";
        String incorrectPassword = "wrongPassword";
        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(clientEnt));
        // Establecer la contraseña correcta en el objeto clientEnt
        clientEnt.setPassword("password");

        // when & then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            clientService.findByEmailAndPassword(email, incorrectPassword);
        });

        assertThat(thrown.getMessage()).isEqualTo("Contraseña incorrecta");
    }

    @Test
    public void whenUpdateClient_thenClientSaved() {
        // given
        ClientEntity clientToUpdate = new ClientEntity();
        clientToUpdate.setRut("12345678-9");
        clientToUpdate.setName("John");
        clientToUpdate.setSurname("Doe");
        clientToUpdate.setBirthday(LocalDate.of(1990, 1, 1));
        clientToUpdate.setEmail("john.doe@example.com");
        clientToUpdate.setPassword("password");

        when(clientRepository.save(clientToUpdate)).thenReturn(clientToUpdate);

        // when
        ClientEntity result = clientService.updateClient(clientToUpdate);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo(clientToUpdate.getRut());
        assertThat(result.getName()).isEqualTo(clientToUpdate.getName());
        assertThat(result.getSurname()).isEqualTo(clientToUpdate.getSurname());
        assertThat(result.getBirthday()).isEqualTo(clientToUpdate.getBirthday());
        assertThat(result.getEmail()).isEqualTo(clientToUpdate.getEmail());
        assertThat(result.getPassword()).isEqualTo(clientToUpdate.getPassword());
    }

    @Test
    public void whenDeleteClient_thenReturnTrue() throws Exception {
        // given
        Long clientIdToDelete = 1L;

        // when
        Boolean result = clientService.deleteClient(clientIdToDelete);

        // then
        assertThat(result).isTrue();
        verify(clientRepository).deleteById(clientIdToDelete); // Verifica que se llamó a deleteById
    }

    @Test
    public void whenDeleteClient_throwsException() {
        // given
        Long clientIdToDelete = 1L;
        doThrow(new RuntimeException("Error al eliminar")).when(clientRepository).deleteById(clientIdToDelete);

        // when
        Exception exception = assertThrows(Exception.class, () -> {
            clientService.deleteClient(clientIdToDelete);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("Error al eliminar");
    }
}
