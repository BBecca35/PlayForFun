import React from 'react';
import "../login/Login.css";
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axios';

import user_icon from "../Assets/person.png";
import password_icon from "../Assets/password.png";
const LOGIN_URL = '/api/auth/login'

export default function Login( { onLogin } ) {

    const [ username, setUsername ] = useState('');
    const [ password, setPassword ] = useState('');
    const [ checkbox, setCheckbox ] = useState(false);
    const navigate = useNavigate(); 

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!username.trim() || !password.trim()) {
            alert("A felhasználónév vagy a jelszó mező üres!");
            return;
        }

        try {
            const response = await axiosInstance.post(LOGIN_URL, 
                JSON.stringify({ username, password }),
                {
                    headers: { 'Content-Type': 'application/json' },
                    withCredentials: true
                }
            );

            const { accessToken, refreshToken, tokenType, userId } = response.data;

            //console.log(`Emlékezz rám értéke: ${checkbox}`);
            
            if (checkbox) {
                localStorage.setItem('accessToken', accessToken);
                localStorage.setItem('refreshToken', refreshToken);
                localStorage.setItem('tokenType', tokenType);
                localStorage.setItem('userId', userId);
                localStorage.setItem('rememberMe', 'true');
              } else {
                localStorage.setItem('accessToken', accessToken);
                localStorage.setItem('tokenType', tokenType);
                localStorage.setItem('userId', userId);
                localStorage.setItem('rememberMe', 'false');
              }

            //console.log('LocalStorage Access Token:', localStorage.getItem('accessToken'));
            //console.log('LocalStorage Refresh Token:', localStorage.getItem('refreshToken')); 
            //console.log('LocalStorage user Id:', localStorage.getItem('userId')); 

            onLogin();
            setUsername('');
            setPassword(''); 
            navigate('/home');
            
    
        } catch (error) {
            if(error.response){
                const { status, data } = error.response;
                console.error(`Hiba történt: ${status} - ${data.error}`);
                if (status === 400) {
                    alert("Nem található a felhasználó!");
                } else if (status === 403) {
                    alert("Hibás felhasználónév vagy jelszó!");    
                } else {
                    alert(`Ismeretlen hiba: ${status} - ${data.error}`);
                }
            }
            console.error("Hiba történt: ", error.message);
        }
    };
    
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
                               value={username}
                               onChange={(e) => setUsername(e.target.value)} 
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
                               checked={checkbox} 
                               onChange={(e) => setCheckbox(e.target.checked)}
                        />
                        <label for="remember" className="custom-checkbox">Emlékezz rám</label>
                    </div>

                    <div className='submit-container'>
                        <button type='submit' className='submit'>Belépés</button>
                    </div>
                </div>

        </form>
    );
}

//https://medium.com/@javatechie/how-to-kill-the-process-currently-using-a-port-on-localhost-in-windows-31ccdea2a3ea
