package miracle.field.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true, nullable = false, length = 32)
    @Pattern(regexp = "[A-Za-z0-9_]{3,16}", message = "username.incorrect")
    private String username;

    @Column(nullable = false)
    @Pattern(regexp = "[A-Za-z0-9_]{6,16}", message = "password.incorrect")
    private String password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "statistic_id", referencedColumnName ="id")
    @ToString.Exclude
    @JsonIgnore
    private Statistic statistic;

    @ToString.Exclude
    @Transient
    private String confirmPassword;

    public User() {
        statistic = new Statistic();
    }
}
