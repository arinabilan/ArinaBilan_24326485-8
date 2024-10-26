package bank.app.appbank.packages.services;

import bank.app.appbank.packages.entities.SolicitudeEntity;
import bank.app.appbank.packages.repositories.SolicitudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SolicitudeService {
    @Autowired
    private SolicitudeRepository solicitudeRepository;

    public SolicitudeEntity saveSolicitude(SolicitudeEntity solicitude) {
        float amount = solicitude.getAmount();
        float maxAmount = solicitude.getMaxAmount();
        float totalAmount = calculateAmount(amount, maxAmount);
        solicitude.setCalculatedAmount(totalAmount);
        return solicitudeRepository.save(solicitude);
    }

    float calculateAmount(float amount, float maxAmount){
        return amount * maxAmount;
    }


}
