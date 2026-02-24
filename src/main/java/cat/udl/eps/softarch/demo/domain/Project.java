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
    @Override
    public void getVisibility() {return this.visibility}
    public void setVisibility(enum visibility) {this.visibility = visibility}
}
