package ec.edu.espe.arquitectura.wscuentas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "DOCUMENT_TYPE")
public class DocumentType {

    @Id
    @Column(name = "DOCUMENT_TYPE_ID", nullable = false, length = 10)
    private String id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "APPLICABILITY", nullable = false, length = 3)
    private String applicability;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

}
