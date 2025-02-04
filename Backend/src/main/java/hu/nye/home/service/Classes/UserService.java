package hu.nye.home.service.Classes;

import hu.nye.home.dto.ChangeEmailDto;
import hu.nye.home.dto.ChangePasswordDto;
import hu.nye.home.dto.UserDto;
import hu.nye.home.entity.RoleModel;
import hu.nye.home.entity.UserModel;
import hu.nye.home.exception.*;
import hu.nye.home.repository.RoleRepository;
import hu.nye.home.repository.UserRepository;
import hu.nye.home.security.CustomUserDetailsService;
import hu.nye.home.service.Interfaces.UserServiceInterface;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface {
    
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserService(UserRepository userRepository, CustomUserDetailsService userDetailsService,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsService.loadUserByUsername(username);
    }
    
    @Override
    @SneakyThrows
    public UserModel getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
    
    private UserModel convertToEntity(UserDto dto, RoleModel roles) {
        UserModel user = new UserModel();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBirthDate(dto.getBirthDate());
        user.setRoles(Collections.singletonList(roles));
        return user;
    }
    
    @Override
    @SneakyThrows
    public UserModel saveUser(UserDto dto) {
        if (dto == null) {
            throw new NullPointerException("UserDto cannot be null!");
        }
        
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UsernameIsExistException();
        }
        
        if (userRepository.findByEmail(dto.getEmail()) != null) {
            throw new EmailIsExistException();
        }
        
        RoleModel roles = roleRepository.findByName("USER")
                            .orElseThrow(() ->
                                           new IllegalStateException("Role 'USER' " +
                                                                       "not found in the database!"));
        
        UserModel user = convertToEntity(dto, roles);
        return userRepository.save(user);
    }
    
    @Override
    @SneakyThrows
    public UserModel updateUser(Long id, UserDto dto) {
        UserModel user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBirthDate(dto.getBirthDate());
        return userRepository.save(user);
    }
    
    @Override
    @SneakyThrows
    public UserModel updateEmail(ChangeEmailDto dto) {
        UserModel user = userRepository.findById(dto.getId())
                           .orElseThrow(UserNotFoundException::new);
        if(!(user.getEmail().equals(dto.getCurrentEmail()))) {
            throw new EmailNotMatchingException();
        }else if(userRepository.findByEmail(dto.getNewEmail()) != null){
            throw new EmailIsExistException();
            
        }else {
            user.setEmail(dto.getNewEmail());
            return userRepository.save(user);
        }
    }
    
    @Override
    @SneakyThrows
    public UserModel updatePassword(ChangePasswordDto dto) {
        UserModel user = userRepository.findById(dto.getId())
                           .orElseThrow(UserNotFoundException::new);
        
        if(!(passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword()))){
            throw new PasswordNotMatchingException();
        }
        
        else if(passwordEncoder.matches(dto.getNewPassword(), user.getPassword())){
            throw new SameAsCurrentPasswordException();
        }
        else {
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            return userRepository.save(user);
        }
        
    }
    
    @Override
    @SneakyThrows
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }
    
}
