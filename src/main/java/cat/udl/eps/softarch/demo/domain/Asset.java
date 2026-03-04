package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Needed by JPA
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Asset extends UriEntity<String> {

    @Id
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    // --- File Metadata ---
    @Column(nullable = false)
    private String contentType;   // e.g., image/png, video/mp4

    @Column(nullable = false)
    private Long size;            // in bytes

    @Column(nullable = false, unique = true)
    private String storageKey;    // path or S3 key

    // --- Relationships ---
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Project belongsTo;

    // --- Ownership & Authorship ---
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User owner;

    @CreatedBy
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User createdBy;

    @LastModifiedBy
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User lastModifiedBy;

    // --- Timestamps (auto-managed by Spring Data JPA Auditing) ---
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private ZonedDateTime updatedAt;

    public Asset(String id, String name, String description,
                 String contentType, Long size, String storageKey) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.contentType = contentType;
        this.size = size;
        this.storageKey = storageKey;
    }
}