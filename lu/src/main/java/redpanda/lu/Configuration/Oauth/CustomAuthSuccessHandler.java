package redpanda.lu.Configuration.Oauth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import redpanda.lu.Entity.Role;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {


    // RequestCache =  HttpSession 에 SavedRequest를 저장한다.
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final String DEFAULT_LOGIN_SUCCESS_URL = "/thread/korean";


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        clearAuthenticationAttributes(request);

        // 리다이렉트 페이지
        redirectStartegy(request,response,authentication);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        //getSession이 false인 상황에서, 세션이 존재하지 않다면 세션 생성 안하고 null반환. (존재하면 세션반환0
        HttpSession session = request.getSession(false);
        if (session != null){
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }


    private void redirectStartegy(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        //SavedRequest 를 가져옴
        SavedRequest savedRequest = requestCache.getRequest(request,response);

        if(savedRequest == null){
            redirectStrategy.sendRedirect(request,response,DEFAULT_LOGIN_SUCCESS_URL);
        }

        else{
            // authorities 가져오기.
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

            if(roles.contains(Role.GUEST.getKey())){
                redirectStrategy.sendRedirect(request,response,"/thread/korean");

            }else{
                redirectStrategy.sendRedirect(request,response,"/thread/korean");
            }
        }

    }



}
