package hu.nye.home.service.Classes;

import hu.nye.home.dto.*;
import hu.nye.home.entity.BanModel;
import hu.nye.home.entity.UserModel;
import hu.nye.home.exception.*;
import hu.nye.home.repository.UserRepository;
import hu.nye.home.security.CustomUserDetailsService;
import hu.nye.home.service.Interfaces.UserServiceInterface;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import static hu.nye.home.authorization.Role.*;

@Service
public class UserService implements UserServiceInterface {
    
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final GameDescriptionService gameDescriptionService;
    
    @Autowired
    public UserService(UserRepository userRepository,
                       CustomUserDetailsService userDetailsService,
                       PasswordEncoder passwordEncoder, GameDescriptionService gameDescriptionService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.gameDescriptionService = gameDescriptionService;
    }
    
    @Override
    @SneakyThrows
    public UserResponse getUserById(Long id) {
        UserModel user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return convertToDto(user);
    }
    
    @Override
    @SneakyThrows
    public UserResponse getUserByUsername(String username) {
        UserModel user;
        if(gameDescriptionService.isAdmin()){
            user = userRepository.searchByUsername(username);
            if(user == null){
                throw new UserNotFoundException();
            }
        }else if (gameDescriptionService.isModerator()) {
            user = userRepository.searchUserByUsername(username);
            if(user == null){
                throw new UserNotFoundException();
            }
        }else{
            throw new UnauthorizedActionException();
        }
        return convertToDto(user);
    }
    
    private UserModel convertToEntity(RegisterUser dto) {
        UserModel user = new UserModel();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBirthDate(dto.getBirthDate());
        user.setRole(USER);
        return user;
    }
    
    private UserResponse convertToDto(UserModel user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setBirthDate(user.getBirthDate());
        dto.setRole(user.getRole().name());
        
        boolean isUserBanned = false;
        
        for(BanModel banModel : user.getBans()){
            if(!banModel.isExpired()){
                isUserBanned = true;
                break;
            }
        }
        
        dto.setBanned(isUserBanned);
        return dto;
    }
    
    @Override
    @SneakyThrows
    public UserModel registerUser(RegisterUser dto) {
        if (dto == null) {
            throw new NullPointerException("RegisterUser cannot be null!");
        }
        
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UsernameIsExistException();
        }
        
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailIsExistException();
        }
        
        
        UserModel user = convertToEntity(dto);
        return userRepository.save(user);
    }
    
    @Override
    @SneakyThrows
    public UserModel updateUser(Long id, RegisterUser dto) {
        UserModel user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if(userRepository.existsByEmail(dto.getEmail())){
            throw new EmailIsExistException();
        }
        if(userRepository.existsByUsername(dto.getUsername())){
            throw new UsernameIsExistException();
        }
        
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
        }else if(userRepository.existsByEmail(dto.getNewEmail())){
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
    public UserPaginationResponse getAllUser(int pageNumber, int pageSize, String sortBy,
                                             String sortDirection, String filterByRole) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<UserModel> users;
        
        if (filterByRole.equalsIgnoreCase("user")) {
            users = userRepository.findByRole(USER, pageable);
        } else if (filterByRole.equalsIgnoreCase("moderator")) {
            users = userRepository.findByRole(MODERATOR, pageable);
        } else {
            users = userRepository.findByRoleIn(List.of(USER, MODERATOR), pageable);
        }
        
        List<UserResponse> userResponses = users.getContent()
                                             .stream()
                                             .map(this::convertToDto)
                                             .toList();
        
        UserPaginationResponse response = new UserPaginationResponse();
        response.setUsers(userResponses);
        response.setPageNumber(users.getNumber());
        response.setPageSize(users.getSize());
        response.setTotalElements(users.getTotalElements());
        response.setTotalPages(users.getTotalPages());
        response.setLast(users.isLast());
        
        return response;
        
    }
    
    @Override
    @SneakyThrows
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }
    
    @Override
    @SneakyThrows
    public void promoteToModerator(Long id) {
        UserModel user = userRepository.findById(id)
                           .orElseThrow(UserNotFoundException::new);
        if(user.getRole().equals(MODERATOR)){
            throw new AlreadyModeratorException();
        }else if(user.getRole().equals(ADMIN)){
            throw new CannotPromoteAdminException();
        }else{
            user.setRole(MODERATOR);
        }
        userRepository.save(user);
    }
    
    @Override
    @SneakyThrows
    public void demoteToUser(Long id) {
        UserModel user = userRepository.findById(id)
                           .orElseThrow(UserNotFoundException::new);
        if(user.getRole().equals(USER)){
            throw new AlreadyUserException();
        }else if(user.getRole().equals(ADMIN)){
            throw new CannotDemoteAdminException();
        }else{
            user.setRole(USER);
        }
        userRepository.save(user);
    }
}
