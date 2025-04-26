package hu.nye.home.controller;

import hu.nye.home.dto.*;
import hu.nye.home.entity.UserModel;
import hu.nye.home.service.Interfaces.UserServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-api")
public class UserController {
    
    private final UserServiceInterface userService;
    
    @Autowired
    public UserController(UserServiceInterface userService) {
        this.userService = userService;
    }
    
    @PostMapping("/register")
    public UserModel addNewUser(@RequestBody RegisterUser userDto){
        return userService.registerUser(userDto);
    }
    
    
    @GetMapping("/user/get/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }
    
    @GetMapping("user/get/username/{username}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'moderator:read')")
    public UserResponse getUserByUsername(@PathVariable("username") String username){
        return userService.getUserByUsername(username);
    }
    
    @GetMapping("/user/allUser")
    @PreAuthorize("hasAnyAuthority('admin:read', 'moderator:read')")
    public ResponseEntity<UserPaginationResponse> getAllUser(
      @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
      @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize,
      @RequestParam(value = "sortBy", defaultValue = "username", required = false) String sortBy,
      @RequestParam(value = "sortDirection",defaultValue = "asc", required = false) String sortDirection,
      @RequestParam(value = "filterByRole",defaultValue = "no", required = false) String filterByRole
    ){
        return new ResponseEntity<>
                 (userService.getAllUser(pageNumber, pageSize, sortBy, sortDirection, filterByRole),
                   HttpStatus.OK);
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
    
    @PutMapping("user/promote/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<String> promoteToModerator(@PathVariable("id") Long id){
        userService.promoteToModerator(id);
        return new ResponseEntity<>("Sikeres előléptetés!", HttpStatus.OK);
    }
    
    @PutMapping("user/demote/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<String> demoteToUser(@PathVariable("id") Long id){
        userService.demoteToUser(id);
        return new ResponseEntity<>("Sikeres lefokozás!", HttpStatus.OK);
    }
    
    @DeleteMapping("/user/delete/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }
}
