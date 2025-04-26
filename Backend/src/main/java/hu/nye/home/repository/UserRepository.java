package hu.nye.home.repository;

import hu.nye.home.authorization.Role;
import hu.nye.home.entity.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    
    @Query("SELECT u FROM UserModel u WHERE u.username = :username AND u.role = 'USER'")
    UserModel searchUserByUsername(@Param("username") String username);
    @Query("SELECT u FROM UserModel u WHERE u.username = :username AND (u.role = 'USER' OR u.role = 'MODERATOR')")
    UserModel searchByUsername(@Param("username") String username);
    UserModel findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Page<UserModel> findByRole(Role role, Pageable pageable);
    Page<UserModel> findByRoleIn(List<Role> roles, Pageable pageable);
    
}
