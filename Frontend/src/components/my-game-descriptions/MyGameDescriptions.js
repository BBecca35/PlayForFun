import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMemo } from 'react';
import placeholder from '../Assets/placeholder.png';
import axiosInstance from '../../api/axiosInstance';
import useAuth from '../../hooks/useAuth';
import './MyGameDescriptions.css';

export default function MyGameDescriptions() {
    const [cells, setCells] = useState([]);
    const navigate = useNavigate(); 
    const { auth } = useAuth();
    const FETCH_GAME_DESCRIPTIONS_URL = useMemo(() => {
        return `/gd-api/user/${auth.userId}/gameDescriptions`;
    }, [auth.userId]);
   
    useEffect(() => {
        const fetchGameDescriptions = async () => {
            try {
                const response = await axiosInstance.get(FETCH_GAME_DESCRIPTIONS_URL,
                    {
                        headers: { "Authorization": `Bearer ${auth.accessToken}`, 
                        'Accept': 'application/json' 
                    }}
                );
                const data = response.data.map((item) => ({
                    id: item.id,
                    title: item.name,
                    imageUrl: item.imageName? `http://localhost:8080/api/images/${item.imageName}` : placeholder, 
                }));
                setCells(data);
            } catch (error) {
                console.error('Hiba történt a játék leírások betöltésekor:', error);
                alert('Nem sikerült betölteni a játékokat. Próbáld újra később.');
            }
        };
    
        fetchGameDescriptions();
    }, [FETCH_GAME_DESCRIPTIONS_URL, auth.accessToken]); 

    const addCell = () => {
        navigate("/add-new-description");
    };

    const handleEdit = (id) => {
        navigate("/edit-description", {state: { id }}); 
    };
    
    return (
        <form className="my-game-decriptions-container">
            <div className="home-content">
                <p className="my-description-text">
                    Játékleírásaim
                </p> <hr className='line'/>

                <div className="dynamic-table">
                    {cells.map((cell) => (
                        <div className="gd-container" key={cell.id} onClick={() => handleEdit(cell.id)}>
                            <img src={cell.imageUrl} alt="" className='gd-image' id='gd-image'/>
                            <label for="gd-image" name='gd-title'>{cell.title}</label>
                        </div>
                    ))}
                    <div className="add-new-description-container" onClick={addCell}>
                        <p className='add-icon' id="add-card">+</p>
                        <label for="add-card" name="add-text">Új leírás létrehozása</label>
                    </div>

                </div>
            </div>
        </form>
    );
}