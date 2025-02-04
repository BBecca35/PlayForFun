import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import '../navbar/navbar.css';

import controller_icon from '../Assets/game-controller.svg';

export default function Navbar({ onLogout }) {
    const location = useLocation();
    const navigate = useNavigate();
    const [menuOpen, setMenuOpen] = useState(false);

    // Navigációs gomb kezelése az aktuális útvonal alapján
    const handleNavigate = () => {
        if (location.pathname === '/login') {
            navigate('/register'); 
        } else {
            navigate('/login'); 
        }
    };

    // Menü váltás funkció
    const toggleMenu = () => {
        setMenuOpen(!menuOpen);
    };

    const handleLogout = () => {
        onLogout();
        navigate('/login');

    };

    const goHome = () => {
        navigate('/home')
    }

    const goSettings = () => {
        navigate('/settings')
    }

    const navigateToMyGameDescriptions = () => {
        navigate('/my-game-descriptions');
    };
    

    const shouldShowHamburgerMenu = location.pathname !== '/login' && location.pathname !== '/register';

    return (
        <header className="header">
            <nav className="navbar">
            <div className="navbar-logo" onClick={goHome}>
                <img src={controller_icon} alt="PlayForFun logo" />
                <h1 className='first-title'>PlayForFun</h1>
            </div>

            {shouldShowHamburgerMenu && (
                <div className="hamburger-menu" onClick={toggleMenu}>
                    <div className={`bar ${menuOpen ? 'open' : ''}`}></div>
                    <div className={`bar ${menuOpen ? 'open' : ''}`}></div>
                    <div className={`bar ${menuOpen ? 'open' : ''}`}></div>
                </div>
            )}

            {shouldShowHamburgerMenu && (
                <div className={`dropdown-menu ${menuOpen ? 'show' : ''}`}>
                    <ul>
                        <li onClick={goSettings}>Beállítások</li>
                        <li onClick={navigateToMyGameDescriptions}>Játékleírásaim</li>
                        <li onClick={handleLogout}>Kijelentkezés</li>
                    </ul>
                </div>

            )}

            {(location.pathname === '/login' || location.pathname === '/register') && (
                <button 
                    className="register-per-login-button" 
                    title={location.pathname === '/login' ? 'Regisztráció' : 'Bejelentkezés'}
                    onClick={handleNavigate}
                >
                    <span className="register-text">
                        {location.pathname === '/login' ? 'Regisztráció' : 'Mégse'}
                    </span>
                </button>
            )}  
            </nav>
        </header>
    );
}