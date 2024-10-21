package bank.app.appbank.packages.controllers;

import bank.app.appbank.packages.entities.DocumentEntity;
import bank.app.appbank.packages.entities.LoanRequirementEntity;
import bank.app.appbank.packages.services.LoanRequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/loanRequirements")
public class LoanRequirementController {
    @Autowired
    private LoanRequirementService loanRequirementService;

    @GetMapping("/")
    public ResponseEntity<List<LoanRequirementEntity>> findAll() {
        List<LoanRequirementEntity> requirements = loanRequirementService.getAllLoanRequirements();
        return ResponseEntity.ok(requirements);
    }

    @GetMapping("/{typeId}")
    public ResponseEntity<LoanRequirementEntity> findByTypeLoanId(@PathVariable Long typeId){
        LoanRequirementEntity loanRequirement = loanRequirementService.findByTypeLoanId(typeId);
        return ResponseEntity.ok(loanRequirement);
    }

    @GetMapping("/maxMonths/{id}")
    public ResponseEntity<Integer> findMaxMonthsByTypeLoanId(@PathVariable Long id){
        Integer maxMonths = loanRequirementService.findMaxMonthsByTypeLoanId(id);
        return ResponseEntity.ok(maxMonths);
    }

    @GetMapping("/interest/{id}/rate")
    public ResponseEntity<BigDecimal> findInterestRateByTypeLoanId(@PathVariable Long id) {
        BigDecimal interestRate = loanRequirementService.findInterestRateByTypeLoanId(id);
        return ResponseEntity.ok(interestRate);
    }

    @GetMapping("/max/amount/{id}")
    public ResponseEntity<BigDecimal> findMaxAmountByTypeLoanId(@PathVariable Long id){
        BigDecimal maxAmount = loanRequirementService.findMaxAmountByTypeLoanId(id);
        return ResponseEntity.ok(maxAmount);
    }

    @GetMapping("/documents/list/loan/{id}")
    public ResponseEntity<List<DocumentEntity>> findLoanRequirementsByTypeLoanId(@PathVariable Long id){
        List<DocumentEntity> listDocuments = loanRequirementService.findDocumentsByTypeLoanId(id);
        return ResponseEntity.ok(listDocuments);
    }

    @PostMapping("/")
    public ResponseEntity<LoanRequirementEntity> save(@RequestBody LoanRequirementEntity loanRequirement){
        LoanRequirementEntity updated = loanRequirementService.updateLoanRequirement(loanRequirement);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/")
    public ResponseEntity<LoanRequirementEntity> updateLoanRequirement(@RequestBody LoanRequirementEntity loanRequirement){
        LoanRequirementEntity updated = loanRequirementService.updateLoanRequirement(loanRequirement);
        return ResponseEntity.ok(updated);
    }
}
