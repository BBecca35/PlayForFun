package hu.nye.home.exception;

import hu.nye.home.entity.BanModel;
import lombok.Getter;

@Getter
public class BannedUserException extends Exception {
    private final BanModel ban;
    
    public BannedUserException(BanModel ban) {
        super("User is banned");
        this.ban = ban;
    }
    
}
