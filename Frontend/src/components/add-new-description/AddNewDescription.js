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

        if (e.target.type === "file") {
            return;
        }

        if (!gameName.trim() || !publisher.trim() || !description.trim() ) {
            alert("Az összes mezőt ki kell tölteni!");
            return;
        }

        if(ageLimit === '' || publicationYear === '' || platform === '' || genre === ''){
            alert("Az összes mezónél válasszon egy elemet!");
            return;            
        }

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

        try {
            const response = await axiosInstance.post(
                CREATE_NEW_DESCRIPTION_URL, 
                formData
            );
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
                        <label for='myfile' className='browse-button-text'>Tallózás</label>
                        <input type="file" id='myfile' className="myfile" onChange={handleImage}/>    
                    </div> 
                </div>
                <div className='text-inputs'>
                    <div className='first-row'>
                        <input className='write-game-title'
                            type='text' 
                            name='write-game-title' 
                            placeholder='A játék neve'
                            value={gameName}
                            onChange={(e) => setGameName(e.target.value)} 
                        />

                        <input className='write--publisher'
                            type='text' 
                            name='write-publisher' 
                            placeholder='A Kiadó neve'
                            value={publisher}
                            onChange={(e) => setPublisher(e.target.value)} 
                        />
                    </div>

                    <div className='second-row'>
                        <div className='genre-container'>
                            <label for="genre" name="genre-text">A játék műfaja</label>
                            <select className='write-genre'
                                id='genre'
                                name='write-genre'
                                placeholder="A játék műfaja" 
                                value={genre}
                                onChange={(e) => setGenre(e.target.value)} 
                            >
                                <option value="">-- Választás --</option>
                                <option value="akció">Akció</option>
                                <option value="kaland">Kaland</option>
                                <option value="harc">Harc</option>
                                <option value="verseny">Verseny</option>
                                <option value="RPG">RPG</option>
                                <option value="szimuláció">Szimuláció</option>
                                <option value="Parlor">Parlor</option>
                                <option value="MMO">MMO</option>
                                <option value="egyéb">Egyéb</option>

                            </select>
                        </div>
                        
                        <div className='publication-year-container'>
                            <label for="publication-year" name="publication-year-text">A kiadás éve</label>
                            <select className='write--publication-year'
                                id='publication-year'
                                name='write-publication-year'
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
                            <select className='write-platform'
                                id='platform'
                                name='write-platform'
                                placeholder="Platform" 
                                value={platform}
                                onChange={(e) => setPlatform(e.target.value)} 
                            >
                                <option value="">-- Választás --</option>
                                <option value="PC">PC</option>
                                <option value="PC, IOS">PC, IOS</option>
                                <option value="PC, Android, IOS">PC, Android, IOS</option>
                                <option value="PC, IOS, XBOX">PC, IOS, XBOX</option>
                                <option value="PC, IOS, PlayStation">PC, IOS, PlayStation</option>
                                <option value="PC, PlayStation">PC, PlayStation</option>
                                <option value="PC, XBOX">PC, XBOX</option>
                                <option value="PC, XBOX, PlayStation">PC, XBOX, PlayStation</option>
                                <option value="PC, Nintendo, IOS">PC, Nintendo, IOS</option>
                                <option value="PC, Nintendo, XBOX, PlayStation">PC, Nintendo, XBOX, PlayStation</option>
                                <option value="PC, Nintendo, XBOX, PlayStation, IOS">PC, Nintendo, XBOX, PlayStation, IOS</option>
                                <option value="PC, PlayStation, XBOX, IOS, Andriod, Nintendo">PC, PlayStation, XBOX, IOS, Andriod, Nintendo</option>

                            </select>
                        </div>
                        
                        <div className='age-limit-container'>
                            <label for="age-limit" name="age-limit-text">Korhatár besorolás</label>
                            <select className='write-age-limit'
                                id='age-limit'
                                name='write-age-limit'
                                placeholder="Korhatár besorolás" 
                                value={ageLimit}
                                onChange={(e) => setAgeLimit(e.target.value)} 
                            >
                                <option value="">-- Választás --</option>
                                <option value="3">PEGI 3</option>
                                <option value="7">PEGI 7</option>
                                <option value="12">PEGI 12</option>
                                <option value="16">PEGI 16</option>
                                <option value="18">PEGI 18</option>
                            </select>
                        </div>
                    </div>

                    <div className='last-row'>
                        <textarea className='description-text'
                            type='text' 
                            name='write-description' 
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
