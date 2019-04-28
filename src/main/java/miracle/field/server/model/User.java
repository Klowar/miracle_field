package miracle.field.server.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Access(AccessType.FIELD)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
