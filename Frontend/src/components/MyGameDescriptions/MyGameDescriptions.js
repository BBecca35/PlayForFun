import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axios';
import '../MyGameDescriptions/MyGameDescriptions.css';

export default function MyGameDescriptions() {
    const [cells, setCells] = useState([]);
    const userId = localStorage.getItem('userId');
    const userIdLong = userId ? Number(userId) : null;
    const navigate = useNavigate();
    const FETCH_GAME_DESCRIPTIONS_URL = `/gd-api/user/${userIdLong}/gameDescriptions`;

    useEffect(() => {
        const fetchGameDescriptions = async () => {
            try {
                const response = await axiosInstance.get(FETCH_GAME_DESCRIPTIONS_URL);
                const data = response.data.map((item) => ({
                    id: item.id,
                    title: item.name,
                    imageUrl: `http://localhost:8080/api/images/${item.imageName}`, // A kép URL-je
                }));
                setCells(data);
            } catch (error) {
                console.error('Hiba történt a játék leírások betöltésekor:', error);
                alert('Nem sikerült betölteni a játékokat. Próbáld újra később.');
            }
        };
    
        fetchGameDescriptions();
    }, []); // Csak a komponens betöltésekor fut le

    const addCell = () => {
        navigate("/add-new-description");
      };
    
    return (
        <div className="my-game-decriptions-container">
            
            <div className="home-content">
                <p className="my-description-text">
                    Játékleírásaim
                </p> <hr className='line'/>

                <div className="dynamic-table">
                    {cells.map((cell) => (
                        <div className="gd-container">
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

        </div>
    );
}