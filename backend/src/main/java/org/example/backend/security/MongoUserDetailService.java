package org.example.backend.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MongoUserDetailService implements UserDetailsService {

    private final UserRepo repo;

    public MongoUserDetailService(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repo.findByUsername(username)
                .orElseThrow(() -> {
                    log.debug("User not found: {}", username);
                    return new UsernameNotFoundException("User not found");
                });

        return new UserPrincipal(user);
    }
}
