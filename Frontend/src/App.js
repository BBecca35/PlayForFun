import React, { useState, useEffect } from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/login/Login';
import Navbar from './components/navbar/navbar';
import Register from './components/register/Register';
import Home from './components/home/Home';
import MyGameDescriptions from './components/my-game-descriptions/MyGameDescriptions';
import AddNewDescripion from './components/add-new-description/AddNewDescription';
import EditDescription from './components/edit-description/EditDescription';
import Description from './components/description/Description';

function App() {
  
   const [isLoggedIn, setIsLoggedIn] = useState(() => {
    return localStorage.getItem('isLoggedIn') === 'true';
  });

  //console.log('Stored Logging:', localStorage.getItem('logging'));
  //console.log(`Emlékezz rám értéke az appban: ${rememberMe}`);

  useEffect(() => {
    const storedRememberMe = localStorage.getItem('rememberMe') === 'true';
    const accessToken = storedRememberMe
        ? localStorage.getItem('accessToken')
        : sessionStorage.getItem('accessToken');
    const refreshToken = localStorage.getItem('refreshToken');

    //console.log('Stored RememberMe:', storedRememberMe);
    //console.log('AccessToken:', accessToken);
    //console.log('RefreshToken:', refreshToken);

    if (accessToken || (storedRememberMe && refreshToken)) {
        setIsLoggedIn(true);
        localStorage.setItem('isLoggedIn', 'true');
    } else {
        setIsLoggedIn(false);
        localStorage.setItem('isLoggedIn', 'false'); 
    }
}, []);

  
  const handleLogin = () => {
    setIsLoggedIn(true);
    localStorage.setItem('isLoggedIn', 'true');  
  };

  

  const handleLogout = () => {
    localStorage.clear();
    sessionStorage.clear();
    setIsLoggedIn(false);
    localStorage.setItem('isLoggedIn', 'false');
};

  return (
    <Router>
      <div className="App">
        <Navbar isLoggedIn={isLoggedIn} onLogout={handleLogout} />
        <Routes>
    
          <Route path="/" element={isLoggedIn ? <Navigate to="/home" replace/> : <Navigate to="/login" replace />} /> 

          <Route path="/login" element={<Login onLogin={handleLogin}/>} />
          <Route path="/register" element={<Register />} />
          <Route path="/add-new-description" element={isLoggedIn ? <AddNewDescripion /> : <Navigate to="/login" replace />} />
          <Route path="/my-game-descriptions" element={isLoggedIn ? <MyGameDescriptions /> : <Navigate to="/login" replace />} />
          <Route path="/edit-description/:id" element={isLoggedIn ? <EditDescription /> : <Navigate to="/login" replace />} />
          <Route path="/description/:id" element={isLoggedIn ? <Description /> : <Navigate to="/login" replace />} />
          <Route 
            path="/home" 
            element={isLoggedIn ? <Home /> : <Navigate to="/login" replace />}
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
