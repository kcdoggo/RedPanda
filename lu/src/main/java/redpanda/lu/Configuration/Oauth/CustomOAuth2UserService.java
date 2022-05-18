package redpanda.lu.Configuration.Oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import redpanda.lu.Entity.Member;
import redpanda.lu.Repository.MemberRepository;

import javax.servlet.http.HttpSession;
import java.util.Collections;


/*
    이 클래스는  시큐리티 설정에서 소셜로그인 성공후, 후속조치 할 클래스 지정해준거임.
     .userService(customOAuth2UserService);
     여기서, 구글 로그인 후, 가져온 사용자 정보를 토대로 가입,정보수정,세션 저장 할거.
 */

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;





 //유저 정보 가져오기
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {


        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth2 서비스 id (구글, 카카오, 네이버) 구분.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();


        // OAuth2 로그인 진행 시 키가 되는 필드 값(PK), 각 계정마다 유니크한 id값 전달 .
        //구글은 sub가 유니크 필드, 네이버는 id가 유니크 필드.
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 위에 있는 OAuth2UserService를 통해 가져온 oAuth2User의 attributes를 담음.
        // registrationId( 구글,카카오), userNameAttri(유니크 아이디값, 구글의sub,네이버의 id 등)
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());


        Member member = saveOrUpdate(attributes);


        //세션에 사용자 정보 저장 DTO.
        // 로그인 성공시, 세션에 SessionUser객체 저장.
        httpSession.setAttribute("member", new SessionUser(member));

        return new DefaultOAuth2User(

                //싱글톤으로 단 하나의 롤만 저장 가능..
                Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 유저 생성 및 수정 서비스 로직
    private Member saveOrUpdate(OAuthAttributes attributes){

        Member member = memberRepository.findByEmail(attributes.getEmail())

                //찾은 Member엔티티 클래스에 정의된 업데이트 메소드로 수정.
                .map(entity -> entity.modifyMemberInfo(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        return memberRepository.save(member);
    }


    }