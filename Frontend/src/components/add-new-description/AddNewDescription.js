import React from 'react';
import './AddNewDescription.css';
import placeholder from '../Assets/placeholder.png';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axios';

export default function AddNewDescription() {
   
    const [gameName, setGameName] = useState('');
    const [publisher, setPublisher] = useState('');
    const [description, setDescription] = useState('');
    const [genre, setGenre] = useState('');
    const [publicationYear, setPublicationYear] = useState('');
    const [platform, setPlatform] = useState('');
    const [ageLimit, setAgeLimit] = useState('');
    const [image, setImage] = useState(placeholder);
    const navigate = useNavigate();

    const generateYearOptions = () => {
        const currentYear = new Date().getFullYear();
        const startYear = 1952;
        const years = [];
        for (let year = startYear; year <= currentYear; year++){
            years.push(year);
        }
        return years;
    }

    const handleImage = (e) => {
        const file = e.target.files[0];
        if(file){
            const render = new FileReader();
            render.onload = () => {
                setImage(render.result);
            }
            render.readAsDataURL(file);
        }

    }

    const handleSaving = async (e) => {
        e.preventDefault();

        const userId = localStorage.getItem('userId');
        const publicationYearInt = parseInt(publicationYear, 10);
        const ageLimitInt = parseInt(ageLimit, 10);
        const userIdLong = userId ? Number(userId) : null;
        const CREATE_NEW_DESCRIPTION_URL = `/gd-api/user/${userIdLong}/gameDescriptions`

        const formData = new FormData();
        formData.append("dto", new Blob ([JSON.stringify({
            name: gameName,
            genre: genre,
            publisher: publisher,
            platform: platform,
            publishedAt: publicationYearInt,
            ageLimit: ageLimitInt,
            description: description
        })], {type: "application/json"}));

        if (image && image !== placeholder) {
            // Csak akkor adjuk hozzá a fájlt, ha van tényleges fájl
            const fileInput = document.getElementById("myfile");
            if (fileInput.files.length > 0) {
                const file = fileInput.files[0]; // A feltöltött fájl
                formData.append("image", file); // Fájl csatolása
            }
        }

        if (e.target.type === "file") {
            return;
        }

        if (!gameName.trim() || !publisher.trim() || !publisher.trim() ) {
            alert("Az összes mezőt ki kell tölteni!");
            return;
        }

        if(ageLimit === '' || publicationYear === '' || platform === '' || genre === ''){
            alert("Az összes mezónél válasszon egy elemet!");
            return;            
        }

        try {
            const response = await axiosInstance.post(
                CREATE_NEW_DESCRIPTION_URL, 
                formData
            );
            const { Id } = response.data;
            localStorage.setItem('gdId', Id);
            navigate("/my-game-descriptions");         
    
        } catch (error) {
            if(error.response){
                const { status, data } = error.response;
                console.error(`Hiba történt: ${status} - ${data.error}`);
                if (status === 409) {
                    alert("Ezzel a névvel már hoztak létre leírást!");  
                } else {
                    alert(`Ismeretlen hiba: ${status} - ${data.error}`);
                }
            }
            console.error("Hiba történt: ", error.message);
        }

    }

    return (
        <form className="new-description-container" onSubmit={handleSaving}>
            <div className='title-container'>
                <p className="new-description-title">
                    Új leírás létrehozása
                </p>
                <hr className='new-description-line'/>
            </div>
            
            <div className='contents'>
                <div className='image-container'>
                    <img className='image-placeholder' src={image} alt='placeholer' onChange={handleImage}></img>
                    <div className='browse-button'>
                        <label for='myfile'>Tallózás</label>
                        <input type="file" id='myfile' className="myfile" onChange={handleImage}/>    
                    </div> 
                </div>
                <div className='text-inputs'>
                    <div className='first-row'>
                        <input className='game-title'
                            type='text' 
                            name='game-title' 
                            placeholder='A játék neve'
                            value={gameName}
                            onChange={(e) => setGameName(e.target.value)} 
                        />

                        <input className='publisher'
                            type='text' 
                            name='publisher' 
                            placeholder='A Kiadó neve'
                            value={publisher}
                            onChange={(e) => setPublisher(e.target.value)} 
                        />
                    </div>

                    <div className='second-row'>
                        <div className='genre-container'>
                            <label for="genre" name="genre-text">A játék műfaja</label>
                            <select className='genre'
                                id='genre'
                                name='genre'
                                placeholder="A játék műfaja" 
                                value={genre}
                                onChange={(e) => setGenre(e.target.value)} 
                            >
                                <option value="">-- Választás --</option>
                                <option value="akció">Akció</option>
                                <option value="kaland">Kaland</option>
                                <option value="harc">Harc</option>
                                <option value="verseny">Verseny</option>
                                <option value="rpg">RPG</option>
                                <option value="szimuláció">Szimuláció</option>
                                <option value="Parlor">Parlor</option>
                                <option value="MMO">MMO</option>
                                <option value="egyéb">Egyéb</option>

                            </select>
                        </div>
                        
                        <div className='publication-year-container'>
                            <label for="publication-year" name="publication-year-text">A kiadás éve</label>
                            <select className='publication-year'
                                id='publication-year'
                                name='publication-year'
                                placeholder="A kiadás éve" 
                                value={publicationYear}
                                onChange={(e) => setPublicationYear(e.target.value)} 
                            >
                                <option value="">-- Választás --</option>
                                {generateYearOptions().map((year) => (
                                    <option key={year} value={year}>{year}</option>

                                ))}
                            </select>
                        </div>
                    </div>

                    <div className='third-row'>
                        <div className='platform-container'>
                            <label for="platform" name="platform-text">Platform</label>
                            <select className='platform'
                                id='platform'
                                name='platform'
                                placeholder="Platform" 
                                value={platform}
                                onChange={(e) => setPlatform(e.target.value)} 
                            >
                                <option value="">-- Választás --</option>
                                <option value="pc">PC</option>
                                <option value="pc,ios">PC, IOS</option>
                                <option value="pc,android,ios">PC, Android, IOS</option>
                                <option value="pc,ios,xbox">PC, IOS, XBOX</option>
                                <option value="pc,ios,ps5">PC, IOS, PS5</option>
                                <option value="pc,ps5">PC, PS5</option>
                                <option value="pc,xbox">PC, XBOX</option>
                                <option value="pc,xbox,ps5">PC, XBOX, PS5</option>
                                <option value="pc,nintendo,ios">PC, Nintento, IOS</option>
                                <option value="pc,nintendo,xbox,ps5">PC, Nintento, XBOX, PS5</option>
                                <option value="pc,nintendo,ios,xbox,ps5">PC, Nintento, XBOX, PS5, IOS</option>
                                <option value="pc,ps5,xbox,ios,android,nintento">PC, PS5, XBOX, IOS, Andriod, Nintendo</option>

                            </select>
                        </div>
                        
                        <div className='age-limit-container'>
                            <label for="age-limit" name="age-limit-text">Korhatár besorolás</label>
                            <select className='age-limit'
                                id='age-limit'
                                name='age-limit'
                                placeholder="Korhatár besorolás" 
                                value={ageLimit}
                                onChange={(e) => setAgeLimit(e.target.value)} 
                            >
                                <option value="">-- Választás --</option>
                                <option value="3">3</option>
                                <option value="7">7</option>
                                <option value="12">12</option>
                                <option value="16">16</option>
                                <option value="18">18</option>
                            </select>
                        </div>
                    </div>

                    <div className='last-row'>
                        <textarea className='description-text'
                            type='text' 
                            name='description' 
                            placeholder='Leírás'
                            value={description}
                            onChange={(e) => setDescription(e.target.value)} 
                        />
                    </div>

                    <div className='saving-button-container'>
                        <button type='submit' className='saving-button'>Mentés</button>
                    </div>

                </div>
            </div>
            
                       
        </form>
    );
}
