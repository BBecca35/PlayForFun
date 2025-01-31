import React from 'react';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axios';
import './Home.css';

export default function Home() {
    const [cells, setCells] = useState([]);
    //const navigate = useNavigate();
    const FETCH_GAME_DESCRIPTIONS_URL = `/gd-api/gameDescriptions`; 
    const [IsArrayEmpty, setIsArrayEmpty] = useState(false);
    const navigate = useNavigate();

    
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
    }, [FETCH_GAME_DESCRIPTIONS_URL]);

    useEffect(() => {
        if (cells.length === 0) {
            setIsArrayEmpty(true);
        } else {
            setIsArrayEmpty(false);
        }
    }, [cells]); // cells változása esetén fut le
    
    const handleEdit = (id) => {
        navigate(`/description/${id}`); // Navigálás a szerkesztő oldalra az ID-vel
    };

    return (
        <div className="home-container">
            <div className="home-content">
                <input type="text" className="search-bar" placeholder="Keresés" />
                {IsArrayEmpty && (
                    <p className="no-description">
                        Játékleírás még nem lett létrehozva egy felhasználó által sem!
                    </p>
                )}
                
                {!IsArrayEmpty && (
                    <div className="dynamic-table">
                    {cells.map((cell) => (
                        <div className="gd-container" key={cell.id} onClick={() => handleEdit(cell.id)}>
                            <img src={cell.imageUrl} alt="" className='gd-image' id='gd-image'/>
                            <label for="gd-image" name='gd-title'>{cell.title}</label>
                        </div>
                    ))}
                    </div>
                )}

            </div>
        </div>
    );
}
