package HaruIt.domain.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "AuthUser")
@Getter
@NoArgsConstructor
@Table(name = "member")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false,unique = true)
    private String password;

    @Column(nullable = false)
    private String nickname;

    public User(String email,String password,String nickname){
        this.email=email;
        this.password=password;
        this.nickname=nickname;
    }
    public void updatePassword(String password) {
        this.password = password;
    }
}
