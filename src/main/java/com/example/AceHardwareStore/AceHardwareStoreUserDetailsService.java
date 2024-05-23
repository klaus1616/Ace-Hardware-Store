package com.example.AceHardwareStore;

import com.example.AceHardwareStore.daos.UserDao;
import com.example.AceHardwareStore.models.User;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
/**
 * This class is used to load users for the auth system
 */
@Component
public class AceHardwareStoreUserDetailsService implements UserDetailsService {
    /**
     * The DAO used to access user data
     */
    private final UserDao userDao;

    /**
     * Create a new instance of this class
     */
    public AceHardwareStoreUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Load a user by their username
     *
     * @param username The username to load
     * @return The user details as a JwtUser
     * @throws UsernameNotFoundException If the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUser(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        JwtUser jwtUser = new JwtUser();

        jwtUser.setUsername(user.getUsername());
        jwtUser.setPassword(user.getPassword());

        List<String> roles = userDao.getRolesForUser(user.getUsername());
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        jwtUser.setAuthorities(authorities);
        jwtUser.setAccountNonExpired(true);
        jwtUser.setAccountNonLocked(true);
        jwtUser.setApiAccessAllowed(true);
        jwtUser.setCredentialsNonExpired(true);
        jwtUser.setEnabled(true);

        return jwtUser;
    }
}

