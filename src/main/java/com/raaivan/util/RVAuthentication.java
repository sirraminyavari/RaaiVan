package com.raaivan.util;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
@ApplicationScope
public class RVAuthentication {
    AuthenticationManager authenticationManager;

    public RVAuthentication(){
        this.authenticationManager = new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                User details = (User)authentication.getDetails();
                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                grantedAuthorities.add(new SimpleGrantedAuthority("User"));
                Authentication auth = new UsernamePasswordAuthenticationToken(details.getUsername(),
                        details.getPassword(), grantedAuthorities);
                return auth;

                //return null
            }
        };
    }

    public boolean authenticate(String username, String password){
        UUID userId = UUID.randomUUID();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        User details = new User(userId.toString(), password, authorities);
        token.setDetails(details);

        try{
            Authentication auth = authenticationManager.authenticate(token);

            if(auth == null) return false;
            else {
                SecurityContextHolder.getContext().setAuthentication(auth);
                return auth.isAuthenticated();
            }
        }
        catch (Exception ex){
            return false;
        }
    }
}
