package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
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
    private Visibility visibility;

    @Setter
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    protected Portfolio() {}

    public Portfolio(Long id, String name, String description, Visibility visibility, User creator) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.creator = creator;
    }

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

