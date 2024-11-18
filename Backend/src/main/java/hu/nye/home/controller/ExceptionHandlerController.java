package hu.nye.home.controller;

import hu.nye.home.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found in our database!")
    @ExceptionHandler(UserNotFoundException.class)
    public void userNotFoundException(){
    
    }
    
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "This username is already exist!")
    @ExceptionHandler(UsernameIsExistException.class)
    public void usernameIsExistException(){
    
    }
    
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Have already been registered with this email address!")
    @ExceptionHandler(EmailIsExistException.class)
    public void emailIsExistException(){
    
    }
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Game Description not found in our database!")
    @ExceptionHandler(GameDescriptionNotFoundException.class)
    public void GameDescriptionFoundException(){
    
    }
    
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "This description is already exist!")
    @ExceptionHandler(GameDescriptionIsExistException.class)
    public void gameDescriptionIsExistException(){
    
    }
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Comment not found in our database!")
    @ExceptionHandler(CommentNotFoundException.class)
    public void commentNotFoundException(){
    
    }
    
}
