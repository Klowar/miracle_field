package miracle.field.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "statistic")
@Data
@NoArgsConstructor
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private User user;

}
