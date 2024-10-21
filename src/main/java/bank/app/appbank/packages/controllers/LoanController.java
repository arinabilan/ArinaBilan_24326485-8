package bank.app.appbank.packages.controllers;

import bank.app.appbank.packages.entities.LoanEntity;
import bank.app.appbank.packages.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @GetMapping("/{id}")
    public ResponseEntity<LoanEntity> getLoan(@PathVariable Long id) {
        LoanEntity loan = loanService.getLoan(id);
        return ResponseEntity.ok(loan);
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<List<LoanEntity>> findBySolicitudeClientByRut(@PathVariable String rut) {
        List<LoanEntity> loans = loanService.findBySolicitudeClientByRut(rut);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/rut/true/{rut}/")
    public ResponseEntity<List<LoanEntity>> findBySolicitudeClientRutAndApprovedTrue(@PathVariable String clientRut){
        List<LoanEntity> loans = loanService.findBySolicitudeClientRutAndApprovedTrue(clientRut);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/type/{typeId}/{bool}")
    public ResponseEntity<Long> countByTypeIdAndApprovedTrue(@PathVariable Long typeId, @PathVariable Boolean approved){
        Long count = loanService.countByTypeIdAndApproved(typeId, approved);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/")
    public ResponseEntity<LoanEntity> addLoan(@RequestBody LoanEntity loan) {
        LoanEntity newLoan = loanService.addLoan(loan);
        return ResponseEntity.ok(newLoan);
    }

    @PutMapping("/")
    public ResponseEntity<LoanEntity> updateLoan(@RequestBody LoanEntity loan) {
        LoanEntity updated = loanService.updateLoan(loan);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteLoan(@PathVariable Long id) throws Exception{
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }
}
