package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tag extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @EqualsAndHashCode.Include
    @Pattern(
        regexp = "^[a-zA-Z0-9 ]+$",
        message = "Only alphanumeric characters and spaces are allowed"
    )
    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<Record> records = new HashSet<>();

    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }
}