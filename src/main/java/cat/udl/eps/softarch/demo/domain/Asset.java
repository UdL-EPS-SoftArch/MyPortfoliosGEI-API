package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Getter
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
    private Long size;            // in bytes

    @Column(nullable = false, unique = true)
    private String storageKey;    // path or S3 key

    // --- Timestamps ---
    @DateTimeFormat
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @DateTimeFormat
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

    // --- Update Methods ---

    /**
     * Updates the user-facing metadata of the asset.
     */
    public void updateMetadata(String name, String description) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
    }

    /**
     * Replaces the underlying physical file metadata.
     * These fields are grouped because changing the file changes all three.
     */
    public void updateFile(String contentType, Long size, String storageKey) {
        if (contentType != null && size != null && storageKey != null) {
            this.contentType = contentType;
            this.size = size;
            this.storageKey = storageKey;
        }
    }

    // --- Auto-Managed JPA Lifecycle Hooks ---

    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }
}