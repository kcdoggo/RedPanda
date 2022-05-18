package redpanda.lu.Configuration.Oauth;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;
import redpanda.lu.Entity.Member;
import redpanda.lu.Entity.Role;

import java.util.Map;

@Getter
public class OAuthAttributes {


    private Map<String, Object> attributes; // OAuth2 반환하는 유저 정보 Map
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    // 해당 로그인인 서비스가 kakao인지 google인지 구분하여, 알맞게 매핑.
    // 여기서 registrationId는 서비스 명("google","kakao","naver"..)
    // userNameAttributeName은 해당 서비스의 map의 키값이  {google="sub", kakao="id", naver="response"}

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        // 여기서 네이버와 카카오 등 구분 (ofNaver, ofKakao)

        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    // JPA에서 save해주기 위해 Member 엔티티 생성..
    // 가입할때 기본권한은 GUEST 로.
    public Member toEntity(){
        return Member.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST) // 기본 권한 GUEST
                .build();
    }

}