package redpanda.lu.Configuration.Oauth;

import lombok.Getter;
import redpanda.lu.Entity.Member;

import java.io.Serializable;

/**
 * 직렬화 : 객체를 바이트로 변환함.
 * 다른 서버로 보내거나 받을 수 있음.
 */
@Getter
public class SessionUser implements Serializable {

    private String name;
    private String email;
    private String picture;

    public SessionUser(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.picture = member.getPicture();
    }

}