package com.skillforge.backend.config;

import com.skillforge.backend.entity.UserToken;
import com.skillforge.backend.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String header = request.getHeader("Authorization");
        String token=null;
        String username=null;
        if(header==null || !header.startsWith("Bearer ")) {
           return;
        }
        token = header.substring(7);
        Optional<UserToken> userTokenOptional = tokenRepository.findByToken(token);
        if(userTokenOptional.isPresent()) {
            UserToken userToken = userTokenOptional.get();
            userToken.setExpired(true);
            userToken.setRevoked(true);
            tokenRepository.save(userToken);
        }
    }
}
