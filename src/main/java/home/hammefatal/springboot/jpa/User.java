package home.hammefatal.springboot.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @Column(name = "user_id")
    private Long id;
    private String password;
    private String email;
    private String name;

    @OneToOne(mappedBy = "user")
    private Cart cart;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER) // FetchType.LAZY: 지연로딩, FetchType.EAGER: 즉시로딩
    List<Board> boardList = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", cart=" + cart +
                '}';
    }

}
