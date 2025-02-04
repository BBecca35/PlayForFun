import React from 'react';
import "../register/Register.css";

import user_icon from "../Assets/person.png";
import password_icon from "../Assets/password.png";
import email_icon from "../Assets/email.png";

import { useNavigate } from 'react-router-dom';

import axiosInstance from '../../api/axios';
import { useState } from "react";


const USER_REGEX = /^[A-z][A-z0-9-_]{3,23}$/;
const PWD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,24}$/;
const REGISTER_URL = '/user-api/register';


export default function Register() {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [email, setEmail] = useState('');
    const [birthdate, setBirthdate] = useState('');
    const [rulesAccepted, setRulesAccepted] = useState(false);
    const [activeTooltip, setActiveTooltip] = useState(null);
    const [wrongUsername, setWrongUsername] = useState(false);
    const [wrongPassword, setWrongPassword] = useState(false);
    const [twoPasswordnotequals, setTwoPasswordnotequals] = useState(false);
    const navigate = useNavigate();


    const handleUsernameChange = (e) => {
        setUsername(e.target.value);
    
        if (e.target.value.length > 0 && (e.target.value.length < 4 || (!USER_REGEX.test(e.target.value)))) {
            setActiveTooltip('username');
            setWrongUsername(true);
        } else {
            setActiveTooltip(null);
            setWrongUsername(false);
        }
    };
    
    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    
        if (e.target.value.length > 0 && (e.target.value.length < 8 || (!PWD_REGEX.test(e.target.value)))) {
            setActiveTooltip('password');
            setWrongPassword(true);
        } else {
            setActiveTooltip(null);
            setWrongPassword(false);
        }
    };

    const handleConfirmPasswordChange = (e) => {
        setConfirmPassword(e.target.value);
    
        if (e.target.value.length > 0 && (e.target.value !== password)) {
            setActiveTooltip('confirmPassword');
            setTwoPasswordnotequals(true);
            
        } else {
            setActiveTooltip(null);
            setTwoPasswordnotequals(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!username.trim() || !password.trim() || !email.trim() || !birthdate.trim()) {
            alert("Az összes mezőt kötelező kitölteni!");
            return;
        }

        if(wrongUsername){
            alert("A felhasználónév mező helytelenül lett kitöltve!");
            return;
        }

        if(wrongPassword){
            alert("A jelszó mező helytelenül lett kitöltve!");
            return;
        }

        if(twoPasswordnotequals){
            alert("A két jelszónak meg kell egyeznie!");
            return;
        }

        if (!rulesAccepted) {
            alert("El kell fogadnod a szabályzatokat!");
            return;
        }

        try {
            const response = await axiosInstance.post(REGISTER_URL, 
                JSON.stringify({username, password, email, birthdate}),
                {
                    headers: { 'Content-Type': 'application/json' },
                    withCredentials: true
                }
            );
    
            console.log(response.data);
            navigate('/'); 
            alert("Sikeres regisztráció!");
             

    
        } catch (error) {
            if(error.response){
                const { status, data } = error.response;
                console.error(`Hiba történt: ${status} - ${data.error}`);
                if (status === 404 && data.error === "User not found") {
                    alert("Nem található a felhasználó!");
                } else if (status === 409 && data.error === "Username already exist!") {
                    alert("Ez a felhasználónév már létezik!");
                } else if (status === 409 && data.error === "Email address already exist!") {
                    alert("Ezzel az email címmel már regisztráltak!");    
                } else {
                    alert(`Ismeretlen hiba: ${status} - ${data.error}`);
                }
            }
            console.error("Hiba történt: ", error.message);
        }
    };

    const currentYear = new Date().getFullYear();
    const maxDate = `${currentYear}-12-31`; 


return (
    <form className='register-form' onSubmit={handleSubmit}>
        <div className='register-container'>
            <div className='register-title'>Regisztráció</div>
            <div className='register-inputs'>
                <div className='username-password-group'>
                    <div className='username'>
                        <img src={user_icon} alt='' className='input-icon' />
                        <input type='text'
                            placeholder='Felhasználónév'
                            value={username}
                            onChange={handleUsernameChange}
                        />
                    </div>
                    
                    <div className='password'>
                        <img src={password_icon} alt='' className='input-icon' />
                        <input type='password'
                               placeholder='Jelszó'
                               value={password}
                               onChange={handlePasswordChange}
                        />
                    </div>
                </div>

                <div className='username-password-wrapper'>
                    {activeTooltip === 'username' && (
                        <div className="username-tooltip">
                            A felhasználónévnek legalább 4 karakter hosszúnak kell lennie, és csak betűket, számokat, kötőjelet vagy aláhúzást tartalmazhat.
                        </div>
                    )}

                    {activeTooltip === 'password' && (
                        <div className="password-tooltip">
                            A jelszónak legalább 8 karakter hosszúnak kell lennie. Továbbá tartalmaznia kell nagybetűtek és kisbetűket, egy számot és egy speciális karaktert, amik a következők lehetnek: !, @, #, $, %.
                        </div>
                    )}
                </div>

                <div className='email-password-again-group'>
                    <div className='email'>
                        <img src={email_icon} alt='' className='input-icon' />
                        <input type='text'
                            placeholder='Email cím'
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    </div>

                    <div className='password-again'>
                        <img src={password_icon} alt='' className='input-icon' />
                        <input type='password'
                            placeholder='Jelszó ismét'
                            value={confirmPassword}
                            onChange={handleConfirmPasswordChange}
                        />
                    </div>
                </div>

                <div className='email-password-again-wrapper'>
                    {activeTooltip === 'confirmPassword' && (
                        <div className="password-again-tooltip">
                            A két jelszónak meg kell egyeznie!
                        </div>
                    )}
                </div>

                <div className="date-checkbox-group">            
                    <div className='date-input-container'>
                        <label for='date' name='birthdate'>Születési dátum:</label>
                        <input type='date'
                            id='date'
                            name='date' 
                            max={maxDate}
                            min='1950-01-01'
                            value={birthdate}
                            onChange={(e) => setBirthdate(e.target.value)}
                        />
                    </div>
                    <div className='checkbox-container'>
                        <label for="web-rules" name="label-checkbox">Elfogadom a weboldal szabályzatát.</label>
                        <input type="checkbox"
                            id="web-rules"
                            name="web-rules"
                            checked={rulesAccepted}
                            onChange={(e) => setRulesAccepted(e.target.checked)}
                        />
                        <label for="web-rules" className="custom-checkbox"></label>
                    </div>
                </div>

                <div className='register-submit-container'>
                    <button type='submit' className='register-button'>Regisztrálás</button>
                </div>

            </div>
        </div>
    </form>
);
}