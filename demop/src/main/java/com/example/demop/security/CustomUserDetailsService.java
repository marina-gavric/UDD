package com.example.demop.security;
import com.example.demop.model.User;
import com.example.demop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findOneByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Couldn't find user with username '%s'.", username));
        } else {
            return user;
        }
    }

}
