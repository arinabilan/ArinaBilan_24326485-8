package bank.app.appbank.services;


import bank.app.appbank.packages.entities.ClientDatesEntity;
import bank.app.appbank.packages.repositories.ClientDatesRepository;
import bank.app.appbank.packages.services.ClientDatesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientDatesServiceTest {
    @Mock
    private ClientDatesRepository clientDatesRepository;

    @InjectMocks
    private ClientDatesService clientDatesService;

    private ClientDatesEntity clientDates;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuración de datos de prueba
        clientDates = new ClientDatesEntity();
        clientDates.setId(1L);
        clientDates.setMonthSalary(500000);
        clientDates.setDate(2);  // 2 años en el banco (se convertirá a meses)
        clientDates.setInitialContract(3);  // 3 años de contrato (se convertirá a meses)
        clientDates.setDicom(false);
        clientDates.setType(1);
        clientDates.setMediaSalary(450000);
        clientDates.setMonthlyDebt(100000);
    }

    @Test
    void testGetByClientId_WhenClientExists() {
        when(clientDatesRepository.findByClientId(clientDates.getId())).thenReturn(clientDates);

        ClientDatesEntity foundClientDates = clientDatesService.getByClientId(clientDates.getId());

        assertNotNull(foundClientDates);
        assertEquals(clientDates.getId(), foundClientDates.getId());
        verify(clientDatesRepository, times(1)).findByClientId(clientDates.getId());
    }

    @Test
    void testGetByClientId_WhenClientDoesNotExist() {
        when(clientDatesRepository.findByClientId(clientDates.getId())).thenReturn(null);

        ClientDatesEntity foundClientDates = clientDatesService.getByClientId(clientDates.getId());

        assertNull(foundClientDates);
        verify(clientDatesRepository, times(1)).findByClientId(clientDates.getId());
    }

    @Test
    void testSaveDates_ConvertsYearsToMonthsAndSaves() {
        int expectedMonthsBank = clientDates.getDate(); // 2 años -> 24 meses
        int expectedMonthsContract = clientDates.getInitialContract(); // 3 años -> 36 meses

        when(clientDatesRepository.save(any(ClientDatesEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClientDatesEntity savedClientDates = clientDatesService.saveDates(clientDates);

        assertNotNull(savedClientDates);
        assertEquals(expectedMonthsBank, savedClientDates.getDate());
        assertEquals(expectedMonthsContract, savedClientDates.getInitialContract());
        verify(clientDatesRepository, times(1)).save(clientDates);
    }

    @Test
    void testCalculateMonths() {
        int years = 5;
        int expectedMonths = years;

        int result = clientDatesService.calculateMonths(years);

        assertEquals(expectedMonths, result);
    }

    @Test
    void testSaveDates_CalculatesAndSetsCorrectMonths() {
        clientDates.setDate(2); // 2 años en el banco
        clientDates.setInitialContract(3); // 3 años de contrato laboral

        int expectedMonthsBank = clientDatesService.calculateMonths(clientDates.getDate());
        int expectedMonthsContract = clientDatesService.calculateMonths(clientDates.getInitialContract());

        when(clientDatesRepository.save(any(ClientDatesEntity.class))).thenReturn(clientDates);

        ClientDatesEntity savedClientDates = clientDatesService.saveDates(clientDates);

        assertEquals(expectedMonthsBank, savedClientDates.getDate());
        assertEquals(expectedMonthsContract, savedClientDates.getInitialContract());
        verify(clientDatesRepository, times(1)).save(clientDates);
    }
}
