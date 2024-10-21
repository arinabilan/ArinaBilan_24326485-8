package bank.app.appbank.packages.services;

import bank.app.appbank.packages.entities.LoanEntity;
import bank.app.appbank.packages.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    public LoanEntity getLoan(Long id) {
        Optional<LoanEntity> loan = loanRepository.findById(id);
        return loan.orElse(null); // Uso seguro de Optional
    }

    public List<LoanEntity> findBySolicitudeClientByRut(String clientRut) {
        return loanRepository.findBySolicitudeClientRut(clientRut);
    }

    public List<LoanEntity> findBySolicitudeClientRutAndApprovedTrue(String clientRut) {
        return loanRepository.findBySolicitudeClientRutAndApprovedTrue(clientRut);
    }

    public Long countByTypeIdAndApproved(Long typeId, Boolean approved){
        return loanRepository.countByTypeIdAndApproved(typeId, approved);
    }

    public LoanEntity addLoan(LoanEntity loan) {
        return loanRepository.save(loan);
    }

    public LoanEntity updateLoan(LoanEntity loan) {
        return loanRepository.save(loan);
    }

    public Boolean deleteLoan(Long id) throws Exception {
        try {
            loanRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


}
