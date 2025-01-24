package hu.nye.home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
* Ez az alkalmazás indítója.
* */
@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
    
  }
}

/*
  2025.01.06: A felhasználóhoz tartozó CRUD endpointok működnek.
  A GameDescription Entity módosítva lesz oly módon,
  hogy a GameDescription rész és az Image rész össze lesz olvaszva.
 */
