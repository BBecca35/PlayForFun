package hu.nye.home.controller;

import hu.nye.home.entity.UserModel;
import hu.nye.home.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/register")
    UserModel newUser(@RequestBody UserModel newUser){
        return userRepository.save(newUser);
    }
    @GetMapping("/register")
    List<UserModel> getAllUsers(){
        return userRepository.findAll();
    }
}
