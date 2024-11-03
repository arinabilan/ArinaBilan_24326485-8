package bank.app.appbank.services;
import bank.app.appbank.packages.entities.TypeLoanEntity;
import bank.app.appbank.packages.repositories.TypeLoanRepository;
import bank.app.appbank.packages.services.TypeLoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TypeLoanServiceTest {
    @InjectMocks
    private TypeLoanService typeLoanService;

    @Mock
    private TypeLoanRepository typeLoanRepository;

    private TypeLoanEntity mockTypeLoan;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockTypeLoan = new TypeLoanEntity();
        mockTypeLoan.setId(1L);
        mockTypeLoan.setType("Personal Loan");
    }

    @Test
    void testGetAllTypeLoans() {
        List<TypeLoanEntity> mockLoans = new ArrayList<>();
        mockLoans.add(mockTypeLoan);

        when(typeLoanRepository.findAll()).thenReturn(mockLoans);

        ArrayList<TypeLoanEntity> result = typeLoanService.getAllTypeLoans();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockTypeLoan.getType(), result.get(0).getType());
        verify(typeLoanRepository, times(1)).findAll();
    }

    @Test
    void testGetTypeLoan() {
        when(typeLoanRepository.getLoanByType(mockTypeLoan.getType())).thenReturn(mockTypeLoan);

        TypeLoanEntity result = typeLoanService.getTypeLoan(mockTypeLoan.getType());

        assertNotNull(result);
        assertEquals(mockTypeLoan.getId(), result.getId());
        verify(typeLoanRepository, times(1)).getLoanByType(mockTypeLoan.getType());
    }

    @Test
    void testCreateTypeLoan() {
        when(typeLoanRepository.save(any(TypeLoanEntity.class))).thenReturn(mockTypeLoan);

        TypeLoanEntity result = typeLoanService.createTypeLoan(mockTypeLoan);

        assertNotNull(result);
        assertEquals(mockTypeLoan.getId(), result.getId());
        assertEquals(mockTypeLoan.getType(), result.getType());
        verify(typeLoanRepository, times(1)).save(mockTypeLoan);
    }
}
