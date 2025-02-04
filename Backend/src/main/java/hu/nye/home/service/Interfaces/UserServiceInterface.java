package hu.nye.home.service.Interfaces;

import hu.nye.home.dto.ChangeEmailDto;
import hu.nye.home.dto.ChangePasswordDto;
import hu.nye.home.dto.UserDto;
import hu.nye.home.entity.UserModel;

import java.util.Optional;


/*Amit elleőrizni kell a módosítás előtt:
    Amit átküldünk: user_id, jelenlegi jelszó, új jelszó
    Az új jelszót duplán kell megadni, ezért meg kell egyeznie a kettőnek - frontend
    Az adott felhasználó megkeresése, ha nincs meg, kivételt dobunk. - backend
    A jelenlegi jelszó megegyezik-e az adatbázisban letárolt jelszóval -backend
    A jelenlegi jelszó megegyezik-e az újjal, ha igen, akkor kivételt dobunk,
    különben mehet a módosítás - backend
    
    Amit átküldünk: user_id, jelenlegi email, új email
    A két email-címnek nem szabad megegyeznie,
    ha megegyezik, akkor figyelmetjük a felhasználót - frontend
    Az adott felhasználó megkeresése, ha nincs meg, kivételt dobunk. - backend
    A jelenlegi email-cím tartozik-e felhasználóhoz, ha nem, akkor kivételt dobunk,
    különben mehet a módosítás- backend
    
*/

public interface UserServiceInterface {
    
    UserModel getUserById(Long id);
    UserModel saveUser(UserDto dto);
    UserModel updateUser(Long id, UserDto dto);
    UserModel updateEmail(ChangeEmailDto dto);
    UserModel updatePassword(ChangePasswordDto dto);
    void deleteUser(Long id);
    
}
