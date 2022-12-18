package com.app.schservice.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.app.schservice.config.JwtUtil;
import com.app.schservice.config.SystemUserDetails;
import com.app.schservice.users.model.User;
import com.app.schservice.users.model.UserRoles;
import com.app.schservice.users.repository.RolesRepo;
import com.app.schservice.users.repository.UserRepo;
import com.app.schservice.users.repository.UserRolesRepo;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/service")
public class AuthController {

    private JwtUtil jwtUtil;
    private AuthenticationProvider authenticationProvider;
    private SystemUserDetails mySystemUserDetails;
    private UserRepo userRepo;
    private UserRolesRepo userRolesRepo;

    public AuthController(JwtUtil jwtUtil, AuthenticationProvider authenticationProvider, SystemUserDetails mySystemUserDetails, UserRepo userRepo, BCryptPasswordEncoder encoder, RolesRepo rolesRepo, UserRolesRepo userRolesRepo) {
        this.jwtUtil = jwtUtil;
        this.authenticationProvider = authenticationProvider;
        this.mySystemUserDetails = mySystemUserDetails;
        this.userRepo = userRepo;
        this.userRolesRepo = userRolesRepo;
    }

    @PostMapping("/authenticate")
    public JSONObject createAuthenticationToken(@RequestBody JSONObject authenticationRequest) throws Exception {
        try {
            Authentication user =authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getString("username"),authenticationRequest.getString("password")));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        User user=userRepo.findByUsername(authenticationRequest.getString("username"));
        mySystemUserDetails.promptOTP(user);
        JSONObject response = new JSONObject();
        response.put("status","pending");
        response.put("id",user.getUserId());
        return response;





    }
    @PostMapping("/validateotp")
    public JSONObject validateAuthenticationToken(@RequestBody JSONObject jsonObject) throws Exception {
        User user=userRepo.findByUserId(Long.valueOf(jsonObject.getString("userId")));
        mySystemUserDetails.verifyOTP(user,jsonObject.getString("otp"));
        UserDetails userDetails= mySystemUserDetails.loadUserByUsername(user.getUsername());

        String realName=user.getUserFirstname().toUpperCase()+" "+user.getUserLastname().toUpperCase();

        final String jwt=jwtUtil.generateToken(userDetails);
        List<UserRoles> roles = userRolesRepo.findByUser(user);
        Set<String> set = new HashSet<>();
        for(UserRoles r: roles){
            set.add(r.getRoleName());
        }
//        List<String> roles=userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        JSONObject response = new JSONObject();
        response.put("status","verified");
        response.put("roles",set);
        response.put("user",realName);
        response.put("id",user.getUserId());
        response.put("username",user.getUsername());
        response.put("token",jwt);
        return response;
    }
}
