package home.hammefatal.springboot.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

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
