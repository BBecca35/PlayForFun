package hu.nye.home.controller;

import hu.nye.home.dto.BanDetails;
import hu.nye.home.dto.BanDto;
import hu.nye.home.service.Interfaces.BanServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/moderate-api")
public class BanController {
    
    private final BanServiceInterface banService;
    
    @Autowired
    public BanController(BanServiceInterface banService) {
        this.banService = banService;
    }
    
    @PostMapping("/user/ban")
    @PreAuthorize("hasAnyAuthority('admin:create', 'moderator:create')")
    public ResponseEntity<BanDto> banUser(@RequestBody BanDetails details){
        return new ResponseEntity<>(banService.banUser(details), HttpStatus.OK);
    }
    
    @GetMapping("/ban/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'moderator:read')")
    public ResponseEntity<BanDto> getValidBan(@PathVariable("id") Long id){
        return new ResponseEntity<>(banService.getValidBan(id), HttpStatus.OK);
    }
    
    @PutMapping("/ban/update")
    @PreAuthorize("hasAnyAuthority('admin:update', 'moderator:update')")
    public ResponseEntity<BanDto> updateBanDetails(@RequestBody BanDetails details){
        return new ResponseEntity<>(banService.updateBanDetails(details), HttpStatus.OK);
    }
    
    @PutMapping("/ban/undo/{userId}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'moderator:update')")
    public ResponseEntity<String> undoBanByUser(@PathVariable("userId") Long userId){
        banService.invalidatingBanFromUser(userId);
        return new ResponseEntity<>("Sikeres visszavonás!", HttpStatus.OK);
    }
    
    @PutMapping("/ban/undo/all")
    @PreAuthorize("hasAnyAuthority('admin:update', 'moderator:update')")
    public ResponseEntity<String> undoAllBan(){
        banService.invalidatingAllBan();
        return new ResponseEntity<>("Sikeres visszavonás!",HttpStatus.OK);
    }
    
}
