package miracle.field.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "statistic")
@Data
@NoArgsConstructor
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column
    private Long score = 0L;

    @Column(name = "game_total")
    private Long gameTotal = 0L;

    @Column
    private Long wins = 0L;

    @Column
    private Long loses = 0L;

    @OneToOne(mappedBy = "statistic", fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

}
