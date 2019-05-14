package miracle.field.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "statistic_id", referencedColumnName ="id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Statistic statistic;

    @ToString.Exclude
    @Transient
    @EqualsAndHashCode.Exclude
    private String confirmPassword;

    public User() {
        statistic = new Statistic();
    }
}
