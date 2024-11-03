import React from 'react'
import "../login/Login.css"

import user_icon from "../Assets/person.png"
import password_icon from "../Assets/password.png"
//import email_icon from "../Assets/email.png"

export default function Login() {
    return (
        <div className='container'>
            <div className='header'>
                <div className='text'>Bejelentkezés</div>
            </div>

            <div>
                <div className='inputs'>

                    <div className='input'>
                        <img src={"user_icon"} alt='' />
                        <input type='username_text' placeholder='Felhasználónév' />
                    </div>
                    <div className='input'>
                        <img src={'password_icon'} alt='' />
                        <input type='password_text' placeholder='Jelszó' />
                    </div>

                </div>
                <div>
                    <input type="checkbox" id="remember" />
                    <label for="remember" className="custom-checkbox">Emlékezz rám</label>
                </div>

                <a className='forget' href="#">Elfelejtette a jelszavát?</a>
                <div className='submit-container'>
                    <div className='submit'>Belépés</div>
                </div>
            </div>
        </div>
    )
}
