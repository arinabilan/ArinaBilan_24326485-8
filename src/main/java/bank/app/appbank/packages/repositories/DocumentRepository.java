package bank.app.appbank.packages.repositories;

import bank.app.appbank.packages.entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
//Sera descripcion de documentos que se necesitan para cada prestamo
@Repository
public interface DocumentRepository extends JpaRepository <DocumentEntity, Long> {
    public DocumentEntity findDocumentById(long id);
    public DocumentEntity findDocumentByTitle(String title);
}
