package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
public class Portfolio extends User {

    @Id
    private String id;
    @NotBlank
    @Setter
    private String name;
    @Setter
    private String description;
    @Setter
    private Visibility visibility;

    private final User creator;

    public Portfolio(String id, String name, String description, Visibility visibility, User creator) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.creator = creator;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}

