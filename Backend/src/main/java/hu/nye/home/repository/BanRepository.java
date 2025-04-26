package hu.nye.home.repository;

import hu.nye.home.entity.BanModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BanRepository extends JpaRepository<BanModel, Long> {
    
    @Query(value = """
      select b from BanModel b inner join UserModel u\s
      on b.user.id = u.id\s
      where u.id = :id and (b.isExpired = false)\s
      """)
    BanModel findValidBanByUserId(Long id);
    
    @Query(value = """
      select b from BanModel b\s
      where b.isExpired = false\s
      """)
    List<BanModel> findAllValidBan();
    
    @Query(value = """
      select count(b) > 0 from BanModel b inner join UserModel u\s
      on b.user.id = u.id\s
      where u.id = :userId\s
      """)
    boolean existsByUserid(Long userId);
    
}
