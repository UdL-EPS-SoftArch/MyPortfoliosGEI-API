package cat.udl.eps.softarch.demo.domain;

public class Portfolio {

    private String id;
    private String name;
    private String description;
    private Visibility visibility;
    private User creator;

    public Portfolio(String id, String name, String description, Visibility visibility, User creator) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.creator = creator;
    }

    public String getId() {
        return this.id;
    }

    public String setId(String id) {
        return this.id = id;
    }

    public String getName() {
        return name;
    }

    public String setName(String name) {
        return this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String setDescription(String description) {
        return this.description = description;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public User getCreator() {
        return creator;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}

