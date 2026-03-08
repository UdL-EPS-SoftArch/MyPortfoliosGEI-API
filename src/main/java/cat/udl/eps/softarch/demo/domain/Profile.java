package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Profile extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 60)
    private String fullName;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @Size(max = 250)
    private String bio;

    private String avatarUrl;
    private String location;

    /* Social Links */
    private String github;
    private String twitter;
    private String instagram;
    private String linkedin;

    @Builder.Default
    private Boolean isPrivate = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    @JsonIdentityReference(alwaysAsId = true)
    private User user;
}
