package miracle.field.server.model;

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
    private Long id;

    @Column(columnDefinition = "BIGINT default 0")
    private Long score;

    @Column(name = "game_total", columnDefinition = "BIGINT default 0")
    private Long gameTotal;

    @Column(columnDefinition = "BIGINT default 0")
    private Long wins;

    @Column(columnDefinition = "BIGINT default 0")
    private Long loses;

    @OneToOne(mappedBy = "statistic", fetch = FetchType.LAZY)
    private User user;

}
