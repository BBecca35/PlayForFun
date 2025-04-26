package hu.nye.home.service.Classes;

import hu.nye.home.dto.BanDetails;
import hu.nye.home.dto.BanDto;
import hu.nye.home.entity.BanModel;
import hu.nye.home.entity.UserModel;
import hu.nye.home.exception.ActiveBanNotFoundException;
import hu.nye.home.exception.BanNotFoundException;
import hu.nye.home.exception.UserNotFoundException;
import hu.nye.home.repository.BanRepository;
import hu.nye.home.repository.UserRepository;
import hu.nye.home.service.Interfaces.BanServiceInterface;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BanService implements BanServiceInterface {
    
    private final BanRepository banRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public BanService(BanRepository banRepository, UserRepository userRepository) {
        this.banRepository = banRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    @SneakyThrows
    public BanDto banUser(BanDetails details) {
        UserModel user = userRepository.findById(details.getUserId()).
                           orElseThrow(UserNotFoundException::new);
        BanModel ban = new BanModel();
        ban.setUser(user);
        ban.setReason(details.getReason());
        if(details.getExpirationTime() == 0){
            ban.setBanExpiration(-1);
        }
        else{
            ban.setBanExpiration(details.getExpirationTime());
        }
        ban.setExpired(false);
        banRepository.save(ban);
        return mapToDto(ban);
        
    }
    
    @Override
    @SneakyThrows
    public BanDto getValidBan(Long userId) {
        if(userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException();
        }
        if(!banRepository.existsByUserid(userId)){
            throw new BanNotFoundException();
        }
        
        BanModel ban = banRepository.findValidBanByUserId(userId);
        if(ban == null){
            throw new ActiveBanNotFoundException();
        }
        return mapToDto(ban);
    }
    
    @Override
    @SneakyThrows
    public BanDto updateBanDetails(BanDetails details) {
        if(userRepository.findById(details.getUserId()).isEmpty()) {
            throw new UserNotFoundException();
        }
        
        if(!banRepository.existsByUserid(details.getUserId())){
            throw new BanNotFoundException();
        }
        
        BanModel ban = banRepository.findValidBanByUserId(details.getUserId());
        if(ban == null){
            throw new ActiveBanNotFoundException();
        }
        ban.setBanExpiration(details.getExpirationTime());
        ban.setReason(details.getReason());
        banRepository.save(ban);
        return mapToDto(ban);
    }
    
    @Override
    @SneakyThrows
    public void invalidatingBanFromUser(Long userId) {
        if(userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException();
        }
        
        if(!banRepository.existsByUserid(userId)){
            throw new BanNotFoundException();
        }
        
        BanModel ban = banRepository.findValidBanByUserId(userId);
        
        if(ban == null){
            throw new ActiveBanNotFoundException();
        }
        ban.setExpired(true);
        ban.setBanExpiration(0);
        banRepository.save(ban);
        mapToDto(ban);
    }
    
    @Override
    @SneakyThrows
    public void invalidatingAllBan() {
        List<BanModel> bans = banRepository.findAllValidBan();
        if(bans.isEmpty()){
            throw new ActiveBanNotFoundException();
        }
        
        for(BanModel ban : bans){
            ban.setExpired(true);
            ban.setBanExpiration(0);
            banRepository.save(ban);
        }
    }
    
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    @SneakyThrows
    public void invalidatingExpiredBan(){
        List<BanModel> bans = banRepository.findAllValidBan();
        if(bans.isEmpty()){
            throw new ActiveBanNotFoundException();
        }
        
        for(BanModel ban : bans){
            if(validateBan(ban.getBannedAt(), ban.getBanExpiration())){
                ban.setExpired(true);
                ban.setBanExpiration(0);
                banRepository.save(ban);
            }
        }
    }
    
    public BanDto mapToDto(BanModel ban){
        BanDto dto = new BanDto();
        dto.setUserId(ban.getUser().getId());
        dto.setReason(ban.getReason());
        dto.setBanExpiration(ban.getBanExpiration());
        dto.setExpired(ban.isExpired());
        dto.setBannedAt(ban.getBannedAt());
        return dto;
    }
    
    @Override
    public boolean validateBan(LocalDateTime date, int expirationTime) {
        if(date == null || expirationTime == 0){
            return false;
        }
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime expireDate = date.plusSeconds(expirationTime);
        return expireDate.isBefore(currentDate);
    }
}
