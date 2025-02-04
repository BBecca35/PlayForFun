import React from 'react';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axios';
import './Home.css';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFilter, faStar, faMagnifyingGlass, faXmark } from '@fortawesome/free-solid-svg-icons';

export default function Home() {
    const [cells, setCells] = useState([]);
    const FETCH_GAME_DESCRIPTIONS_URL = `/gd-api/gameDescriptions`; 
    const [IsArrayEmpty, setIsArrayEmpty] = useState(false);
    const [isFilterOpen, setIsFilterOpen] = useState(false); 
    const navigate = useNavigate();
    const [rating, setRating] = useState(null);
    const [hover, setHover] = useState(null);
    const [isMatching, setIsMatching] = useState(false);
    const [searchDescription, setSearchDescription] = useState('');
    const [notFoundDescription, setNotFoundDescription] = useState(false);
    const [resetTrigger, setResetTrigger] = useState(false);

    const currentYear = new Date().getFullYear();
    const [range, setRange] = useState({ min: 1952, max: currentYear });

    const [IsFilterStateEmpty, setIsFilterStateEmpty] = useState(false);
    const [isFiltering, setIsFiltering] = useState(false);

    const [filterState, setFilterState] = useState({
        genre: null,
        platform: null,
        ageLimit: null,
        minPublishedAt: 1952,
        maxPublishedAt: currentYear,
        avgRating: null,
    });
      
    const handleMinChange = (e) => {
        const min = Math.min(Number(e.target.value), range.max - 1); // Ne engedje átfedni a fogantyúkat
        setRange((prevRange) => ({ ...prevRange, min }));
        setFilterState({ ...filterState, minPublishedAt: min });
    };
      
    const handleMaxChange = (e) => {
        const max = Math.max(Number(e.target.value), range.min + 1); // Ne engedje átfedni a fogantyúkat
        setRange((prevRange) => ({ ...prevRange, max }));
        setFilterState({ ...filterState, maxPublishedAt: max });
    };
      
    const calculatePercent = (value) => {
        return ((value - 1952) / (currentYear - 1952)) * 100;
    };
      
    const minPercent = calculatePercent(range.min);
    const maxPercent = calculatePercent(range.max);

    
    useEffect(() => {
        if(!isFiltering && !notFoundDescription && !isMatching){
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
        }
    }, [FETCH_GAME_DESCRIPTIONS_URL, isFiltering, notFoundDescription, resetTrigger, isMatching]);

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

    const toggleFilter = () => {
        setIsFilterOpen(!isFilterOpen); // Dropdown megnyitása/zárása
    };

    const [selected1, setSelected1] = useState("-- Választás --");
    const [selected2, setSelected2] = useState("-- Választás --");
    const [isOpen1, setIsOpen1] = useState(false);
    const [isOpen2, setIsOpen2] = useState(false);

    const [selectedGenre, setSelectedGenre] = useState("-- Választás --");
    const [selectedPlatform, setSelectedPlatform] = useState("-- Választás --");
    const [selectedAgeLimit, setSelectedAgeLimit] = useState("-- Választás --");
    const [isOpenGenre, setIsOpenGenre] = useState(false);
    const [isOpenPlatform, setIsOpenPlatform] = useState(false);
    const [isOpenAgeLimit, setIsOpenAgeLimit] = useState(false);

    const options1 = [
        { value: "", label: "-- Választás --" },
        { value: "growing", label: "Növekvő" },
        { value: "decreasing", label: "Csökkenő" },
    ];

    const options2 = [
        { value: "", label: "-- Választás --" },
        { value: "growing", label: "Növekvő" },
        { value: "decreasing", label: "Csökkenő" },
    ];

    const handleSelect1 = (option) => {
        setSelected1(option.label);
        setIsOpen1(false);
    };

    const handleSelect2 = (option) => {
        setSelected2(option.label);
        setIsOpen2(false);
    };

    const genres = [
        { value: "", label: "-- Választás --" },
        { value: "akció", label: "Akció" },
        { value: "kaland", label: "Kaland" },
        { value: "harc", label: "Harc" },
        { value: "verseny", label: "Verseny" },
        { value: "RPG", label: "RPG" },
        { value: "szimuláció", label: "Szimuláció" },
        { value: "parlor", label: "Parlor" },
        { value: "MMO", label: "MMO" },
        { value: "egyéb", label: "Egyéb" },
    ];

    const platforms = [
        { value: "", label: "-- Választás --" },
        { value: "PC", label: "PC" },
        { value: "PC, IOS", label: "PC, IOS" },
        { value: "PC, Android, IOS", label: "PC, Android, IOS" },
        { value: "PC, IOS, XBOX", label: "PC, IOS, XBOX" },
        { value: "PC, IOS, PlayStation", label: "PC, IOS, PlayStation" },
        { value: "PC, PlayStation", label: "PC, PlayStation" },
        { value: "PC, XBOX", label: "PC, XBOX" },
        { value: "PC, XBOX, PlayStation", label: "PC, XBOX, PlayStation" },
        { value: "PC, Nintendo, XBOX, PlayStation", label: "PC, Nintendo, XBOX, PlayStation" },
        { value: "PC, Nintendo, XBOX, PlayStation, IOS", label: "PC, Nintendo, XBOX, PlayStation, IOS" },
        { value: "PC, PlayStation, XBOX, IOS, Andriod, Nintendo", label: "PC, PlayStation, XBOX, IOS, Andriod, Nintendo" },

    ];

    const ageLimits = [
        { value: "", label: "-- Választás --" },
        { value: "3", label: "PEGI 3" },
        { value: "7", label: "PEGI 7" },
        { value: "12", label: "PEGI 12" },
        { value: "16", label: "PEGI 16" },
        { value: "18", label: "PEGI 18" },
    ];

    const handleGenre = (option) => {
        setSelectedGenre(option.label);
        setFilterState({ ...filterState, genre: option.value });
        setIsOpenGenre(false);
    };

    const handlePlatform = (option) => {
        setSelectedPlatform(option.label);
        setFilterState({ ...filterState, platform: option.value });
        setIsOpenPlatform(false);
    };

    const handleAgeLimit = (option) => {
        setSelectedAgeLimit(option.label);
        setFilterState({ ...filterState, ageLimit: option.value });
        setIsOpenAgeLimit(false);
    };

    const handleRating = (rating) => {
        setRating(rating);
        setFilterState({ ...filterState, avgRating: rating});
    };

    const applyFilters = async () => {
        setIsFiltering(true);
        try {
            const filteredState = Object.fromEntries(
                Object.entries(filterState).filter(([_, value]) => (value !== null && value !== ""))
            );
            
            const response = await axiosInstance.post("/gd-api/gameDescriptions/filter", filteredState);
            const data = response.data.map((item) => ({
                id: item.id,
                title: item.name,
                imageUrl: `http://localhost:8080/api/images/${item.imageName}`,
            }));
            console.log(`${data}`);
            setCells(data);
            setIsArrayEmpty(false);
            setIsFilterStateEmpty(data.length === 0);
        } catch (error) {
            console.error("Hiba történt a szűrés során:", error);
            alert("Nem sikerült végrehajtani a szűrést. Próbáld újra később.");
            setIsArrayEmpty(true);
        }
    };

    const resetFilters = async () => {
        // Szűrőfeltételek visszaállítása alapértelmezett értékekre
        setFilterState({
            genre: null,
            platform: null,
            ageLimit: null,
            minPublishedAt: 1952,
            maxPublishedAt: new Date().getFullYear(),
            avgRating: null,
        });
    
        // UI elemek visszaállítása
        setSelectedGenre("-- Választás --");
        setSelectedPlatform("-- Választás --");
        setSelectedAgeLimit("-- Választás --");
        setHover(null);
        setRange({ min: 1952, max: new Date().getFullYear() });
        setRating(null);
        setIsFilterStateEmpty(false);
        setIsFiltering(false);   
    
        // Összes játékleírás újra lekérése
    };

    const applySorting = async () => {
        if((selected1 === "-- Választás --") !== (selected2 === "-- Választás --")){
            setIsFiltering(true);
            if(selected1 === "Növekvő"){
                try{
                    const response = await axiosInstance.get("/gd-api/gameDescriptions/sorted/ByUserNameAsc");
                    const data = response.data.map((item) => ({
                        id: item.id,
                        title: item.name,
                        imageUrl: `http://localhost:8080/api/images/${item.imageName}`,
                    }));
                    setCells(data);
                    setIsArrayEmpty(data.length === 0);
                }
                catch(error){
                    console.error("Hiba történt a játék leírások betöltésekor:", error);
                    alert("Nem sikerült betölteni a játékokat. Próbáld újra később.");
                }
            }

            if(selected1 === "Csökkenő"){
                try{
                    const response = await axiosInstance.get("/gd-api/gameDescriptions/sorted/ByUserNameDesc");
                    const data = response.data.map((item) => ({
                        id: item.id,
                        title: item.name,
                        imageUrl: `http://localhost:8080/api/images/${item.imageName}`,
                    }));
                    setCells(data);
                    setIsArrayEmpty(data.length === 0);
                }
                catch(error){
                    console.error("Hiba történt a játék leírások betöltésekor:", error);
                    alert("Nem sikerült betölteni a játékokat. Próbáld újra később.");
                }
            }

            if(selected2 === "Növekvő"){
                try{
                    const response = await axiosInstance.get("/gd-api/gameDescriptions/sorted/ByNameAsc");
                    const data = response.data.map((item) => ({
                        id: item.id,
                        title: item.name,
                        imageUrl: `http://localhost:8080/api/images/${item.imageName}`,
                    }));
                    setCells(data);
                    setIsArrayEmpty(data.length === 0);
                }
                catch(error){
                    console.error("Hiba történt a játék leírások betöltésekor:", error);
                    alert("Nem sikerült betölteni a játékokat. Próbáld újra később.");
                }
            }

            if(selected2 === "Csökkenő"){
                try{
                    const response = await axiosInstance.get("/gd-api/gameDescriptions/sorted/ByNameDesc");
                    const data = response.data.map((item) => ({
                        id: item.id,
                        title: item.name,
                        imageUrl: `http://localhost:8080/api/images/${item.imageName}`,
                    }));
                    setCells(data);
                    setIsArrayEmpty(data.length === 0);
                }
                catch(error){
                    console.error("Hiba történt a játék leírások betöltésekor:", error);
                    alert("Nem sikerült betölteni a játékokat. Próbáld újra később.");
                }
            }
            
        }else{
            alert("Egyszerre csak egy mezőnek lehet értéke és nem lehet mindkét mezőt értéktelenül hagyni.");
            return;
        }
    } 

    const resetSorting = async () => {
        setSelected1("-- Választás --");
        setSelected2("-- Választás --");
        setIsFilterStateEmpty(false);
        setIsFiltering(false);   
    
    };

    const handleSearching = async (e) => {
        e.preventDefault();
        setIsFiltering(true);
        if(searchDescription.trim()){
            
            try{
                const response = await axiosInstance.get(`/gd-api/name/${searchDescription}/gameDescription`);
                const data = Array.isArray(response.data)
                    ? response.data.map((item) => ({
                        id: item.id,
                        title: item.name,
                        imageUrl: `http://localhost:8080/api/images/${item.imageName}`,
                    }))
                    : [{
                        id: response.data.id,
                        title: response.data.name,
                        imageUrl: `http://localhost:8080/api/images/${response.data.imageName}`,
                    }];
                setCells(data);
                setIsArrayEmpty(data.length === 0);
                setIsArrayEmpty(false);
                setNotFoundDescription(false);
                setIsMatching(true);
                

            }catch(error){
                if(error.response){
                    const { status, data } = error.response;
                    if (status === 404 || status === 403) {
                        setIsMatching(true); 
                        setNotFoundDescription(true);
                        setIsFiltering(true); 
                    } else {
                        alert(`Ismeretlen hiba: ${status} - ${data.error}`);
                    }
                }
            }
        }else{
            setIsFiltering(false);
            setNotFoundDescription(false);
            setIsMatching(false);
            return;
        }
    }

    const resetSearching = () => {
        setSearchDescription('');
        setIsFiltering(false);
        setIsMatching(false);
        setNotFoundDescription(false);
        setResetTrigger(prev => !prev); 
    }

    return (
        <div className="home-container">
            <div className="home-content">
                <div className="search-bar-container">
                    <button className="search-icon" onClick={handleSearching}>
                        {<FontAwesomeIcon icon={faMagnifyingGlass} size="lg" style={{color: "#000000",}} />}  
                    </button>
                    <input type="text" 
                        name="search-bar" 
                        placeholder="Keresés"
                        value={searchDescription}
                        onChange={(e) => setSearchDescription(e.target.value)} />
                    
                    {isMatching && (
                        <button className="cross-icon" onClick={resetSearching}>
                            {<FontAwesomeIcon icon={faXmark} size="lg" style={{color: "#000000",}} />}  
                        </button>
                    )}
                    <button className="filter-icon" onClick={toggleFilter}>
                        {<FontAwesomeIcon icon={faFilter} size="lg" style={{color: "#000000",}} />}  
                    </button>

                </div>

                {isFilterOpen && (
                    <div className="filter-dropdown">
                        <h2 className='sorting-title'>Rendezés</h2>

                        <div className="first-row-select-container">

                            <div className="sorting-creator">
                                <p className='sorting-creator-text'>A leírás készítője alapján</p>

                                <div
                                    className={`sorting-creator-selected ${
                                        isOpen1 ? "sorting-creator-arrow-active" : ""
                                    }`}
                                    onClick={() => setIsOpen1(!isOpen1)}
                                >
                                    {selected1}
                                </div>
                                {isOpen1 && (
                                <div className="sorting-creator-items">
                                    {options1.map((option1) => (
                                        <div
                                            key={option1.value}
                                            onClick={() => handleSelect1(option1)}
                                            className={selected1 === option1.label ? "same-as-selected-creator" : ""}
                                        >
                                            {option1.label}
                                        </div>
                                    ))}
                                </div>
                                )}
                            </div>

      
                            <div className="sorting-game-title">
                                <p className='sorting-game-title-text'>A játék neve alapján</p>
                                <div
                                    className={`sorting-game-title-selected ${
                                        isOpen2 ? "sorting-game-title-arrow-active" : ""
                                    }`}
                                    onClick={() => setIsOpen2(!isOpen2)}
                                >
                                    {selected2}
                                </div>
                                {isOpen2 && (
                                    <div className="sorting-game-title-items">
                                        {options2.map((option2) => (
                                            <div
                                                key={option2.value}
                                                onClick={() => handleSelect2(option2)}
                                                className={
                                                selected2 === option2.label ? "same-as-selected-game-title" : ""}
                                            >
                                                {option2.label}
                                            </div>
                                        ))}
                                    </div>
                                )}
                            </div>
                        </div>

                        <div className='filter-buttons-container'>
                                <button className='submit-filtering-or-sorting' onClick={applySorting}>
                                    Indítás
                                </button>
                                <button className="reset-filters-button" onClick={resetSorting}>
                                    Visszavonás
                                </button>
                        </div>

                        <h2 className='filtering-title'>Szűrés</h2>
                        <div className="second-row-select-container">

                        <div className="filtering-genre">
                                <p className='filtering-genre-text'>Műfaj</p>
                                <div
                                    className={`filtering-genre-selected ${
                                        isOpenPlatform ? "filtering-genre-arrow-active" : ""
                                    }`}
                                    onClick={() => setIsOpenGenre(!isOpenGenre)}
                                >
                                    {selectedGenre}
                                </div>
                                {isOpenGenre && (
                                    <div className="filtering-genre-items">
                                        {genres.map((option3) => (
                                            <div
                                                key={option3.value}
                                                onClick={() => handleGenre(option3)}
                                                className={
                                                selectedGenre === option3.label ? "same-as-selected-genre" : ""}
                                            >
                                                {option3.label}
                                            </div>
                                        ))}
                                    </div>
                                )}
                            </div>

      
                            <div className="filtering-platform">
                                <p className='filtering-platform-text'>Platform</p>
                                <div
                                    className={`filtering-platform-selected ${
                                        isOpenPlatform ? "filtering-platform-arrow-active" : ""
                                    }`}
                                    onClick={() => setIsOpenPlatform(!isOpenPlatform)}
                                >
                                    {selectedPlatform}
                                </div>
                                {isOpenPlatform && (
                                    <div className="filtering-platform-items">
                                        {platforms.map((option4) => (
                                            <div
                                                key={option4.value}
                                                onClick={() => handlePlatform(option4)}
                                                className={
                                                selectedPlatform === option4.label ? "same-as-selected-platform" : ""}
                                            >
                                                {option4.label}
                                            </div>
                                        ))}
                                    </div>
                                )}
                            </div>
                        </div>
                        <div className='filtering-rating-container'>
                            <p className='filtering-rating-text'>Értékelés: </p>
                            <div className="filtering-rating" id="filtering-rating">
                                {[...Array(5)].map((star,index) => {
                                    const currentRating = index + 1;
                                    return(
                                        <label key={index}>
                                            <input type="radio" 
                                                name="filtering-rating"
                                                value={currentRating}
                                                onClick={() => handleRating(currentRating)}

                                            />
                                            <FontAwesomeIcon className='filtering-rating-icon' 
                                                icon={faStar} 
                                                size="2x"
                                                color={currentRating > (hover|| rating) ? "#4FC4F3" : "yellow"}
                                                onMouseEnter={() => setHover(currentRating)}
                                                onMouseLeave={() => setHover(null)}
                                            />
                                        </label>
                                    );
                                })}
                            </div>
                        </div>
                        <div className='filtering-publication-year-age-limit-row'>
                            <div className="filtering-publication-year-container"
                                style={{
                                "--min-percent": `${minPercent}%`,
                                "--max-percent": `${maxPercent}%`,
                            }}
                            >
                                <p className='filtering-publication-year-text'>Kiadás éve</p>
                                <div className="range-values">
                                    <span>{range.min}</span>
                                    <span>{range.max}</span>
                                </div>
                                <div className="range-slider">
                                    <input
                                        type="range"
                                        min="1952"
                                        max={currentYear}
                                        value={range.min}
                                        onChange={handleMinChange}
                                        className="range-input"
                                    />
                                    <input
                                        type="range"
                                        min="1952"
                                        max={currentYear}
                                        value={range.max}
                                        onChange={handleMaxChange}
                                        className="range-input"
                                    />
                                </div>
                            </div>

                            <div className="filtering-age-limit">
                                <p className='filtering-age-limit-text'>Korhatár</p>
                                <div
                                    className={`filtering-age-limit-selected ${
                                        isOpenAgeLimit ? "filtering-age-limit-arrow-active" : ""
                                    }`}
                                    onClick={() => setIsOpenAgeLimit(!isOpenAgeLimit)}
                                >
                                    {selectedAgeLimit}
                                </div>
                                {isOpenAgeLimit && (
                                    <div className="filtering-age-limit-items">
                                        {ageLimits.map((option5) => (
                                            <div
                                                key={option5.value}
                                                onClick={() => handleAgeLimit(option5)}
                                                className={
                                                selectedAgeLimit === option5.label ? "same-as-selected-age-limit" : ""}
                                            >
                                                {option5.label}
                                            </div>
                                        ))}
                                    </div>
                                )}
                            </div>

                        </div>
                        <div className='filter-buttons-container'>
                            <button className='submit-filtering-or-sorting' onClick={applyFilters}>
                                Indítás
                            </button>
                            <button className="reset-filters-button" onClick={resetFilters}>
                                Visszavonás
                            </button>
                        </div>
                        
                    </div>

                    
                )}

                {IsArrayEmpty && !isFiltering && (
                    <p className="no-description">
                        Játékleírás még nem lett létrehozva egy felhasználó által sem!
                    </p>
                )}

                {IsFilterStateEmpty && isFiltering &&(
                    <p className="empty-result">
                        Nincs szűrési találat!
                    </p>
                )}

                {notFoundDescription && isFiltering &&(
                    <p className="description-not-found">
                        Nincs kerési találat találat!
                    </p>
                )}
                
                {(!IsArrayEmpty || isFiltering) && !notFoundDescription && !isMatching && (
                    <div className="home-dynamic-table">
                    {cells.map((cell) => (
                        <div className="home-gd-container" key={cell.id} onClick={() => handleEdit(cell.id)}>
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
