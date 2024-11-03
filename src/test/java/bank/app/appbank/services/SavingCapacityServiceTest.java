package bank.app.appbank.services;
import bank.app.appbank.packages.entities.ClientDatesEntity;
import bank.app.appbank.packages.entities.SavingCapacityEntity;
import bank.app.appbank.packages.entities.SolicitudeEntity;
import bank.app.appbank.packages.repositories.ClientDatesRepository;
import bank.app.appbank.packages.repositories.SavingCapacityRepository;
import bank.app.appbank.packages.repositories.SolicitudeRepository;
import bank.app.appbank.packages.services.SavingCapacityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SavingCapacityServiceTest {

    @Mock
    private SavingCapacityRepository savingCapacityRepository;
    @Mock
    private SolicitudeRepository solicitudeRepository;
    @Mock
    private ClientDatesRepository clientDatesRepository;

    @InjectMocks
    private SavingCapacityService savingCapacityService;

    private SavingCapacityEntity savingCapacity;
    private SolicitudeEntity solicitude;
    private ClientDatesEntity clientDates;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuración de datos de prueba
        savingCapacity = new SavingCapacityEntity();
        savingCapacity.setId(1);
        savingCapacity.setBalance(50000);
        savingCapacity.setWithdrawal(true);
        savingCapacity.setDeposits(2000);
        savingCapacity.setYearsSavings(3);
        savingCapacity.setClient(null); // Configura un objeto mock o real de ClientEntity si es necesario

        solicitude = new SolicitudeEntity();
        solicitude.setId(1L);
        solicitude.setAmount(300000);
        solicitude.setCalculatedAmount(350000);

        clientDates = new ClientDatesEntity();
        clientDates.setId(1L);
        clientDates.setMonthSalary(25000);
    }

    @Test
    void testGetByClientId_WhenClientExists() {
        when(savingCapacityRepository.findByClientId(1L)).thenReturn(savingCapacity);

        SavingCapacityEntity foundCapacity = savingCapacityService.getByClientId(1L);

        assertNotNull(foundCapacity);
        assertEquals(savingCapacity.getId(), foundCapacity.getId());
        verify(savingCapacityRepository, times(1)).findByClientId(1L);
    }

    @Test
    void testGetByClientId_WhenClientDoesNotExist() {
        when(savingCapacityRepository.findByClientId(1L)).thenReturn(null);

        SavingCapacityEntity foundCapacity = savingCapacityService.getByClientId(1L);

        assertNull(foundCapacity);
        verify(savingCapacityRepository, times(1)).findByClientId(1L);
    }

    @Test
    void testSaveCapacity_SuccessfullySaves() {
        when(savingCapacityRepository.save(savingCapacity)).thenReturn(savingCapacity);

        SavingCapacityEntity savedCapacity = savingCapacityService.saveCapacity(savingCapacity);

        assertNotNull(savedCapacity);
        assertEquals(savingCapacity.getId(), savedCapacity.getId());
        verify(savingCapacityRepository, times(1)).save(savingCapacity);
    }

    @Test
    void testCalculateState_ScoreSolid() {
        when(savingCapacityRepository.findByClientId(1L)).thenReturn(savingCapacity);
        when(solicitudeRepository.findBySolicitudeId(1L)).thenReturn(solicitude);
        when(clientDatesRepository.findByClientId(1L)).thenReturn(clientDates);

        // Ajusta datos de prueba para obtener un score de 5
        savingCapacity.setBalance(40000); // cumple con saldo mínimo
        savingCapacity.setWithdrawal(true); // historial consistente
        savingCapacity.setDeposits(1500); // cumple con depósitos periódicos
        savingCapacity.setYearsSavings(3); // más de 2 años, cumple con R74

        int estado_resultado = savingCapacityService.calculateState(1L, 1L);

        assertEquals(2, estado_resultado); // Estado "sólido"
    }

    @Test
    void testCalculateState_ScoreModerate() {
        when(savingCapacityRepository.findByClientId(1L)).thenReturn(savingCapacity);
        when(solicitudeRepository.findBySolicitudeId(1L)).thenReturn(solicitude);
        when(clientDatesRepository.findByClientId(1L)).thenReturn(clientDates);

        // Ajusta datos de prueba para obtener un score entre 3-4
        savingCapacity.setBalance(30000); // cumple con saldo mínimo
        savingCapacity.setWithdrawal(false); // historial no consistente
        savingCapacity.setDeposits(1000); // no cumple completamente con depósitos periódicos
        savingCapacity.setYearsSavings(1); // menos de 2 años

        int estado_resultado = savingCapacityService.calculateState(1L, 1L);

        assertEquals(0, estado_resultado); // Estado "moderado"
    }

    @Test
    void testCalculateState_SaldoMenorQue20PorCientoDelMontoSolicitado() {
        when(savingCapacityRepository.findByClientId(1L)).thenReturn(savingCapacity);
        when(solicitudeRepository.findBySolicitudeId(1L)).thenReturn(solicitude);
        when(clientDatesRepository.findByClientId(1L)).thenReturn(clientDates);

        // Configuramos la capacidad de ahorro y solicitud de forma que el balance sea menor al 20% del monto solicitado
        savingCapacity.setBalance(5000); // balance que es menor al 20% de 300000 (monto de la solicitud)
        savingCapacity.setYearsSavings(1); // menos de 2 años de antigüedad para activar la condición
        savingCapacity.setWithdrawal(true); // historial consistente para no afectar el score por esta regla
        savingCapacity.setDeposits(1500); // depósitos aceptables para no afectar el score por esta regla

        // Calculamos el estado de evaluación
        int estado_resultado = savingCapacityService.calculateState(1L, 1L);

        // Verificamos que el score refleja la penalización y que el estado es "moderado" o "insuficiente"
        assertTrue(estado_resultado >= 2 && estado_resultado <= 3, "El estado debería ser moderado o insuficiente debido al balance bajo en relación al monto solicitado");
    }

    @Test
    void testCalculateState_SaldoMenorQue10PorCientoDelMontoSolicitadoConAntiguedadMayorA2() {
        when(savingCapacityRepository.findByClientId(1L)).thenReturn(savingCapacity);
        when(solicitudeRepository.findBySolicitudeId(1L)).thenReturn(solicitude);
        when(clientDatesRepository.findByClientId(1L)).thenReturn(clientDates);

        // Configuramos la capacidad de ahorro y solicitud de forma que el balance sea menor al 10% del monto solicitado
        savingCapacity.setBalance(2000); // balance menor al 10% de 300000 (monto de la solicitud)
        savingCapacity.setYearsSavings(3); // más de 2 años de antigüedad para activar la condición del else
        savingCapacity.setWithdrawal(true); // historial consistente para no afectar el score por esta regla
        savingCapacity.setDeposits(1500); // depósitos aceptables para no afectar el score por esta regla

        // Calculamos el estado de evaluación
        int estado_resultado = savingCapacityService.calculateState(1L, 1L);

        // Verificamos que el score refleja la penalización y que el estado es "moderado" o "insuficiente"
        assertTrue(estado_resultado >= 2 && estado_resultado <= 3, "El estado debería ser moderado o insuficiente debido al balance bajo en relación al monto solicitado");
    }



}
