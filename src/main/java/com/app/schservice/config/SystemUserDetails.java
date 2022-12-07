package com.app.schservice.config;


import com.app.schservice.users.model.User;
import com.app.schservice.users.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SystemUserDetails implements UserDetailsService {
    private UserRepo userRepo;

    public SystemUserDetails(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepo.findByUsernameAndActiveTrue(username);
        if (user==null)
            throw new UsernameNotFoundException("Invalid Username or Password");
        return new UserDetailsImpl(user);
    }
}
