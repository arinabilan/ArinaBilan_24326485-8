package bank.app.appbank.packages.repositories;

import bank.app.appbank.packages.entities.ClientDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

//import static org.hibernate.FetchMode.SELECT;

//este repositorio se conecta con base de datos para obtener algun documento especifico subido por el cliente
@Repository
public interface ClientDocumentRepository extends JpaRepository<ClientDocumentEntity, Long> {

    //findByClientId: Client es porque en ClientDocumentEntity asocié esta clase con entidad de cliente con
    // private ClientEntity client, y como llame variable "client", por esto aparece Client en findByClient
    //va internamente juntando las tablas entre ClientDocumentEntity y ClientEntity, y Id en porque en ClientEntity
    //es el atributo Id.
    public ArrayList<ClientDocumentEntity> findByClientId(Long id);

    public ArrayList<ClientDocumentEntity> findByClientRut(String rut);

    //la siguiente Query tiene por objetivo de buscar el documento subido por el cliente a la base de datos
    //por id de cliente y el titulo de documento
    //Ej: quiero que base de datos me trae el documento de certificado de nacimiento que subio el usuario
    //Jose Perez que tiene id 123.
    @Query("SELECT cd FROM ClientDocumentEntity cd WHERE cd.client.id = :clientId AND cd.document.title = :title")
    public ClientDocumentEntity findByClientIdAndTitle(@Param("clientId") Long clientId, @Param("title") String title);

}


