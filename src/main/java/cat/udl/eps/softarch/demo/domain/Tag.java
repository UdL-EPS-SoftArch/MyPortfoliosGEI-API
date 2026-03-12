package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
    @Column(nullable = false, unique = true)
    private String name;

    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

}