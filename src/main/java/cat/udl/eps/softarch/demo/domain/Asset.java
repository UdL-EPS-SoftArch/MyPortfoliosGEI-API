package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.Objects;

@Entity
@Table(name = "Asset")
public class Asset extends UriEntity<String> {

    @Id
    private String id;

    @Getter
    @NotBlank
    @Column(nullable = false)
    private String name;

    @Getter
    @Column(length = 2000)
    private String description;

    protected Asset() {
        // Required by JPA
    }

    public Asset(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public String getId() {
        return id;
    }

    public Asset setId(String id) {
        this.id = id;
        return this;
    }

    public Asset setName(String name) {
        this.name = name;
        return this;
    }

    public Asset setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Asset other)) return false;
        return id != null && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Asset{id='" + id + "', name='" + name + "'}";
    }
}
