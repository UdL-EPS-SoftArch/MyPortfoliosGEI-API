package cat.udl.eps.softarch.demo.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Tag {

    private String name;
    private Set<Project> projects = new HashSet<>();

    public Tag(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Set<Project> getProjects(){
        return projects;
    }

    public void addProject(Project project){
        projects.add(project);
    }

    public void removeProject(Project project){
        projects.remove(project);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(name);
    }

}
