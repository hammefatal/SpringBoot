package home.hammefatal.springboot.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Cart {

    @Id
    @Column(name = "cart_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false) // nullable = false: inner join
    private User user;

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
              //  ", user=" + user +
                '}';
    }

}
