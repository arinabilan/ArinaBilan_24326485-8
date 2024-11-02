package bank.app.appbank.services;


import bank.app.appbank.packages.entities.*;
import bank.app.appbank.packages.repositories.*;
import bank.app.appbank.packages.services.ClientService;
import bank.app.appbank.packages.services.SolicitudeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SolicitudeServiceTest {

    @Mock
    private SolicitudeRepository solicitudeRepository;

    @Mock
    private ClientDocumentRepository clientDocumentRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private LoanRequirementRepository loanRequirementRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private ClientDatesRepository clientDatesRepository;

    @InjectMocks
    private SolicitudeService solicitudeService;

    private SolicitudeEntity solicitude;
    private ExecutiveEntity executive;
    private ClientEntity clientEnt;
    private TypeLoanEntity typeLoan;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        solicitude = new SolicitudeEntity();
        solicitude.setId(1L);
        solicitude.setMaxAmount(10000);
        solicitude.setInterestRate(0.05);
        solicitude.setDeadline(12);
        solicitude.setAmount(5000);
        solicitude.setState(0);
        solicitude.setDate(LocalDate.now());
        solicitude.setCalculatedAmount(0);

        executive = new ExecutiveEntity();
        executive.setId(1L);
        executive.setName("Executive");
        executive.setSurname("Executive");
        executive.setPhone("890");
        executive.setEmail("executive@gmail.com");
        executive.setPassword("pass");

        clientEnt = new ClientEntity();
        clientEnt.setId(1L);
        clientEnt.setRut("12345678-9");
        clientEnt.setName("John");
        clientEnt.setSurname("Doe");
        clientEnt.setBirthday(LocalDate.of(1990, 1, 1));
        clientEnt.setEmail("john.doe@example.com");
        clientEnt.setPassword("password");

        typeLoan = new TypeLoanEntity();
        typeLoan.setId(1L);
        typeLoan.setType("Loan");

        //solicitude.setExecutive(executive);
        solicitude.setClient(clientEnt);
        solicitude.setLoanType(typeLoan);
        solicitude.setExecutive(executive);

    }

    @Test
    public void whenGetAllSolicitudes_thenReturnAllSolicitudes() {
        // given
        ArrayList<SolicitudeEntity> solicitudes = new ArrayList<>();
        solicitudes.add(solicitude);
        when(solicitudeRepository.findAll()).thenReturn(solicitudes);

        // when
        ArrayList<SolicitudeEntity> result = solicitudeService.getAllSolicitudes();

        // then
        assertThat(result).hasSize(1).containsExactly(solicitude);
    }

    @Test
    public void whenSaveSolicitude_thenReturnSavedSolicitude() {
        // given
        float amount = 5000;
        float maxAmount = 0.8f; // Ejemplo de porcentaje
        solicitude.setMaxAmount(maxAmount);
        solicitude.setCalculatedAmount(amount * maxAmount);
        when(solicitudeRepository.save(solicitude)).thenReturn(solicitude);

        // when
        SolicitudeEntity result = solicitudeService.saveSolicitude(solicitude);

        // then
        assertThat(result).isEqualTo(solicitude);
        assertThat(result.getCalculatedAmount()).isEqualTo(amount * maxAmount);
        assertThat(1728).isEqualTo(solicitude.getDeadline() * 12);
    }

    @Test
    public void whenUpdateSolicitude_thenReturnUpdatedSolicitude() {
        // given
        when(solicitudeRepository.findById(solicitude.getId())).thenReturn(Optional.of(solicitude));
        when(solicitudeRepository.save(any(SolicitudeEntity.class))).thenReturn(solicitude); // Simula el comportamiento de save

        // Asigna un nuevo ejecutivo y un estado
        ExecutiveEntity newExecutive = new ExecutiveEntity();
        newExecutive.setId(2L);
        newExecutive.setName("New Executive");
        newExecutive.setSurname("New Surname");
        newExecutive.setPhone("123456789");
        newExecutive.setEmail("newexecutive@gmail.com");
        newExecutive.setPassword("newpass");

        int state = 1; // En Revisión Inicial

        // when
        SolicitudeEntity result = solicitudeService.updateSolicitude(solicitude.getId(), newExecutive, state);

        // then
        assertThat(result).isNotNull(); // Asegúrate de que el resultado no sea null
        assertThat(result.getExecutive()).isEqualTo(newExecutive);
        assertThat(result.getState()).isEqualTo(state);
        verify(solicitudeRepository).save(result); // Verifica que se llamó a save con el resultado
    }

    @Test
    public void whenEvaluateSolicitude_thenReturnEvaluatedSolicitude_state2() {
        // given
        when(solicitudeRepository.findById(solicitude.getId())).thenReturn(Optional.of(solicitude));
        when(clientDocumentRepository.findByClientId(clientEnt.getId())).thenReturn(new ArrayList<>());
        when(documentRepository.findDocumentsByMinimunRequirements(true)).thenReturn(new ArrayList<>()); // Simula que no hay documentos mínimos
        when(loanRequirementRepository.findDocumentsByTypeLoanId(clientEnt.getId())).thenReturn(new ArrayList<>()); // Simula que no hay documentos requeridos
        ClientDatesEntity clientDates = new ClientDatesEntity();
        clientDates.setId(1L);
        clientDates.setClient(clientEnt);
        clientDates.setMonthSalary(90000);
        clientDates.setDate(12);
        clientDates.setInitialContract(144);
        clientDates.setDicom(true);
        clientDates.setType(1);
        clientDates.setMediaSalary(600000);
        clientDates.setMonthlyDebt(110000);
        when(clientDatesRepository.findByClientId(clientEnt.getId())).thenReturn(clientDates);
        when(clientService.simulateAmount(anyDouble(), anyDouble(), anyDouble())).thenReturn(200000.0); // Simula el monto de la cuota
        when(clientService.getById(clientEnt.getId())).thenReturn(clientEnt); // Simula la obtención del cliente

        // Configura los datos del cliente
        clientDates.setMonthSalary(600000); // Salario mensual
        clientDates.setMonthlyDebt(100000); // Deuda mensual
        clientDates.setDicom(false); // Sin problemas en el DICOM
        clientDates.setType(1); // Tipo de relación
        clientDates.setMediaSalary(800000); // Salario promedio
        clientDates.setInitialContract(24); // Contrato inicial en meses

        when(solicitudeRepository.save(any(SolicitudeEntity.class))).thenReturn(solicitude);
        // when
        SolicitudeEntity result = solicitudeService.EvaluateSolicitude(solicitude.getId());

        // then
        assertThat(result.getState()).isEqualTo(2); // Asegúrate de que el estado sea pre-aprobado
    }

    @Test
    public void whenEvaluateSolicitudeWithStateGreaterThan3_thenReturnSolicitudeUnchanged() {
        // given
        SolicitudeEntity solicitudeWithStateGreaterThan3 = new SolicitudeEntity();
        solicitudeWithStateGreaterThan3.setId(1L);
        solicitudeWithStateGreaterThan3.setState(4); // Estado mayor a 3

        when(solicitudeRepository.findById(solicitudeWithStateGreaterThan3.getId()))
                .thenReturn(Optional.of(solicitudeWithStateGreaterThan3));

        // when
        SolicitudeEntity result = solicitudeService.EvaluateSolicitude(solicitudeWithStateGreaterThan3.getId());

        // then
        assertThat(result).isEqualTo(solicitudeWithStateGreaterThan3);
        verify(solicitudeRepository, never()).save(any(SolicitudeEntity.class));
    }




}
