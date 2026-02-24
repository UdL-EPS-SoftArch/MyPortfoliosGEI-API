package cat.udl.eps.softarch.demo.domain;

import org.springframework.lang.Nullable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

public class Project {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private boolean flagged;


    private Visibility visibility;


    @Nullable
    public Visibility getVisibility() {
        return this.visibility;
    }
    public void setVisibility(Visibility visibility) {this.visibility = visibility;}

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFlagged(String Flagged) {
        this.flagged = flagged;
    }
}
