package bank.app.appbank.packages.services;

import bank.app.appbank.packages.entities.TypeLoanEntity;
import bank.app.appbank.packages.repositories.TypeLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TypeLoanService {
    @Autowired
    private TypeLoanRepository typeLoanRepository;

    public ArrayList<TypeLoanEntity> getAllTypeLoans() {
        return (ArrayList<TypeLoanEntity>) typeLoanRepository.findAll();
    }

    public TypeLoanEntity getTypeLoan(String type) {
        return typeLoanRepository.getLoanByType(type);
    }

    public TypeLoanEntity createTypeLoan(TypeLoanEntity typeLoan) {
        return typeLoanRepository.save(typeLoan);
    }
}
