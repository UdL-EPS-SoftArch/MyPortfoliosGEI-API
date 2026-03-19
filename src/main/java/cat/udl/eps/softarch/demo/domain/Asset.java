package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Needed by JPA
@Entity
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
    private String url;

    // --- Relationships ---
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Project belongsTo;

    // --- Ownership & Authorship ---

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User createdBy;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User lastModifiedBy;

    // --- Timestamps (managed via JPA lifecycle callbacks) ---
    @DateTimeFormat
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @DateTimeFormat
    @Column(nullable = false)
    private ZonedDateTime updatedAt;

    public Asset(String id, String name, String description,
                 String contentType, String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.contentType = contentType;
        this.url = url;
    }

    @PrePersist
    protected void onCreate() {
        ZonedDateTime now = ZonedDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }
}