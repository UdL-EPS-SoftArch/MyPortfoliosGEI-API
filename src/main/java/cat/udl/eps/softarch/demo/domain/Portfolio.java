package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


@Entity
@Getter
@Data
@EqualsAndHashCode(callSuper = true)
public class Portfolio extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    @Setter
    private String name;

    @Setter
    @Column(length = 2000)
    @Length(max = 2000)
    private String description;

    @Setter
    @Column(nullable = false)
    private Boolean isPrivate = false;

    @Setter
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User creator;

    protected Portfolio() {}
}