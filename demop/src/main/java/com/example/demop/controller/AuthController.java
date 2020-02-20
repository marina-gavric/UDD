package com.example.demop.controller;
import com.example.demop.model.*;
import com.example.demop.security.CustomUserDetailsService;
import com.example.demop.security.TokenUtils;
import com.example.demop.security.auth.JwtAuthenticationRequest;
import com.example.demop.services.UserService;
import com.example.demop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Clock;
import java.util.Collection;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "auth")
public class AuthController {
    @Autowired
    public TokenUtils tokenUtils;

    @Autowired
    public AuthenticationManager manager;

    @Autowired
    public CustomUserDetailsService userDetailsService;

    @Autowired
    public UserService userService;

    @RequestMapping(value="/login",method = RequestMethod.POST)
    public ResponseEntity<UserTokenState> loginUser(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response, Device device, HttpServletRequest hr){
      System.out.println("In loginUser method");
        final Authentication authentication = manager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        System.out.println("ULOGOVAN:");
        System.out.println(authentication.getName());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        User user =  (User) authentication.getPrincipal();
        if(user == null) {
            System.out.println("User null");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String jwt = tokenUtils.generateToken(user.getUsername(), device);
        int expiresIn = 3600;

        return ResponseEntity.ok(new UserTokenState(jwt,expiresIn));
    }

    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logOut(HttpServletRequest request, HttpServletResponse response){
        System.out.printf("IN logout");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @RequestMapping(value = "/getUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUser(HttpServletRequest request){
        System.out.println("usao u metodu auth/getUser");
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
//        System.out.println("username u getUser: " + username);
        UserDTO ui = new UserDTO();
        if(username != "" && username != null) {
            System.out.println("Username "+ username);
            User u = (User) userService.findUserByUsername(username);
//
            for(Role role: u.getRoles()){
                if(role.getName().equals("ADMIN")){
                    ui.setRole("ADMIN");
                    System.out.println("admin je");
                    break;
                }else if(role.getName().equals("REVIEWER")){
                    System.out.println("reviewer je");
                    ui.setRole("REVIEWER");
                    break;
                }else if(role.getName().equals("EDITOR")){
                    System.out.println("editor je");
                    ui.setRole("EDITOR");
                    for(Privilege p : role.getPrivileges()){
                        System.out.println("Privilegija "+p.getName());
                    }
                    break;
                }
            }
            ui.setUsername(u.getUsername());
            ui.setName(u.getName());
            ui.setSurname(u.getSurname());
            return new ResponseEntity<UserDTO>(ui, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
