import React from 'react'
import "./Settings.css"
import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom';
import password_icon from "../Assets/password.png";
import email_icon from "../Assets/email.png";
import axiosInstance from '../../api/axios';

const PWD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,24}$/;

export default function Settings({ onLogout }) {
    
    /*
        <img name="password-icon" src={password_icon} alt=''></img>
    */

    const[currentPassword, setCurrentPassword] = useState('');
    const[newPassword, setNewPassword] = useState('');
    const[newPasswordAgain, setNewPasswordAgain] = useState('');
    const[currentEmail, setCurrentEmail] = useState('');
    const[newEmail, setNewEmail] = useState('');
    const[activeTooltip, setActiveTooltip] = useState(null);
    const[wrongNewPassword, setWrongNewPassword] = useState(false);
    const navigate = useNavigate();


    const userId = localStorage.getItem('userId');
    const userIdLong = userId ? Number(userId) : null;

    const handleLogout = () => {
        onLogout();
        navigate('/login');

    };

    const handleNewPassword = (e) => {
        setNewPassword(e.target.value);
    
        if (!(PWD_REGEX.test(e.target.value))) {
            setActiveTooltip('newPassword');
            setWrongNewPassword(true);
        } else {
            setActiveTooltip(null);
            setWrongNewPassword(false);
        }
    };

    const handleConfirmPasswordChange = (e) => {
        setNewPasswordAgain(e.target.value);
    
        if (e.target.value.length > 0 && (e.target.value !== newPassword)) {
            setActiveTooltip('newPasswordAgain');
            
        } else {
            setActiveTooltip(null);
        }
    };

    const handleNewEmail = (e) => {
        setNewEmail(e.target.value);
    
        if (e.target.value.length > 0 && (e.target.value === currentEmail)) {
            setActiveTooltip('newEmail');
            
        } else {
            setActiveTooltip(null);
        }
    };

    const handleChangingPassword = async (e) => {
        e.preventDefault();

        if(!currentPassword.trim() || !newPassword.trim() || !newPasswordAgain.trim()){
            alert("A jelszó módosítása során az összes mezőt ki kell tölteni!");
            return;
        }

        if(wrongNewPassword){
            alert("Az új jelszó mező helytelenül lett kitöltve!");
            return;
        }


        if(newPassword !== newPasswordAgain){
            alert("A két jelszó nem egyezik meg");
            return;
        }

        try{
            const CHANGE_PASSWORD_URL = "/user-api/user/changePassword"
            const response = await axiosInstance.put(CHANGE_PASSWORD_URL, 
                JSON.stringify({
                    id: userIdLong, 
                    currentPassword: currentPassword, 
                    newPassword: newPassword}),
                {
                    headers: { 'Content-Type': 'application/json' },
                    withCredentials: true
                }
            );
            setCurrentPassword('');
            setNewPassword('');
            setNewPasswordAgain('');
            handleLogout();
            alert("A jelszó módosult!");


        }catch(error){
            if(error.response){
                const { status, data } = error.response;
                console.error(`Hiba történt: ${status} - ${data.error}`);
                if (status === 404) {
                    alert("Nem található a felhasználó!");
                } else if (status === 401) {
                    alert("A megadott jelenlegi jelszó nem egyezik meg a jelszavaddal!");
                    
                } else if(status === 409) {
                    alert("Az új jelszó nem lehet azonos a jelenlegi jelszóval!")

                }else {
                    alert(`Ismeretlen hiba: ${status} - ${data.error}`);
                }
            }
            console.error("Hiba történt: ", error.message);
        }

    }

    const handleChangingEmail = async (e) => {
        e.preventDefault();

        if(!currentEmail.trim() || !newEmail.trim()){
            alert("Az Email-cím módosítása során az összes mezőt ki kell tölteni!");
            return;
        }

        if(currentEmail === newEmail){
            alert("A két email-cím megegyezik!");
            return;
        }

        try{
            const CHANGE_EMAIL_URL = "/user-api/user/changeEmail"
            const response = await axiosInstance.put(CHANGE_EMAIL_URL, 
                JSON.stringify({
                    id: userIdLong, 
                    currentEmail: currentEmail, 
                    newEmail: newEmail}),
                {
                    headers: { 'Content-Type': 'application/json' },
                    withCredentials: true
                }
            );
            setCurrentEmail('');
            setNewEmail('');
            handleLogout();
            alert("Az email-cím módosult!");


        }catch(error){
            if(error.response){
                const { status, data } = error.response;
                console.error(`Hiba történt: ${status} - ${data.error}`);
                if (status === 404) {
                    alert("Nem található a felhasználó!");
                } else if (status === 401) {
                    alert("A megadott jelenlegi email-cím nem egyezik meg az email-címeddel!");
                    
                }else if(status === 409) {
                    alert("A megadott e-cím foglalt!");
                
                }else {
                    alert(`Ismeretlen hiba: ${status} - ${data.error}`);
                }
            }
            console.error("Hiba történt: ", error.message);
        }

    }


    return (
        <form className='main-content'>
            <div className='settings-container'>
                <p className="settings-text">Beállítások</p> 
                <hr className='settings-line'/>
                <p className="change-password-text">Jelszó megváltoztatása</p> 
                <div className='settings-first-row'>
                    <div className='current-password-container'>
                        <img name="change-password-icon" src={password_icon} alt=''></img>
                        <input name="current-password"
                            type='password'
                            placeholder='Jelenlegi jelszó'
                            value={currentPassword}
                            onChange={(e) => setCurrentPassword(e.target.value)}
                        >
                        </input>
                    </div>
                    
                    <div className='new-password-container'>
                        <img name="change-password-icon" src={password_icon} alt=''></img>
                        <input name="new-password"
                            type='password'
                            placeholder='Új jelszó'
                            value={newPassword}
                            onChange={handleNewPassword}
                        >
                        </input>
                        
                    </div>

                    <div className='new-password-again-container'>
                        <img name="change-password-icon" src={password_icon} alt=''></img>
                        <input name="new-password-again"
                            type='password'
                            placeholder='Új jelszó ismét'
                            value={newPasswordAgain}
                            onChange={handleConfirmPasswordChange}
                        >
                        </input>
                        
                    </div>
                </div>
                <div className='password-tooltips-container'>
                    {activeTooltip === 'newPassword' && (
                        <p className='wrongNewPasswordText'>
                            A jelszónak legalább 8 karakter hosszúnak kell lennie. Továbbá tartalmaznia kell nagybetűtek és kisbetűket, 
                            egy számot és egy speciális karaktert, amik a következők lehetnek: !, @, #, $, %.
                        </p>
                    )}
                    
                    {activeTooltip === 'newPasswordAgain' && (
                        <p className='wrongNewPasswordAgainText'>
                            A két jelszó nem egyezik meg!
                        </p>
                    )}
                </div>

                <button className='save-new-password' onClick={handleChangingPassword}>
                    Jelszó módosítása
                </button>

                <p className="change-email-text">Email-cím megváltoztatása</p>

                <div className='settings-second-row'>
                    <div className='current-email-container'>
                        <img name="change-email-icon" src={email_icon} alt=''></img>
                        <input name="current-email"
                            type='text'
                            placeholder='Jelenlegi email-cím'
                            value={currentEmail}
                            onChange={(e) => setCurrentEmail(e.target.value)}
                        >
                        </input>
                    </div>
                    
                    <div className='new-email-container'>
                        <img name="change-email-icon" src={email_icon} alt=''></img>
                        <input name="new-email"
                            type='text'
                            placeholder='Új email-cím'
                            value={newEmail}
                            onChange={handleNewEmail}

                        >
                        </input>
                    </div>
                </div>

                {activeTooltip === 'newEmail' && (
                    <p className='wrongNewEmail'>
                        Az új email-cím nem egyezhet meg a régéivel!
                    </p>
                )}
                

                <button className='save-new-email' onClick={handleChangingEmail}>
                    Email-cím módosítása
                </button>

            </div>
        </form>
)}
