package hu.nye.home.service.Interfaces;

import hu.nye.home.dto.UserDto;
import hu.nye.home.entity.UserModel;


public interface UserServiceInterface {
    
    UserModel getUserById(Long id);
    UserModel saveUser(UserDto dto);
    UserModel updateUser(Long id, UserDto dto);
    void deleteUser(Long id);
    
}
