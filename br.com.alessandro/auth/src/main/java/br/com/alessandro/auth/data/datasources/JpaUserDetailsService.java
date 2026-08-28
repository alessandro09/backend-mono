package br.com.alessandro.auth.data.datasources;

import br.com.alessandro.auth.data.repositories.AuthUserRepository;
import br.com.alessandro.auth.domain.entities.AuthAuthority;
import br.com.alessandro.auth.domain.entities.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * JPA-backed implementation of {@link UserDetailsService}, replacing the default
 * in-memory user store. Loads {@link AuthUser} entities (and their {@link AuthAuthority}
 * associations) through {@link AuthUserRepository}.
 */
@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<GrantedAuthority> authorities = authUser.getAuthorities().stream()
                .map(AuthAuthority::getAuthority)
                .map(SimpleGrantedAuthority::new)
                .map(GrantedAuthority.class::cast)
                .toList();

        return User.withUsername(authUser.getUsername())
                .password(authUser.getPassword())
                .authorities(authorities)
                .accountExpired(!authUser.isAccountNonExpired())
                .accountLocked(!authUser.isAccountNonLocked())
                .credentialsExpired(!authUser.isCredentialsNonExpired())
                .disabled(!authUser.isEnabled())
                .build();
    }
}
