import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import useAuth from '../../hooks/useAuth';
import useLogout from "../../hooks/useLogout";
import "../navbar/navbar.css";

import controller_icon from "../Assets/game-controller.svg";

export default function Navbar() {
  const { auth } = useAuth();
  const logoutUser = useLogout(); 
  const location = useLocation();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);
  const isAdmin = auth?.roles?.includes("ADMIN");
  const isModerator = auth?.roles?.includes("MODERATOR");

  const handleNavigate = () => {
    if (location.pathname === "/login") {
      navigate("/register");
    } else {
      navigate("/login");
    }
  };

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };

  const handleLogout = () => {
    logoutUser();
    setMenuOpen(!menuOpen);
  };

  const goHome = () => {
    navigate("/home");

  };

  const goSettings = () => {
    navigate("/settings");
    setMenuOpen(!menuOpen);
  };

  const navigateToMyGameDescriptions = () => {
    navigate("/my-game-descriptions");
    setMenuOpen(!menuOpen);
  };

  const navigateToAdminPanel = () => {
    navigate("/user-management");
    setMenuOpen(!menuOpen);
  };

  const shouldShowHamburgerMenu = auth?.accessToken;

  return (
    <header className="header">
      <nav className="navbar">
        <div className="navbar-logo" onClick={goHome}>
          <img src={controller_icon} alt="PlayForFun logo" />
          <h1 className="first-title">PlayForFun</h1>
        </div>

        {shouldShowHamburgerMenu && (
          <>
            <div className={`navbar-links ${menuOpen ? "show" : ""}`}>
              <ul>
                <li onClick={goSettings}>Beállítások</li>
                <li onClick={navigateToMyGameDescriptions}>Játékleírásaim</li>
                {(isAdmin || isModerator) && ( 
                  <li onClick={navigateToAdminPanel}>Felhasználók kezelése</li>
                )}
                <li onClick={handleLogout}>Kijelentkezés</li>
              </ul>
            </div>

            <div className="hamburger-menu" onClick={toggleMenu}>
              <div className={`bar ${menuOpen ? "open" : ""}`}></div>
              <div className={`bar ${menuOpen ? "open" : ""}`}></div>
              <div className={`bar ${menuOpen ? "open" : ""}`}></div>
            </div>
          </>
        )}


        {!auth?.accessToken && (
          <button
            className="register-per-login-button"
            title={location.pathname === "/login" ? "Regisztráció" : "Bejelentkezés"}
            onClick={handleNavigate}
          >
            <span className="register-text">
              {location.pathname === "/login" ? "Regisztráció" : "Mégse"}
            </span>
          </button>
        )}
      </nav>
    </header>
  );
}