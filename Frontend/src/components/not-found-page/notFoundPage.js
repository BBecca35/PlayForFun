import React from 'react'
import './notFoundPage.css'
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBan } from '@fortawesome/free-solid-svg-icons';

export default function NotFoundPage() {
  return (
    <div className='not-found'>
        <h1 className='not-found-title'>404 | Nincs találat</h1>
        <p className='not-found-message'>Az elérni kívánt oldal nem található!</p>
        <FontAwesomeIcon className='ban-icon' icon={faBan}></FontAwesomeIcon>
    </div>
  )
}
