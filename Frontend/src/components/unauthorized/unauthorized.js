import React from 'react'
import './unauthorized.css'
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTriangleExclamation } from '@fortawesome/free-solid-svg-icons';

export default function Unauthorized() {
  return (
    <div className='unauth'>
        <h1 className='unauth-title'>Hozzáférés megtagadva!</h1>
        <p className='unauth-message'>Ehhez az oldalhoz nincs hozzáférési engedélyed!</p>
        <FontAwesomeIcon className='triangle-icon' icon={faTriangleExclamation}></FontAwesomeIcon>
    </div>
  )
}
