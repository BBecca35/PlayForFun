package hu.nye.home.controller;

import hu.nye.home.dto.UserDto;
import hu.nye.home.entity.UserModel;
import hu.nye.home.service.Interfaces.UserServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//https://github.com/hello-iftekhar/springJwt/blob/main/src/main/java/com/helloIftekhar/springJwt/service/JwtService.java

@RestController
@RequestMapping("/user-api")
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
    
    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }
}
