package hu.nye.home.controller;

import hu.nye.home.dto.ChangeEmailDto;
import hu.nye.home.dto.ChangePasswordDto;
import hu.nye.home.dto.UserDto;
import hu.nye.home.entity.UserModel;
import hu.nye.home.service.Interfaces.UserServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    
    @PutMapping("/user/changeEmail")
    public ResponseEntity<UserModel> changeEmailAddress(@RequestBody @Valid ChangeEmailDto dto){
        UserModel user = userService.updateEmail(dto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @PutMapping("/user/changePassword")
    public ResponseEntity<UserModel> changePassword(@RequestBody @Valid ChangePasswordDto dto){
        UserModel user = userService.updatePassword(dto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }
}
