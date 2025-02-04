package hu.nye.home.specifications;

import hu.nye.home.entity.RoleModel;
import hu.nye.home.entity.UserModel;
import hu.nye.home.repository.RoleRepository;
import hu.nye.home.repository.UserRepository;
import hu.nye.home.security.SecurityConstants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class RoleAndUserInitializer implements CommandLineRunner {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public RoleAndUserInitializer(RoleRepository roleRepository,
                                  UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            RoleModel admin = new RoleModel();
            admin.setName("ADMIN");
            roleRepository.save(admin);
        }
        
        if (roleRepository.findByName("USER").isEmpty()) {
            RoleModel user = new RoleModel();
            user.setName("USER");
            roleRepository.save(user);
        }
        
        if ((userRepository.findByEmail(AdminUserConstants.EMAIL) == null) &&
              (userRepository.findByUsername(AdminUserConstants.USERNAME).isEmpty())){
            UserModel user = new UserModel();
            user.setUsername(AdminUserConstants.USERNAME);
            user.setEmail(AdminUserConstants.EMAIL);
            RoleModel roles = roleRepository.findByName("ADMIN")
                                .orElseThrow(() ->
                                               new IllegalStateException("Role 'ADMIN' " +
                                                                           "not found in the database!"));
            roles = entityManager.merge(roles);
            user.setRoles(Collections.singletonList(roles));
            user.setPassword(passwordEncoder.encode(AdminUserConstants.PASSWORD));
            user.setBirthDate(AdminUserConstants.BIRTHDATE);
            userRepository.save(user);
        }
    }
}
