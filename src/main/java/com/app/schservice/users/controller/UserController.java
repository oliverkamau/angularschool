package com.app.schservice.users.controller;

import com.alibaba.fastjson.JSONObject;
import com.app.schservice.users.model.User;
import com.app.schservice.users.service.UserService;
import com.app.schservice.utils.BadRequestException;
import com.app.schservice.utils.ResponseData;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/saveUsers")
    public ResponseData saveUsers(@RequestBody JSONObject object) throws IOException, BadRequestException {
        User user = new ModelMapper().map(object,User.class);
        userService.saveUsers(user);
        return new ResponseData("200","User saved sucessfully");
    }
}
