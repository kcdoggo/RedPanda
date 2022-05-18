package redpanda.lu.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseTimeEntity {

    @Id
    private String email;

    private String password;

    private String name;

    //profile pic 지정
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public Member modifyMemberInfo(String name,String picture){
        this.name = name;
        this.picture = picture;
        return  this;
    }


    public String getRoleKey(){

        return this.role.getKey();
    }

}
