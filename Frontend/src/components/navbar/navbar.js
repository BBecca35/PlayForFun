import React from 'react'
import '../navbar/navbar.css'

import controller_icon from '../Assets/game-controller.svg'

export default function Navbar() {
    return (
        <header>
            <div className="topbar">
                <div className="navbar-logo">
                    <img src={controller_icon} alt="PlayForFun logo" />
                    <h1>PlayForFun</h1>
                </div>
                <a href="#" class="register-button" title="Regisztráció">
                    <span class="register-text">Regisztráció</span>
                </a>
            </div>
        </header>
    )
}
