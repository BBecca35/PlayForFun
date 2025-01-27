import React from 'react';
import './Description.css';
import placeholder from '../Assets/placeholder.png';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axiosInstance from '../../api/axios';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar } from "@fortawesome/free-solid-svg-icons";

export default function Description() {
   
    const { id } = useParams(); 

    const [gameName, setGameName] = useState('');
    const [gdUserId, setGdUserId] = useState('');
    const [creator, setCreator] = useState('');
    const [publisher, setPublisher] = useState('');
    const [description, setDescription] = useState('');
    const [genre, setGenre] = useState('');
    const [publicationYear, setPublicationYear] = useState('');
    const [platform, setPlatform] = useState('');
    const [ageLimit, setAgeLimit] = useState('');
    const [image, setImage] = useState(placeholder);

    const [comment, setComment] = useState('');
    const [rating, setRating] = useState(null);
    const [hover, setHover] = useState(null);

    console.log(`A komment értéke : ${comment}`);

    useEffect(() => {
        const fetchGameDescription = async () => {
            try {
                const gdResponse = await axiosInstance.get(`/gd-api/gameDescriptions/${id}`);
                const gdData = gdResponse.data;
                //console.log("API válasz:", gdData); 
                setGdUserId(gdData.userId);
                setGameName(gdData.name);
                setPublisher(gdData.publisher);
                setDescription(gdData.description);
                setGenre(gdData.genre);
                setPublicationYear(gdData.publishedAt.toString());
                setPlatform(gdData.platform);
                setAgeLimit(gdData.ageLimit.toString());
                if (gdData.imageName) {
                    const imageUrl = `http://localhost:8080/api/images/${gdData.imageName}`;
                    setImage(imageUrl);
                }
                if(gdUserId){
                    const response = await axiosInstance.get(`/user-api/user/${gdUserId}`);
                    const userData = response.data;
                    setCreator(userData.username);
                }

            } catch (error) {
                console.error("Hiba történt az adatok betöltésekor: ", error);
            }
        };

        fetchGameDescription();
    }, [id, gdUserId]);


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

    const handleSending = async (e) => {
        e.preventDefault();
        if(!comment.trim()){
            alert("A komment mezőt nem hagyhatod üresen!");
            return;
        }
        
        if(!rating){
            alert("Az értékelés kötelezőnek számít!");
            return;
        }

        const userId = localStorage.getItem('userId');
        const userIdLong = userId ? Number(userId) : null;
        const COMMENT_POST_URL = `/comment-api/user/${userIdLong}/gameDescription/${id}/comments`;
        try{
            console.log("Küldött adatok:", { comment, rating });
            const commentResponse = await axiosInstance.post(COMMENT_POST_URL, 
                JSON.stringify({ message: comment, rating: rating }),
                    {
                        headers: { 'Content-Type': 'application/json' },
                        withCredentials: true
                    })
            alert("Sikeres küldés!");

        }catch(error){
            if(error.response){
                const { status, data } = error.response;
                console.error(`Hiba történt: ${status} - ${data.error}`);
                if (status === 404 && data.error === "User not found") {
                    alert("Nem található a felhasználó!");
                } else if (status === 404 && data.error === "Game Description not found!") {
                    alert("Nem található a játékleírás!");
                }
                else {
                    alert(`Ismeretlen hiba: ${status} - ${data.error}`);
                }
            }
            console.error("Hiba történt: ", error.message);
        }

        
    }

    return (
        <form className="one-description">
            <div className='title-container'>
                <p className="description-title">
                    {gameName}
                </p>              
                <hr className='new-description-line'/>
            </div>
            
            <div className='contents'>
                <div className='image-container'>
                    <img className='image-placeholder' src={image} alt='placeholer' onChange={handleImage}></img> 
                </div>
                <div className='text-inputs'>
                    <div className='read-first-row'>
                        <p className="genre-text">A játék műfaja: {genre}</p>
                        <p className="creator-text">A leírás készítője: {creator}</p>
                    </div>

                    <div className='read-second-row'>
                        <p className='publisher'>Kiadó: {publisher}</p>
                        <p className="platform-text">Platform: {platform}</p> 
                        
                    </div>

                    <div className='read-third-row'>
                        <p className="publication-year-text">Kiadás éve: {publicationYear}</p>
                        <p className="age-limit-text">Korhatár: PEGI {ageLimit}</p>
                    </div>

                    <div className='read-last-row'>
                        <p className="description">{description}</p>
                    </div>

                    <div className='comment-editor'>
                        <textarea className='comment-editor-text'
                            type='text'  
                            placeholder='Megjegyzés hozzáadása:'
                            value={comment}
                            onChange={(e) => setComment(e.target.value)} 
                        />
                        <div className='rating-sending-group'>
                            <label for="rating-container" className='rating-text'>Értékelés: </label>
                            <div className="rating-container" id="rating-container">
                                {[...Array(5)].map((star,index) => {
                                    const currentRating = index + 1;
                                    return(
                                        <label key={index}>
                                            <input type="radio" 
                                                name="rating"
                                                value={currentRating}
                                                onClick={() => setRating(currentRating)}

                                            />
                                            <FontAwesomeIcon className='rating-icon' 
                                                icon={faStar} 
                                                size="2x"
                                                color={currentRating > (hover|| rating) ? "#FFFFFF" : "#4FC4F3"}
                                                onMouseEnter={() => setHover(currentRating)}
                                                onMouseLeave={() => setHover(null)}
                                            />
                                        </label>
                                    );
                                    

                                })}
                            </div>
                            <button className='send-button' onClick={handleSending}>Küldés</button>
                        </div>
                        
                    </div>

                </div>
            </div>
            
            <div className='comment-section'>

            </div>
                       
        </form>
    );
}
