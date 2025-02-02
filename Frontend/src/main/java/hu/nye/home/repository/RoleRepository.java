package hu.nye.home.repository;

import hu.nye.home.entity.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {

    Optional<RoleModel> findByName(String name);
}
