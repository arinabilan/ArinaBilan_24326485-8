package bank.app.appbank.packages.services;

import bank.app.appbank.packages.entities.*;
import bank.app.appbank.packages.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudeService {
    @Autowired
    private SolicitudeRepository solicitudeRepository;
    @Autowired
    private ClientDocumentRepository clientDocumentRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private LoanRequirementRepository loanRequirementRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientDatesRepository clientDatesRepository;

    public ArrayList<SolicitudeEntity> getAllSolicitudes() {
        return (ArrayList<SolicitudeEntity>) solicitudeRepository.findAll();
    }

    public List<SolicitudeEntity> getAllSolicitudesByClientId(Long clientId) {
        return solicitudeRepository.findByClientId(clientId);
    }

    public SolicitudeEntity saveSolicitude(SolicitudeEntity solicitude) {
        float amount = solicitude.getAmount();
        float maxAmount = solicitude.getMaxAmount();
        float totalAmount = calculateAmount(amount, maxAmount);
        int years = solicitude.getDeadline();
        int months = calculateMonths(years);
        solicitude.setCalculatedAmount(totalAmount);
        solicitude.setDeadline(months);
        return solicitudeRepository.save(solicitude);
    }

    float calculateAmount(float amount, float maxAmount){
        return amount * maxAmount;
    }

    int calculateMonths(int years){
        return years * 12;
    }

    public SolicitudeEntity updateSolicitude(Long id, ExecutiveEntity executive, int state) {
        SolicitudeEntity updated = solicitudeRepository.findById(id).get();
        if (state == 1) { // En Revisión Inicial
            updated.setExecutive(executive);
        }
        updated.setState(state);
        return solicitudeRepository.save(updated);
    }

    public SolicitudeEntity EvaluateSolicitude(Long id) {
        SolicitudeEntity solicitude = solicitudeRepository.findById(id).get();
        // si la solicitud ya pasó de En evaluación
        if (solicitude.getState() > 3) {
            return solicitude;
        }
        Long clientId = solicitude.getClient().getId();
        Long loanId = solicitude.getLoanType().getId(); //agregue esta linea
        ArrayList<ClientDocumentEntity> documentsClient =  clientDocumentRepository.findByClientId(clientId);
        List<Long> documentIds = documentsClient.stream()
                .map(clientDocument -> clientDocument.getDocument().getId())
                .collect(Collectors.toList());

        // 1 - Verifica si la documentación mínima requerida + la documentación del tipo de prestamo está completa
        ArrayList<DocumentEntity> documentsMinimun = documentRepository.findDocumentsByMinimunRequirements(true);
        List<Long> documentIds1 = documentsMinimun.stream()
                .map(DocumentEntity::getId)
                .collect(Collectors.toList());
        boolean documentsMinimunRequired = documentsClient.isEmpty() && documentsMinimun.isEmpty() || documentIds.containsAll(documentIds1); //agregue All
        ArrayList<DocumentEntity> documentByLoan = (ArrayList<DocumentEntity>) loanRequirementRepository.findDocumentsByTypeLoanId(loanId); // cambie de clientId a loanId
        List<Long> documentIdsByLoan = documentByLoan.stream()
                .map(DocumentEntity::getId)
                .collect(Collectors.toList());
        boolean documentsLLoanRequired = documentsClient.isEmpty() && documentByLoan.isEmpty() || documentIds.containsAll(documentIdsByLoan); //agregue All
        if (!documentsMinimunRequired || !documentsLLoanRequired) {
            solicitude.setState(2);
            return solicitudeRepository.save(solicitude);
        }

        ClientDatesEntity clientDates = clientDatesRepository.findByClientId(clientId);
        // 2(R1) - Verifica que la relación cuota/ingreso no sea mayor a 35%
        double monthCuote = clientService.simulateAmount(solicitude.getAmount(), solicitude.getInterestRate() * 100, (double) (solicitude.getDeadline() / 12));
        int monthSalary = clientDates.getMonthSalary();
        double relationCuoteSalary = (monthCuote / monthSalary) * 100;
        if (relationCuoteSalary > 35) {
            solicitude.setState(7); // rechazada
            return solicitudeRepository.save(solicitude);
        }

        // 3(R2) - Verifica si tiene buen historial crediticio (DICOM)
        if (clientDates.getDicom()) {
            solicitude.setState(7); // rechazada
            return solicitudeRepository.save(solicitude);
        }

        // 4(R3) - Verificación de estado contractual
        if (!((clientDates.getType() == 1 &&
                clientDates.getMediaSalary() > 750000 &&
                clientDates.getMonthSalary() - clientDates.getMonthlyDebt() > 500000)
            || (clientDates.getType() == 2 && clientDates.getInitialContract() > 1 ))) {
            solicitude.setState(7); // rechazada
            return solicitudeRepository.save(solicitude);
        }

        // 5(R4) - Verificación deuda/ingreso
        double relationDebtSalary = ((monthCuote + clientDates.getMonthlyDebt()) / monthSalary) * 100;
        if (relationDebtSalary > 50) {
            solicitude.setState(7); // rechazada
            return solicitudeRepository.save(solicitude);
        }

        // 6(R6) - Verificación edad
        int currentAge = Period.between(clientService.getById(clientId).getBirthday(), LocalDate.now()).getYears();
        if (currentAge + (solicitude.getDeadline() / 12) > 70) {
            solicitude.setState(7); // rechazada
            return solicitudeRepository.save(solicitude);
        }

        solicitude.setState(4); // pre-aprobada
        return solicitudeRepository.save(solicitude);
    }

}
