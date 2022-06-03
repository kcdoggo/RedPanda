package redpanda.lu.Configuration.Oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import redpanda.lu.Entity.Role;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()// URL별 권한 권리
                .antMatchers("/","/css/**","/ckeditor/**").permitAll()
                .antMatchers("/thread/korean","/thread/main","/thread/getpost/**","/thread/delete-comment","/thread/customLogin","/thread/language-partner","/thread/sendMessages","/thread/search/**","/thread/koreanNews").permitAll()
                .antMatchers("thread/post-k-questions").hasRole(Role.USER.name())
                .antMatchers("thread/post").hasRole(Role.USER.name())
                .anyRequest().authenticated() // anyRequest : 설정된 값들 이외 나머지 URL 나타냄, authenticated : 인증된 사용자
                .and()
                .logout()
                .logoutSuccessUrl("/thread/language-partner")
                .and()
                .oauth2Login()
                .successHandler(successHandler())
                .userInfoEndpoint() // oauth2 로그인 성공 후 가져올 때의 설정들
                // 소셜로그인 성공 시 후속 조치를 진행할 UserService 인터페이스 구현체 등록
                .userService(customOAuth2UserService); // 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시

    }

    //로그인 성공한 후, 처리해줄 핸들러 (redirect페이지 등 설정 셋 ).
    @Bean
    public AuthenticationSuccessHandler successHandler(){

        return new CustomAuthSuccessHandler();
    }


}

/**
 *
 *
 <article th:each=" item : ${reply}" class="uk-comment uk-comment-primary reply_items">

 <div class="comment_flex">
 <header class="uk-comment-header">
 <div >
 <div>
 <div class="profile_name">
 <img class="uk-comment-avatar" src="/css/user.jpg" width="80" height="80" alt="">
 <h4 class="uk-comment-title uk-margin-remove"><a class="uk-link-reset" href="#">[[${item.replyer}]]</a></h4>
 </div>
 </div>
 <div class="uk-width-expand">
 <ul class="uk-comment-meta uk-subnav uk-subnav-divider uk-margin-remove-top">

 </ul>
 </div>
 </div>
 </header>

 <div class="uk-comment-body">
 <p style="white-space:pre;"> [[${item.replyContent}]]</p>
 <li><a href="#" th:text="${#temporals.format(item.regDate, 'yyyy-MM-dd')}">
 </a></li>
 <span  class="commentDelete" uk-toggle="target: #my-id" type="button" uk-icon="trash"></span>


 </div>

 </div>

 <div>



 </div>
 </article>
 *
 *
 *
 *
 */