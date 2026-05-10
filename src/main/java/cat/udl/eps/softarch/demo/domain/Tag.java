package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = "records")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Tag extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Pattern(
        regexp = "^[\\p{L}0-9 _.-]+$",
        message = "Only letters, numbers, spaces, dots, dashes and underscores are allowed"
    )
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<Record> records = new HashSet<>();

    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

    @PrePersist
    @PreUpdate
    public void normalize() {
        if (name != null) {
            name = name.trim();
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    public void addRecord(Record record) {
        records.add(record);
        record.getTags().add(this);
    }

    public void removeRecord(Record record) {
        records.remove(record);
        record.getTags().remove(this);
    }
}