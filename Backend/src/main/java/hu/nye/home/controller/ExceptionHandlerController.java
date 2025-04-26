package hu.nye.home.controller;

import hu.nye.home.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerController {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> userNotFoundException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "404");
        error.put("error", "User not found");
        error.put("message", "User not found in our database!");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(UsernameIsExistException.class)
    public ResponseEntity<Map<String, String>> usernameIsExistException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "409");
        error.put("error", "Username already exist!");
        error.put("message", "This username is already exist!");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(EmailIsExistException.class)
    public ResponseEntity<Map<String, String>> emailIsExistException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "409");
        error.put("error", "Email address already exist!");
        error.put("message", "Have already been registered with this email address!");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(GameDescriptionNotFoundException.class)
    public ResponseEntity<Map<String, String>> gameDescriptionFoundException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "404");
        error.put("error", "Game Description not found!");
        error.put("message", "Game Description not found in our database!");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(GameDescriptionIsExistException.class)
    public ResponseEntity<Map<String, String>> gameDescriptionIsExistException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "409");
        error.put("error", "Game Description already exist!");
        error.put("message", "This description is already exist!");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Map<String, String>> commentNotFoundException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "404");
        error.put("error", "Comment not found!");
        error.put("message", "Comment not found in our database!");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(EmailNotMatchingException.class)
    public ResponseEntity<Map<String, String>> emailNotFoundException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "401");
        error.put("error", "Current email not matching!");
        error.put("message", "The provided current email address does not match the user's email address!");
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(PasswordNotMatchingException.class)
    public ResponseEntity<Map<String, String>> passwordNotFoundException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "401");
        error.put("error", "Current Password not matching!");
        error.put("message", "The provided current password does not match the user's password!");
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(SameAsCurrentPasswordException.class)
    public ResponseEntity<Map<String, String>> sameAsCurrentPasswordException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "409");
        error.put("error", "Same as current password!");
        error.put("message", "The new password cannot be the same as the current password!");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<Map<String, String>> unauthorizedActionException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "403");
        error.put("error", "Access Denied!");
        error.put("message", "You can't act on behalf of another user!");
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(TokenIsExpiredException.class)
    public ResponseEntity<Map<String, String>> tokenIsExpiredException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "401");
        error.put("error", "Token is expired!");
        error.put("message", "Token is expired!");
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(ActiveBanNotFoundException.class)
    public ResponseEntity<Map<String, String>> activeBanNotFoundException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "404");
        error.put("error", "Active ban not found!");
        error.put("message", "Theres no active ban!");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(BanNotFoundException.class)
    public ResponseEntity<Map<String, String>> banNotFoundException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "404");
        error.put("error", "Ban not found!");
        error.put("message", "Ban not found in our database!");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(BannedUserException.class)
    public ResponseEntity<Map<String, String>> bannedUserException(BannedUserException ex){
        Map<String, String> error = new HashMap<>();
        error.put("status", "403");
        error.put("error", "The user is banned.");
        error.put("message", "Your account is banned.");
        error.put("banExpiration", String.valueOf(ex.getBan().getBanExpiration()));
        error.put("bannedAt", String.valueOf(ex.getBan().getBannedAt()));
        error.put("reason", ex.getBan().getReason());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(AlreadyModeratorException.class)
    public ResponseEntity<Map<String, String>> alreadyModeratorException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "409");
        error.put("error", "Already Moderator!");
        error.put("message", "This user is already a moderator!");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(AlreadyUserException.class)
    public ResponseEntity<Map<String, String>> alreadyUserException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "409");
        error.put("error", "Already User!");
        error.put("message", "This user is already a user!");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(CannotPromoteAdminException.class)
    public ResponseEntity<Map<String, String>> cannotPromoteAdminException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "403");
        error.put("error", "Cannot Promote Admin!");
        error.put("message", "Users with the admin role cannot be reassigned as moderators!");
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(CannotDemoteAdminException.class)
    public ResponseEntity<Map<String, String>> cannotDemoteAdminException(){
        Map<String, String> error = new HashMap<>();
        error.put("status", "403");
        error.put("error", "Cannot Demote Admin!");
        error.put("message", "Users with the admin role cannot be reassigned as users!");
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
    
}
