import React from 'react';
import './Description.css';
import placeholder from '../Assets/placeholder.png';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axiosInstance from '../../api/axios';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar } from "@fortawesome/free-solid-svg-icons";
import { format } from 'date-fns';

export default function Description() {
   
    const { id } = useParams(); 

    const userId = localStorage.getItem('userId');
    const userIdLong = userId ? Number(userId) : null;

    const [gameName, setGameName] = useState('');
    const [gdUserId, setGdUserId] = useState('');
    const [creator, setCreator] = useState('');
    const [publisher, setPublisher] = useState('');
    const [description, setDescription] = useState('');
    const [genre, setGenre] = useState('');
    const [publicationYear, setPublicationYear] = useState('');
    const [platform, setPlatform] = useState('');
    const [avgRating, setAvgRating] = useState('');
    const [ageLimit, setAgeLimit] = useState('');
    const [image, setImage] = useState(placeholder);
    const [createdAt, setCreatedAt] = useState('');

    const [comment, setComment] = useState('');
    const [rating, setRating] = useState(null);
    const [hover, setHover] = useState(null);
    const [comments, setComments] = useState([]);
    const [isArrayEmpty ,setIsArrayEmpty] = useState(false);

    
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
                setAvgRating(gdData.avgRating === null ? 0 : gdData.avgRating);
                setCreatedAt(format(new Date(gdData.createdAt), "yyyy. MM. dd. "));
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

    
    const addCreatorsToComments = async (comments) => {
        const updatedComments = await Promise.all(
            comments.map(async (comment) => {
                if (comment.userId) {
                    try {
                        // Lekérdezzük a felhasználói adatokat az API-ról
                        const userResponse = await axiosInstance.get(`/user-api/user/${comment.userId}`);
                        const userData = userResponse.data;
                        comment.creator = userData.username; // Hozzáadjuk a creator mezőt
                    } catch (error) {
                        comment.creator = "Ismeretlen"; // Hibakezelés
                    }
                } else {
                    comment.creator = "Ismeretlen"; // Ha nincs userId
                }
                return comment;
            })
        );
        return updatedComments;
    };

    useEffect(() => {
        const fetchComments = async () => {
            try {
                const commentResponse = await axiosInstance.get(`/comment-api/gameDescription/${id}/comments`);
                let commentData = commentResponse.data.map((item) => ({
                       id: item.id,
                       userId: item.userId,
                       message: item.message,
                       rating: item.rating === null ? 0 : item.rating,
                       isMyComment: item.userId === userIdLong,
                       createdAt: format(new Date(item.createdAt), "yyyy. MM. dd. HH:mm")
                }));

                commentData = await addCreatorsToComments(commentData);
                setComments(commentData);

                //console.log("API válasz:", gdData); 

            } catch (error) {
                console.error("Hiba történt az adatok betöltésekor: ", error);
            }
        };

        fetchComments();
    }, [id]);

    useEffect(() => {
            if (comments.length === 0) {
                setIsArrayEmpty(true);
            } else {
                setIsArrayEmpty(false);
            }
    }, [comments]);

    const renderRatingStars = (rating) => {
        return (
            <div className="rating-container">
                {[...Array(5)].map((_, index) => {
                    const currentRating = index + 1;
                    return (
                        <FontAwesomeIcon 
                            key={index}
                            className="display-rating-icon" 
                            icon={faStar}
                            size="2x" 
                            color={currentRating <= rating ? "yellow" : "#FFFFFF"} 
                        />
                    );
                })}
            </div>
        );
    };

    const ifRatingis0 = (rating) => {
        if(rating === 0){
            return "Nincs értékelés"
        }
        else{
            return renderRatingStars(rating);
        }
    }

    const renderRating = (rating, isMyCommentparam) => {
        if(rating !== 0 && isMyCommentparam){
            return "my-rating"
        }
        else if(rating !== 0 && !isMyCommentparam){
            return "others-rating"
        }
        else{
            return "no-rating"
        }
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

    const handleSending = async (e) => {
        e.preventDefault();
        if(!comment.trim()){
            alert("A komment mezőt nem hagyhatod üresen!");
            return;
        }
        
        const userId = localStorage.getItem('userId');
        const userIdLong = userId ? Number(userId) : null;
        const COMMENT_POST_URL = `/comment-api/user/${userIdLong}/gameDescription/${id}/comments`;
        try{
            //console.log("Küldött adatok:", { comment, rating });
            const commentResponse = await axiosInstance.post(COMMENT_POST_URL, 
                JSON.stringify({ message: comment, rating: rating }), {
                        headers: { 'Content-Type': 'application/json' },
                        withCredentials: true 
                }
            )

            window.location.reload();         
            
    
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

    /*
    A leírás képe alá egy átlag értékelőt kéne majd megjeleníteni, ami átlagolja a másoktól kapott értékeléseket és azt jeleníti meg. 
    */    
        
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

                    <div className='read-fourth-row'>
                        <p className="created-at-text">Létrehozási dátum: {createdAt}</p>
                        <label for="avg-rating-value" className="avg-rating-text">Átlag értékelés: </label>
                        <div className="avg-rating-value" id="avg-rating-value">
                            {renderRatingStars(avgRating)}
                        </div>
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
                {!isArrayEmpty && ([...comments].reverse().map((comment) => (
                    <div key={comment.id}
                        className={comment.isMyComment ? "my-comments" : "others-comments"}>
                        <p className={comment.isMyComment ? "my-username" : "creator-of-comment"}>
                            {comment.creator} 
                        </p>   
                        <p className='message'>{comment.message}</p>
                        <div className='rating-creating-date-group'>
                            <p className={comment.isMyComment ? "my-created-date" : "others-created-date"}>
                                {comment.createdAt}
                            </p>
                            <p className={renderRating(comment.rating, comment.isMyComment)}>
                                {ifRatingis0(comment.rating)}
                            </p>
                        </div>
                        
                    </div>
                )))}

                {isArrayEmpty && (
                    <p className='no-comment'>A játékleírás alá még nem küldtek megjegyzést!</p>
                )}    

            </div>
                       
        </form>
    );
}
