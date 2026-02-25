package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


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
    private String description;

    @Setter
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Setter
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User creator;

    protected Portfolio() {}

    private void createPortfolio(String name, String description, Visibility visibility, User creator){

        Portfolio p = new Portfolio();
        p.id = Long.parseLong(name);        // Temporally solution
        p.name = name;
        p.description = description;
        p.visibility = visibility;
        p.creator = creator;
    }

    private void editPortfolio(String name, String description, Visibility visibility){
        if(name != null && !name.trim().isEmpty()){
            this.name = name;
        }
        this.description = description;
        this.visibility = visibility;
    }
}