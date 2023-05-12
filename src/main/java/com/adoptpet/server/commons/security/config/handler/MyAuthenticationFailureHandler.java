package com.adoptpet.server.commons.security.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException exception) throws IOException, ServletException {
        String errorMsg = null;

        if(exception instanceof BadCredentialsException){
            errorMsg = "아이디나 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
        }else if(exception instanceof DisabledException) {
            errorMsg = "계정이 비활성화 되었습니다. 관리자에게 문의하세요.";
        }else if(exception instanceof LockedException){
            log.info("이메일이 인증되지 않았습니다. 이메일을 확인해 주세요.");
            errorMsg = "이메일이 인증되지 않았습니다. 이메일을 확인해 주세요.";
        }else{
            errorMsg = "알수없는 이유로 로그인에 실패하였습니다.";
        }
        req.setAttribute("errorMsg", errorMsg);
        req.getRequestDispatcher("/user/loginFail").forward(req, res);
    }

}
