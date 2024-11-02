package bank.app.appbank.services;

import bank.app.appbank.packages.entities.DocumentEntity;
import bank.app.appbank.packages.entities.LoanRequirementEntity;
import bank.app.appbank.packages.repositories.LoanRequirementRepository;
import bank.app.appbank.packages.services.LoanRequirementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
public class LoanRequirementServiceTest {

    @Mock
    private LoanRequirementRepository loanRequirementRepository;

    @InjectMocks
    private LoanRequirementService loanRequirementService;

    private LoanRequirementEntity loanRequirement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanRequirement = new LoanRequirementEntity();
        loanRequirement.setId(1L);
        loanRequirement.setMaxMonths(12);
        loanRequirement.setInterestRate(BigDecimal.valueOf(0.05));
        loanRequirement.setMaxAmount(BigDecimal.valueOf(10000));
        loanRequirement.setDocuments(new ArrayList<>());
    }

    @Test
    public void whenGetAllLoanRequirements_thenReturnAllLoanRequirements() {
        // given
        ArrayList<LoanRequirementEntity> requirements = new ArrayList<>();;
        requirements.add(loanRequirement);
        requirements.add(new LoanRequirementEntity(2L, 24, BigDecimal.valueOf(0.06), BigDecimal.valueOf(20000), null, new ArrayList<>()));
        when(loanRequirementRepository.findAll()).thenReturn(requirements);

        // when
        ArrayList<LoanRequirementEntity> result = loanRequirementService.getAllLoanRequirements();

        // then
        assertThat(result).hasSize(2).containsExactlyInAnyOrderElementsOf(requirements);
    }

    @Test
    public void whenFindByTypeLoanId_thenReturnLoanRequirement() {
        // given
        Long typeLoanId = 1L;
        when(loanRequirementRepository.findByTypeLoanId(typeLoanId)).thenReturn(loanRequirement);

        // when
        LoanRequirementEntity result = loanRequirementService.findByTypeLoanId(typeLoanId);

        // then
        assertThat(result).isEqualTo(loanRequirement);
    }

    @Test
    public void whenFindMaxMonthsByTypeLoanId_thenReturnMaxMonths() {
        // given
        Long typeLoanId = 1L;
        Integer expectedMaxMonths = 12;

        when(loanRequirementRepository.findMaxMonthsByTypeLoanId(typeLoanId)).thenReturn(expectedMaxMonths);

        // when
        Integer result = loanRequirementService.findMaxMonthsByTypeLoanId(typeLoanId);

        // then
        assertThat(result).isEqualTo(expectedMaxMonths);
    }

    @Test
    public void whenFindInterestRateByTypeLoanId_thenReturnInterestRate() {
        // given
        Long typeLoanId = 1L;
        BigDecimal expectedInterestRate = BigDecimal.valueOf(0.05);

        when(loanRequirementRepository.findInterestRateByTypeLoanId(typeLoanId)).thenReturn(expectedInterestRate);

        // when
        BigDecimal result = loanRequirementService.findInterestRateByTypeLoanId(typeLoanId);

        // then
        assertThat(result).isEqualTo(expectedInterestRate);
    }

    @Test
    public void whenFindMaxAmountByTypeLoanId_thenReturnMaxAmount() {
        // given
        Long typeLoanId = 1L;
        BigDecimal expectedMaxAmount = BigDecimal.valueOf(10000);

        when(loanRequirementRepository.findMaxAmountByTypeLoanId(typeLoanId)).thenReturn(expectedMaxAmount);

        // when
        BigDecimal result = loanRequirementService.findMaxAmountByTypeLoanId(typeLoanId);

        // then
        assertThat(result).isEqualTo(expectedMaxAmount);
    }

    @Test
    public void whenUpdateLoanRequirement_thenReturnUpdatedLoanRequirement() {
        // given
        when(loanRequirementRepository.save(loanRequirement)).thenReturn(loanRequirement);

        // when
        LoanRequirementEntity result = loanRequirementService.updateLoanRequirement(loanRequirement);

        // then
        assertThat(result).isEqualTo(loanRequirement);
    }

    @Test
    public void whenPercentRate_thenReturnCorrectPercentage() {
        // given
        double interestRate = 0.05;
        double expectedPercentage = 5.0;

        // when
        double result = loanRequirementService.percentRate(interestRate);

        // then
        assertThat(result).isEqualTo(expectedPercentage);
    }

    @Test
    public void whenYear_thenReturnCorrectYears() {
        // given
        double months = 24;
        double expectedYears = 2.0;

        // when
        double result = loanRequirementService.year(months);

        // then
        assertThat(result).isEqualTo(expectedYears);
    }

    @Test
    public void whenFindDocumentsByTypeLoanId_thenReturnDocuments() {
        // given
        Long typeLoanId = 1L;
        DocumentEntity document1 = new DocumentEntity(); // Crea documentos de ejemplo
        document1.setId(1L);
        document1.setTitle("Document 1");

        DocumentEntity document2 = new DocumentEntity();
        document2.setId(2L);
        document2.setTitle("Document 2");

        List<DocumentEntity> expectedDocuments = new ArrayList<>();
        expectedDocuments.add(document1);
        expectedDocuments.add(document2);

        // Simula el comportamiento del repositorio
        when(loanRequirementRepository.findDocumentsByTypeLoanId(typeLoanId)).thenReturn(expectedDocuments);

        // when
        List<DocumentEntity> result = loanRequirementService.findDocumentsByTypeLoanId(typeLoanId);

        // then
        assertThat(result).hasSize(2).containsExactlyInAnyOrderElementsOf(expectedDocuments);
    }
}
