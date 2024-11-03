package bank.app.appbank.services;


import bank.app.appbank.packages.entities.LoanEntity;
import bank.app.appbank.packages.entities.SolicitudeEntity;
import bank.app.appbank.packages.entities.TypeLoanEntity;
import bank.app.appbank.packages.repositories.LoanRepository;
import bank.app.appbank.packages.services.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanServiceTest {
    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanService loanService;

    private LoanEntity loan;
    private String clientRut = "12345678-9";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuración de datos de prueba
        SolicitudeEntity solicitude = new SolicitudeEntity();
        TypeLoanEntity typeLoan = new TypeLoanEntity();
        loan = new LoanEntity(1L, 500, 50, 20, 570, 10000, true, typeLoan, solicitude);
    }

    @Test
    void testGetLoan_WhenLoanExists() {
        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));

        LoanEntity foundLoan = loanService.getLoan(loan.getId());

        assertNotNull(foundLoan);
        assertEquals(loan.getId(), foundLoan.getId());
        verify(loanRepository, times(1)).findById(loan.getId());
    }

    @Test
    void testGetLoan_WhenLoanDoesNotExist() {
        when(loanRepository.findById(loan.getId())).thenReturn(Optional.empty());

        LoanEntity foundLoan = loanService.getLoan(loan.getId());

        assertNull(foundLoan);
        verify(loanRepository, times(1)).findById(loan.getId());
    }

    @Test
    void testFindBySolicitudeClientByRut() {
        List<LoanEntity> loans = new ArrayList<>();
        loans.add(loan);

        when(loanRepository.findBySolicitudeClientRut(clientRut)).thenReturn(loans);

        List<LoanEntity> result = loanService.findBySolicitudeClientByRut(clientRut);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(loans, result);
        verify(loanRepository, times(1)).findBySolicitudeClientRut(clientRut);
    }

    @Test
    void testFindBySolicitudeClientRutAndApprovedTrue() {
        List<LoanEntity> approvedLoans = new ArrayList<>();
        approvedLoans.add(loan);

        when(loanRepository.findBySolicitudeClientRutAndApprovedTrue(clientRut)).thenReturn(approvedLoans);

        List<LoanEntity> result = loanService.findBySolicitudeClientRutAndApprovedTrue(clientRut);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getApproved());
        verify(loanRepository, times(1)).findBySolicitudeClientRutAndApprovedTrue(clientRut);
    }

    @Test
    void testCountByTypeIdAndApproved() {
        Long typeId = loan.getType().getId();
        Boolean approved = true;
        Long expectedCount = 5L;

        when(loanRepository.countByTypeIdAndApproved(typeId, approved)).thenReturn(expectedCount);

        Long count = loanService.countByTypeIdAndApproved(typeId, approved);

        assertEquals(expectedCount, count);
        verify(loanRepository, times(1)).countByTypeIdAndApproved(typeId, approved);
    }

    @Test
    void testAddLoan() {
        when(loanRepository.save(loan)).thenReturn(loan);

        LoanEntity savedLoan = loanService.addLoan(loan);

        assertNotNull(savedLoan);
        assertEquals(loan.getId(), savedLoan.getId());
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void testUpdateLoan() {
        when(loanRepository.save(loan)).thenReturn(loan);

        LoanEntity updatedLoan = loanService.updateLoan(loan);

        assertNotNull(updatedLoan);
        assertEquals(loan.getId(), updatedLoan.getId());
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void testDeleteLoan_WhenLoanExists() throws Exception {
        doNothing().when(loanRepository).deleteById(loan.getId());

        Boolean result = loanService.deleteLoan(loan.getId());

        assertTrue(result);
        verify(loanRepository, times(1)).deleteById(loan.getId());
    }

    @Test
    void testDeleteLoan_WhenExceptionOccurs() {
        doThrow(new RuntimeException("Delete failed")).when(loanRepository).deleteById(loan.getId());

        Exception exception = assertThrows(Exception.class, () -> loanService.deleteLoan(loan.getId()));

        assertEquals("Delete failed", exception.getMessage());
        verify(loanRepository, times(1)).deleteById(loan.getId());
    }
}
