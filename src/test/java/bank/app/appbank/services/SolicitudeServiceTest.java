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

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private SolicitudeService solicitudeService;

    private SolicitudeEntity solicitude;
    private ExecutiveEntity executive;
    private ClientEntity clientEnt;
    private TypeLoanEntity typeLoan;
    private ArrayList<ClientDocumentEntity> documentsClient;
    private ArrayList<DocumentEntity> documentsMinimun;
    private ArrayList<DocumentEntity> documentByLoan;

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

        documentsClient = new ArrayList<>();
        documentsMinimun = new ArrayList<>();
        documentByLoan = new ArrayList<>();
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
    void testEvaluateSolicitudeMissingDocuments() {
        SolicitudeEntity solicitude = createBasicSolicitude();
        ArrayList<DocumentEntity> requiredDocs = new ArrayList<>();
        DocumentEntity doc = new DocumentEntity();
        doc.setId(1L);
        doc.setMinimunRequirements(true);
        requiredDocs.add(doc);

        when(solicitudeRepository.findById(1L)).thenReturn(Optional.of(solicitude));
        when(clientDocumentRepository.findByClientId(1L)).thenReturn(new ArrayList<>());
        when(documentRepository.findDocumentsByMinimunRequirements(true)).thenReturn(requiredDocs);
        when(loanRequirementRepository.findDocumentsByTypeLoanId(clientEnt.getId())).thenReturn(new ArrayList<>());

        when(solicitudeRepository.save(any(SolicitudeEntity.class))).thenReturn(solicitude);
        SolicitudeEntity result = solicitudeService.EvaluateSolicitude(1L);

        assertThat(result.getState()).isEqualTo(2);
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
    }
