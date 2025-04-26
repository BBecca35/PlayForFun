import React from 'react'
import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import useAuth from '../../hooks/useAuth';
import axiosInstance from '../../api/axiosInstance';
import { format } from 'date-fns';
import "./manageUser.css"

export default function ManageUser() {
  const location = useLocation();
  const navigate = useNavigate();
  const user_id = location.state?.id;
  const { auth } = useAuth();
  const isAdmin = auth?.roles?.includes("ADMIN");
  const LOAD_USER_URL = `/user-api/user/get/${user_id}`;
  const PROMOTE_URL = `/user-api/user/promote/${user_id}`;
  const DEMOTE_URL = `/user-api/user/demote/${user_id}`

  const[username, setUsername] = useState("");
  const[email, setEmail] = useState("");
  const[birthdate, setBirthdate] = useState("");
  const[role, setRole] = useState("");
  const[banned, setBanned] = useState(false);

  useEffect(() => {
    const loadUser = async () => {
      try{
        const response = await axiosInstance.get(LOAD_USER_URL, {
          headers: { Authorization: `Bearer ${auth.accessToken}`, 
                     Accept: 'application/json'
          },
          withCredentials: true
        })
        const data = response.data;
        setUsername(data.username);
        setEmail(data.email);
        setBirthdate(format(new Date(data.birthdate), "yyyy. MM. dd. "));
        setBanned(data.banned);
        setRole(data.role === "USER" ? "Felhasználó" : "Moderátor");

      }
      catch(error){
        console.error(error);
      }

    }
    loadUser();
  }, [LOAD_USER_URL, auth.accessToken]);

  const handleBanUser = (id) => {
    if(banned){
      alert("Az adott felhasználó már ki van tiltva az oldalról!");
    }
    else{
      navigate("/ban-user", {state: { id }});
    }
    
  };

  const editBanUser = (id) => {
    if(!banned){
      alert("Az adott felhasználó még nem lett kitiltva az oldalról");
    }
    else{
      navigate("/edit-ban", {state: { id }});
    }
    
  };

  const handlePromote = async () => {
    try{
      const response = await axiosInstance.put(PROMOTE_URL, {}, {
        headers: { Authorization: `Bearer ${auth.accessToken}`, 
                   Accept: 'application/json'
        },
        withCredentials: true
      })
      navigate("/user-management");
      alert(response.data);
    }catch(error){
      if(error.response){
        const { status, data } = error.response;
        if(status === 404){
          alert("A felhasználó nem található!");
        }else if(status === 409){
          alert("Ez a felhasználó már rendelkezik moderátori ranggal");
        }else if(status === 403){
          alert("Admin ranggal rendelkező felhasználó nem fokozható le moderátorrá");
        }else{
          alert("Váratlan hiba történt az előléptetés során!");
          console.error(data.error);
        }
      }
      alert("Váratlan hiba!");
      console.error(error);
    }
  }

  const handleDemote = async () => {
    try{
      const response = await axiosInstance.put(DEMOTE_URL, {}, {
        headers: { Authorization: `Bearer ${auth.accessToken}`, 
                   Accept: 'application/json'
        },
        withCredentials: true
      })
      navigate("/user-management");
      alert(response.data);
    }catch(error){
      if(error.response){
        const { status, data } = error.response;
        if(status === 404){
          alert("A felhasználó nem található!");
        }else if(status === 409){
          alert("A felhasználó már le lett fokozva, vagy nem lett előléptetve!");
        }else if(status === 403){
          alert("Admin ranggal rendelkező felhasználó nem fokozható le felhasználóvá!");
        }else{
          alert("Váratlan hiba történt a lefokozás során!");
          console.error(data.error);
        }
      }
      alert("Váratlan hiba!");
      console.error(error);
    }
  }

  return (
    <div className='manage-user-page'>
      <div className='user-data-container'>
        <h1 className='user-data-title'>Felhasználó adatai</h1>
        <hr className='user-data-title-line'/>
        <p className='user-data'>Felhasználónév: {username}</p>
        <p className='user-data'>Email-cím: {email}</p>
        <p className='user-data'>Születési dátum: {birthdate}</p>
        <p className='user-data'>Betöltött szerepkör: {role}</p>
        <p className='user-data'>Kitiltott: {banned ? "Igen" : "Nem"}</p>
      </div>
      <div className='settings-option-container'>
        <h1 className='settings-option-title'>Beállítási opciók</h1>
        <hr className='settings-option-title-line'/>
        <button className='option-button' onClick={() => handleBanUser(user_id)}>Kitiltás kiadása</button>
        <button className='option-button' onClick={() => editBanUser(user_id)}>Kitiltás módosítása</button>
        {isAdmin && role === "Felhasználó" &&(
          <button className='option-button' onClick={handlePromote}>Előléptetés</button>
        )}
        {isAdmin && role === "Moderátor" &&(
          <button className='option-button' onClick={() => handleDemote()}>Lefokozás</button>
        )}
      </div>
    </div>
  )
}
