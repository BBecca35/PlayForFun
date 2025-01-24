import React, { useState } from 'react';
import '../MyGameDescriptions/MyGameDescriptions.css';

export default function MyGameDescriptions() {
    const [cells, setCells] = useState([]);

    const addCell = () => {
        const newCell = {
          id: cells.length + 1,
          title: `Cím ${cells.length + 1}`,
          imageUrl: 'https://via.placeholder.com/100', // Helykitöltő kép
        };
        setCells([...cells, newCell]);
      };
    
    return (
        <div className="my-game-decriptions-container">
            
            <div className="home-content">
                <p className="my-description-text">
                    Játékleírásaim
                </p> <hr className='line'/>

            <div className="dynamic-table">
                {cells.map((cell) => (
                <div key={cell.id} className="table-cell">
                    <img src={cell.imageUrl} alt={`Cell ${cell.id}`} />
                    <p className='cell'>{cell.title}</p>
                </div>
                ))}
                <div className="table-cell add-cell" onClick={addCell}>
                    <span>+</span>
                    <p className='new-text'>Új leírás létrehozása</p>
                </div>
                
            </div>
            </div>

        </div>
    );
}