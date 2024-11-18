package hu.nye.home.controller;

import hu.nye.home.dto.UserDto;
import hu.nye.home.entity.UserModel;
import hu.nye.home.repository.UserRepository;
import hu.nye.home.service.Interfaces.UserServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    
    private final UserServiceInterface userService;
    
    @Autowired
    public UserController(UserServiceInterface userService) {
        this.userService = userService;
    }
    
    
    @PostMapping("/register")
    public UserModel addNewUser(@RequestBody UserDto userDto){
        return userService.saveUser(userDto);
    }
    
    @GetMapping("/user/{id}")
    public UserModel getUserById(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }
    
    @PutMapping("/user/{id}")
    public UserModel updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserDto dto) {
        return userService.updateUser(id, dto);
    }
    
    @DeleteMapping("/games/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }
}
