package hu.nye.home.service.Interfaces;

import hu.nye.home.dto.*;
import hu.nye.home.entity.UserModel;

import java.util.List;

public interface UserServiceInterface {
    
    UserResponse getUserById(Long id);
    UserResponse getUserByUsername(String username);
    UserModel registerUser(RegisterUser dto);
    UserModel updateUser(Long id, RegisterUser dto);
    UserModel updateEmail(ChangeEmailDto dto);
    UserModel updatePassword(ChangePasswordDto dto);
    UserPaginationResponse getAllUser(int pageNumber, int pageSize, String sortBy,
                                      String sortDirection, String filterByRole);
    void deleteUser(Long id);
    void promoteToModerator(Long id);
    void demoteToUser(Long id);
    
}
