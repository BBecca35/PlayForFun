import "../login/Login.css";
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from "jwt-decode";
import axiosInstance from "../../api/axiosInstance";
import useInput from "../../hooks/useInput";
import useAuth from '../../hooks/useAuth';
import user_icon from "../Assets/person.png";
import password_icon from "../Assets/password.png";

const LOGIN_URL = "/api/auth/login"

export default function Login() {

    const [user, resetUser, userAttribs] = useInput('user', '')
    const [ password, setPassword ] = useState('');
    const { setAuth, persist, setPersist } = useAuth();
    const navigate = useNavigate();

    const calculateExpirationDate = (creationDate, durationInSeconds) => {
        const createdAt = new Date(creationDate);
        const createdAtSeconds = Math.floor(createdAt.getTime() / 1000);
        const duration = Number(durationInSeconds);
        const expirationSeconds = createdAtSeconds + duration;
        return new Date(expirationSeconds * 1000);
    };
   
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!user.trim() || !password.trim()) {
            alert("A felhasználónév vagy a jelszó mező üres!");
            return;
        }

        try {

            const response = await axiosInstance.post(LOGIN_URL, 
                JSON.stringify({ username: user, password: password, checkedRememberMe: persist }),
                {
                    headers: { 'Content-Type': 'application/json' },
                    withCredentials: true
                }
            );

            const accessToken = response.headers['authorization'];
            sessionStorage.setItem('accessToken', accessToken);
            const decodedToken = jwtDecode(accessToken);
            const userId = decodedToken.userId;
            const role = decodedToken.role;
            
            setAuth({ user, userId, roles:[role], accessToken });
            resetUser();
            setPassword('');
            navigate("/home");
            
    
        } catch (error) {
            if(error.response){
                const { status, data } = error.response;
                console.error(`Hiba történt: ${status} - ${data.error}`);
                if (status === 400) {
                    alert("Nem található a felhasználó!");
                } else if (status === 403 && data.error !== "The user is banned.") {
                    alert("Hibás felhasználónév vagy jelszó!");
                } else if(status === 403 && data.error === "The user is banned.") {
                    alert(`Ki lettél tiltva!\n\nEddig: ${data.banExpiration === "-1" ? 
                        "Végleges" :
                        (calculateExpirationDate(data.bannedAt, data.banExpiration)).toLocaleString()
                    } \nIndoka: ${data.reason}` 
                );
                }else {
                    alert(`Ismeretlen hiba: ${status} - ${data.error}`);
                }
            }
            console.error("Hiba történt: ", error.message);
        }
    };
    
    const togglePersist = () => {
        setPersist(prev => !prev);
    }

    useEffect(() => {
        localStorage.setItem("persist", persist);
    }, [persist])


    return (
        <form className='login-container' onSubmit={handleSubmit}>
            <div className='login-title'>Bejelentkezés</div>
                <div className='inputs'>

                    <div className='input'>
                        <img src={user_icon} alt='' />
                        <input type='text' 
                               name='username' 
                               placeholder='Felhasználónév'
                               autoComplete='off'
                               value={user}
                               {...userAttribs}
                        />
                    </div>
                    <div className='input'>
                        <img src={password_icon} alt='' />
                        <input type='password' 
                               name='password' 
                               placeholder='Jelszó'
                               value={password}
                               onChange={(e) => setPassword(e.target.value)} 
                        />
                    </div>
                </div>
                
                <div className='checkbox-submit-container'>
                    <div>
                        <input type="checkbox" 
                               id="remember" 
                               checked={persist} 
                               onChange={togglePersist}
                        />
                        <label htmlFor="remember" className="custom-checkbox">Emlékezz rám</label>
                    </div>

                    <div className='login-submit-button-container'>
                        <button type='submit' className='login-submit-button'>Belépés</button>
                    </div>
                </div>

        </form>
    );
}

