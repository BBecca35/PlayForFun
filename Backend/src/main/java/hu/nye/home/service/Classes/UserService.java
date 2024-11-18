package hu.nye.home.service.Classes;

import hu.nye.home.dto.UserDto;
import hu.nye.home.entity.UserModel;
import hu.nye.home.exception.EmailIsExistException;
import hu.nye.home.exception.UserNotFoundException;
import hu.nye.home.exception.UsernameIsExistException;
import hu.nye.home.repository.UserRepository;
import hu.nye.home.service.Interfaces.UserServiceInterface;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceInterface {
    
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    
    @Override
    @SneakyThrows
    public UserModel getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
    
    @Override
    @SneakyThrows
    public UserModel saveUser(UserDto dto) {
        UserModel user = new UserModel();
        
        if (dto == null) {
            throw new NullPointerException("Userdto cannot be null!");
        } else {
            String findUsername = userRepository.findUserName(dto.getUsername());
            String findEmail = userRepository.findEmail(dto.getEmail());
            if (findUsername != null) {
                throw new UsernameIsExistException();
            } else if (findEmail != null) {
                throw new EmailIsExistException();
            } else {
                user.setUsername(dto.getUsername());
                user.setEmail(dto.getEmail());
                user.setPassword(dto.getPassword());
                user.setBirthDate(dto.getBirthDate());
                userRepository.save(user);
                return user;
            }
        }
    }
    
    @Override
    @SneakyThrows
    public UserModel updateUser(Long id, UserDto dto) {
        UserModel user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setBirthDate(dto.getBirthDate());
        return userRepository.save(user);
    }
    
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
}
