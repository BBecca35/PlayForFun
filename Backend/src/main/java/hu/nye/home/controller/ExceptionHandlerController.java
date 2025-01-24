package hu.nye.home.controller;

import hu.nye.home.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    public ResponseEntity<Map<String, String>> GameDescriptionFoundException(){
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
    
}
