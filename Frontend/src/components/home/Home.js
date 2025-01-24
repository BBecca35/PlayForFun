import React from 'react';
import './Home.css';

export default function Home() {

    return (
        <div className="home-container">
            <div className="home-content">
                <input type="text" className="search-bar" placeholder="Keresés" />
                <p className="no-description">
                    Játékleírás még nem lett létrehozva egy felhasználó által sem!
                </p>
            </div>
        </div>
    );
}
