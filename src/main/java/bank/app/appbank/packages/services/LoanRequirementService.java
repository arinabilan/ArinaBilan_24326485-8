package bank.app.appbank.packages.services;

import bank.app.appbank.packages.entities.DocumentEntity;
import bank.app.appbank.packages.entities.LoanRequirementEntity;
import bank.app.appbank.packages.repositories.LoanRepository;
import bank.app.appbank.packages.repositories.LoanRequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanRequirementService {
    @Autowired
    private LoanRequirementRepository loanRequirementRepository;

    public ArrayList<LoanRequirementEntity> getAllLoanRequirements() {
        return (ArrayList<LoanRequirementEntity>) loanRequirementRepository.findAll();
    }

    public LoanRequirementEntity findByTypeLoanId(Long typeId) {
        return loanRequirementRepository.findByTypeLoanId(typeId);
    }

    public Integer findMaxMonthsByTypeLoanId(Long id){
        return loanRequirementRepository.findMaxMonthsByTypeLoanId(id);
    }

    public BigDecimal findInterestRateByTypeLoanId(Long id){
        return loanRequirementRepository.findInterestRateByTypeLoanId(id);
    }

    public BigDecimal findMaxAmountByTypeLoanId(Long id){
        return loanRequirementRepository.findMaxAmountByTypeLoanId(id);
    }

    public List<DocumentEntity> findDocumentsByTypeLoanId(Long typeLoanId){
        return loanRequirementRepository.findDocumentsByTypeLoanId(typeLoanId);
    }

    public LoanRequirementEntity updateLoanRequirement(LoanRequirementEntity loanRequirement){
        return loanRequirementRepository.save(loanRequirement);
    }

    public double percentRate(double interestRate){
        return interestRate * 100;
    }

    public double year(double month){
        return Math.floor(month / 12);
    }

}