//
//    @Test
//    public void testEvaluateSolicitude_RelationCuoteSalaryGreaterThan35() {
//        Long solicitudeId = 1L;
//        SolicitudeEntity solicitudenew = new SolicitudeEntity();
//        solicitudenew.setId(solicitudeId);
//        solicitudenew.setState(1);
//        solicitudenew.setAmount(100000);
//        solicitudenew.setInterestRate(0.05f);
//        solicitudenew.setDeadline(12);
//        ClientEntity client = new ClientEntity();
//        client.setId(1L);
//        solicitudenew.setClient(client);
//
//        ClientDatesEntity clientDates = new ClientDatesEntity();
//        clientDates.setId(1L);
//        clientDates.setClient(clientEnt);
//        clientDates.setMonthSalary(90000);
//        clientDates.setDate(12);
//        clientDates.setInitialContract(144);
//        clientDates.setDicom(true);
//        clientDates.setType(1);
//        clientDates.setMediaSalary(600000);
//        clientDates.setMonthlyDebt(110000);
//
//        when(solicitudeRepository.findById(solicitudenew.getId()))
//                .thenReturn(Optional.of(solicitudenew));
//        when(solicitudeRepository.findById(solicitudeId)).thenReturn(Optional.of(solicitudenew));
//        when(clientDocumentRepository.findByClientId(clientEnt.getId())).thenReturn(new ArrayList<>());
//        when(documentRepository.findDocumentsByMinimunRequirements(true)).thenReturn(new ArrayList<>());
//        when(loanRequirementRepository.findDocumentsByTypeLoanId(clientEnt.getId())).thenReturn(new ArrayList<>());
//        when(clientDatesRepository.findByClientId(clientEnt.getId())).thenReturn(clientDates);
//        when(clientService.simulateAmount(anyFloat(), anyFloat(), anyDouble())).thenReturn(400.0);
//
//        when(solicitudeRepository.save(any(SolicitudeEntity.class))).thenReturn(solicitudenew);
//
//        SolicitudeEntity result = solicitudeService.EvaluateSolicitude(solicitudenew.getId());
//
//        assertThat(result.getState()).isEqualTo(7);
//    }
//
//    @Test
//    public void testEvaluateSolicitude_DicomTrue() {
//        Long solicitudeId = 1L;
//        SolicitudeEntity solicitude = new SolicitudeEntity();
//        solicitude.setId(solicitudeId);
//        solicitude.setState(1);
//        ClientEntity client = new ClientEntity();
//        client.setId(1L);
//        solicitude.setClient(client);
//
//        ClientDatesEntity clientDates = new ClientDatesEntity();
//        clientDates.setDicom(true);
//
////        setupBasicMocks(solicitude, clientDates);
//        when(solicitudeRepository.findById(solicitudeId)).thenReturn(Optional.of(solicitude));
//        when(clientDocumentRepository.findByClientId(client.getId())).thenReturn(new ArrayList<>());
//        when(documentRepository.findDocumentsByMinimunRequirements(true)).thenReturn(new ArrayList<>());
//        when(loanRequirementRepository.findDocumentsByTypeLoanId(client.getId())).thenReturn(new ArrayList<>());
//        when(clientDatesRepository.findByClientId(client.getId())).thenReturn(clientDates);
//
//        setupBasicMocks(solicitude, clientDates);
//        when(solicitudeRepository.save(any(SolicitudeEntity.class))).thenReturn(solicitude);
//        SolicitudeEntity result = solicitudeService.EvaluateSolicitude(solicitudeId);
//
//        assertThat(result.getState()).isEqualTo(7);
//    }

    @Test
    void testEvaluateSolicitudeQuotaIncomeRatioExceeded() {
        SolicitudeEntity solicitude = createBasicSolicitude();
        ClientDatesEntity clientDates = new ClientDatesEntity();
        clientDates.setMonthSalary(1000);
        clientDates.setDicom(false);


        setupBasicMocks(solicitude, clientDates);
        when(clientService.simulateAmount(anyFloat(), anyFloat(), anyDouble())).thenReturn(400.0);

        when(solicitudeRepository.save(any(SolicitudeEntity.class))).thenReturn(solicitude);
        SolicitudeEntity result = solicitudeService.EvaluateSolicitude(1L);

        assertThat(result.getState()).isEqualTo(7);
    }

    @Test
    void testEvaluateSolicitudeContractStateVerification() {
        SolicitudeEntity solicitude = createBasicSolicitude();
        ClientDatesEntity clientDates = new ClientDatesEntity();
        clientDates.setType(1);
        clientDates.setDicom(false);
        clientDates.setMediaSalary(700000);
        clientDates.setMonthSalary(800000);
        clientDates.setMonthlyDebt(400000);

        setupBasicMocks(solicitude, clientDates);

        when(solicitudeRepository.save(any(SolicitudeEntity.class))).thenReturn(solicitude);
        SolicitudeEntity result = solicitudeService.EvaluateSolicitude(1L);

        assertThat(result.getState()).isEqualTo(7);
    }

    @Test
    public void testEvaluateSolicitude_WhenDebtToIncomeRatioExceeds50Percent_ShouldReject() {
        // Arrange
        SolicitudeEntity solicitude = createBasicSolicitude();
        ClientDatesEntity clientDates = createValidClientDates();
        clientDates.setMonthlyDebt(600000); // 60% del salario mensual

        setupBasicMocks(solicitude, clientDates);
        when(solicitudeRepository.save(any())).thenReturn(solicitude);

        // Act
        SolicitudeEntity result = solicitudeService.EvaluateSolicitude(1L);

        // Assert
        assertThat(result.getState()).isEqualTo(7); // Estado rechazado
    }

    @Test
    public void testEvaluateSolicitude_WhenPaymentToIncomeRatioExceeds35Percent_ShouldReject() {
        // Arrange
        SolicitudeEntity solicitude = createBasicSolicitude();
        solicitude.setAmount(1000000);
        solicitude.setInterestRate(0.10f);
        solicitude.setDeadline(12);

        ClientDatesEntity clientDates = createValidClientDates();
        clientDates.setMonthSalary(100000); // Salario bajo para que la cuota exceda 35%

        setupBasicMocks(solicitude, clientDates);
        when(clientService.simulateAmount(anyDouble(), anyDouble(), anyDouble())).thenReturn(50000.0);
        when(solicitudeRepository.save(any())).thenReturn(solicitude);

        // Act
        SolicitudeEntity result = solicitudeService.EvaluateSolicitude(1L);

        // Assert
        assertThat(result.getState()).isEqualTo(7); // Estado rechazado
    }

    @Test
    public void testEvaluateSolicitude_WhenAgeNotInRange_ShouldReject() {
        // Arrange
        SolicitudeEntity solicitude = createBasicSolicitude();
        ClientEntity client = new ClientEntity();
        client.setId(1L);
        client.setBirthday(LocalDate.now().minusYears(75));
        solicitude.setClient(client);

        ClientDatesEntity clientDates = createValidClientDates();

        setupBasicMocks(solicitude, clientDates);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientService.getById(1L)).thenReturn(client);
        when(solicitudeRepository.save(any())).thenReturn(solicitude);

        when(solicitudeRepository.save(any(SolicitudeEntity.class))).thenReturn(solicitude);
        SolicitudeEntity result = solicitudeService.EvaluateSolicitude(1L);

        // Assert
        assertThat(result.getState()).isEqualTo(7); // Estado rechazado
    }

    @Test
    void testEvaluateSolicitudeSuccessfulPreApproval() {
        SolicitudeEntity solicitude = createBasicSolicitude();
        ClientDatesEntity clientDates = createValidClientDates();
        ClientEntity client = new ClientEntity();
        client.setBirthday(LocalDate.now().minusYears(30));

        setupBasicMocks(solicitude, clientDates);
        when(clientService.getById(1L)).thenReturn(client);
        when(clientService.simulateAmount(anyFloat(), anyFloat(), anyDouble())).thenReturn(1000.0);

        when(solicitudeRepository.save(any(SolicitudeEntity.class))).thenReturn(solicitude);
        SolicitudeEntity result = solicitudeService.EvaluateSolicitude(1L);

        assertThat(result.getState()).isEqualTo(4);
    }

    private SolicitudeEntity createBasicSolicitude() {
        SolicitudeEntity solicitude = new SolicitudeEntity();
        solicitude.setState(1);
        solicitude.setDeadline(12);
        ClientEntity client = new ClientEntity();
        client.setId(1L);
        solicitude.setClient(client);
        TypeLoanEntity typeLoan = new TypeLoanEntity();
        typeLoan.setId(1L);
        solicitude.setLoanType(typeLoan);
        return solicitude;
    }

    private ClientDatesEntity createValidClientDates() {
        ClientDatesEntity clientDates = new ClientDatesEntity();
        clientDates.setType(1);
        clientDates.setMediaSalary(800000);
        clientDates.setMonthSalary(1000000);
        clientDates.setMonthlyDebt(200000);
        clientDates.setDicom(false);
        return clientDates;
    }

    private void setupBasicMocks(SolicitudeEntity solicitude, ClientDatesEntity clientDates) {
        // Llenamos los requerimientos mínimos
        ArrayList<DocumentEntity> minimunDocuments = new ArrayList<>();
        DocumentEntity doc = new DocumentEntity();
        doc.setId(1L);
        doc.setMinimunRequirements(true);
        minimunDocuments.add(doc);
        doc = new DocumentEntity();
        doc.setId(2L);
        doc.setMinimunRequirements(true);
        minimunDocuments.add(doc);

        ArrayList<DocumentEntity> loanDocuments = new ArrayList<>();
        doc = new DocumentEntity();
        doc.setId(3L);
        doc.setMinimunRequirements(false);
        loanDocuments.add(doc);
        doc = new DocumentEntity();
        doc.setId(4L);
        doc.setMinimunRequirements(false);
        loanDocuments.add(doc);

        ArrayList<ClientDocumentEntity> clientDocuments = new ArrayList<>();
        ClientDocumentEntity clientDocument = new ClientDocumentEntity();
        clientDocument.setId(1L);
        doc = new DocumentEntity();
        doc.setId(1L);
        doc.setMinimunRequirements(true);
        clientDocument.setDocument(doc);
        clientDocument.setClient(clientEnt);
        clientDocuments.add(clientDocument);

        clientDocument = new ClientDocumentEntity();
        clientDocument.setId(2L);
        doc = new DocumentEntity();
        doc.setId(2L);
        doc.setMinimunRequirements(true);
        clientDocument.setDocument(doc);
        clientDocument.setClient(clientEnt);
        clientDocuments.add(clientDocument);

        clientDocument = new ClientDocumentEntity();
        clientDocument.setId(3L);
        doc = new DocumentEntity();
        doc.setId(3L);
        doc.setMinimunRequirements(false);
        clientDocument.setDocument(doc);
        clientDocument.setClient(clientEnt);
        clientDocuments.add(clientDocument);

        clientDocument = new ClientDocumentEntity();
        clientDocument.setId(4L);
        doc = new DocumentEntity();
        doc.setId(4L);
        doc.setMinimunRequirements(false);
        clientDocument.setDocument(doc);
        clientDocument.setClient(clientEnt);
        clientDocuments.add(clientDocument);

        when(solicitudeRepository.findById(1L)).thenReturn(Optional.of(solicitude));
        when(clientDocumentRepository.findByClientId(1L)).thenReturn(clientDocuments);
        when(documentRepository.findDocumentsByMinimunRequirements(true)).thenReturn(minimunDocuments);
        when(loanRequirementRepository.findDocumentsByTypeLoanId(1L)).thenReturn(loanDocuments);
        when(clientDatesRepository.findByClientId(1L)).thenReturn(clientDates);
    }
}
