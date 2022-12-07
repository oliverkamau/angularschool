package com.app.schservice.users.service.impl;

import com.app.schservice.users.model.User;
import com.app.schservice.users.repository.UserRepo;
import com.app.schservice.users.service.UserService;
import com.app.schservice.utils.BadRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;

    private BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepo userRepo, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public void saveUsers(User user) {
        if(user.getUserId()==null){
            if(user.getStatus().equals("A")) {
                user.setActive(true);
            }else{
                user.setActive(false);
            }
                user.setPassword(encoder.encode(user.getPassword()));
                userRepo.save(user);

            }
            else {
            User current = userRepo.findByUserId(user.getUserId());
            if(user.getStatus().equals("A")) {
                current.setActive(true);
            }else{
                current.setActive(false);
            }
           current.setEmail(user.getEmail());
            current.setUserAddress(user.getUserAddress());
            current.setUserGender(user.getUserGender());
            current.setUserTeacherRef(user.getUserTeacherRef());
            current.setUserFirstname(user.getUserFirstname());
            current.setUserLastname(user.getUserLastname());
            current.setUserPhone(user.getUserPhone());
            current.setUserSupervisor(user.getUserSupervisor());
            current.setUserType(user.getUserType());
            userRepo.save(current);
            }


    }
}
