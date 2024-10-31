package bank.app.appbank.packages.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
//@Table(name="client_documents")
@Table(
        name="client_documents",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"Client_Id", "Document_Id"})}
)
@Data
@AllArgsConstructor
@NoArgsConstructor

//esta clase(entidad) es de documento que se está subiendo el cliente al momento de solicitar su prestamo
public class ClientDocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private LocalDate fechaCarga; //cuando se cargo dicho documento al sistema
    private String rutaDocumento; //la ubicacion del documento
    private Boolean estado; //estado del documento(si es valido o no, lo debe aprobar ejecutivo)

    @ManyToOne
    @JoinColumn(name = "Client_Id", nullable = false)
    private ClientEntity client; //el dicho documento esta asociado a entidad cliente, a un body entero de cliente


    @ManyToOne
    @JoinColumn(name = "Document_Id", nullable = false)
    private DocumentEntity document; //el dicho documento ingresado por el cliente esta asociado a entidad
    //documento que consiste en entidad-descripcion del documento, tiene id y titulo de documento

}
