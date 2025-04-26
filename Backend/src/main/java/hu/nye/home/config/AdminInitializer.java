package hu.nye.home.config;

import hu.nye.home.entity.UserModel;
import hu.nye.home.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static hu.nye.home.authorization.Role.ADMIN;

@Component
public class AdminInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public AdminInitializer(UserRepository userRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        if (!(userRepository.existsByEmail(AdminUserConstants.EMAIL)) &&
              !(userRepository.existsByUsername(AdminUserConstants.USERNAME))){
            UserModel user = new UserModel();
            user.setUsername(AdminUserConstants.USERNAME);
            user.setEmail(AdminUserConstants.EMAIL);
            user.setRole(ADMIN);
            user.setPassword(passwordEncoder.encode(AdminUserConstants.PASSWORD));
            user.setBirthDate(AdminUserConstants.BIRTHDATE);
            userRepository.save(user);
        }
    }
}
