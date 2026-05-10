package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.HashSet;
import java.util.Set;
import java.time.ZonedDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Record extends UriEntity<Long> {

    @ManyToMany
    @JoinTable(
        name = "record_tags",
        joinColumns = @JoinColumn(name = "record_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Tag> tags = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<Record> records = new HashSet<>();

    private String description;

    @DateTimeFormat
    private ZonedDateTime created;

    @DateTimeFormat
    private ZonedDateTime modified;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User ownedBy;


    @Override
    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnedBy(User owner) {
        this.ownedBy = owner;
    }

    public void setCreated(ZonedDateTime timeStamp) {
        this.created = timeStamp;
    }

    public void setModified(ZonedDateTime timeStamp) {
        this.modified = timeStamp;
    }

    public void setDescription(String s) {
        this.description = s;
    }

    public ZonedDateTime getCreated() {
        return created;
    }
}
