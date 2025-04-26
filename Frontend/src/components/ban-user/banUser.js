import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import useAuth from '../../hooks/useAuth';
import axiosInstance from '../../api/axiosInstance';
import './banUser.css';
import TimePicker from '../time-picker/timePicker';

export default function BanUser() {
  const location = useLocation();
  const navigate = useNavigate();
  const id = location.state?.id;
  const SAVE_BAN_URL = "/moderate-api/user/ban";
  const { auth } = useAuth();
  const [selectedBanDuration, setSelectedBanDuration] = useState("");
  const [selectedBanMode, setSelectedBanMode] = useState("");
  const [time, setTime] = useState({ day: 0, hour: 0, minute: 0 });
  const [isDisabled, setIsDisabled] = useState(true);
  const [isReasonSkipped, setIsReasonSkipped] = useState(false);
  const [banDuration, setBanDuration] = useState(0);
  const [reasonOfBan, setReasonOfBan] = useState("");

  const handleTimeChange = (type, value) => {
    setTime((prev) => {
      const updatedTime = { ...prev, [type]: value };
      const seconds = convertTimeToSeconds(updatedTime); 
      setBanDuration(seconds);
      return updatedTime;
    });
  };

  const convertTimeToSeconds = (time) => {
    const totalSeconds =
      Number(time.day) * 86400 + Number(time.hour) * 3600 + Number(time.minute) * 60;
    return totalSeconds;
  };

  const handleBanDurationChange = (event) => {
    const newDuration = event.target.value;
    setSelectedBanDuration(newDuration);

    if (newDuration !== "custom") {
      setBanDuration(Number(newDuration) * 60);
    }
    else{
      setBanDuration(0);
      setTime({ day: 0, hour: 0, minute: 0 });
    }
    
  };

  const handleBanModeChange = (event) => {
    const banMode = event.target.value;
    setSelectedBanMode(banMode);
    if(banMode === "permament"){
      setBanDuration(-1);
      setTime({ day: 0, hour: 0, minute: 0 });
    }
    else{
      setSelectedBanDuration("");
      setBanDuration(0);
    }
  };

  const toggleIsReasonSkipped = () => {
    setIsReasonSkipped(!isReasonSkipped); 
  }

  useEffect(() => {
    if(banDuration !== 0 && ((isReasonSkipped || reasonOfBan.trim()) && !(isReasonSkipped && reasonOfBan.trim())))
    {
      setIsDisabled(false);
    }
    else{
      setIsDisabled(true);
    }
  }, [banDuration, isReasonSkipped, reasonOfBan.trim()]);

  
  const handleBanSubmit = async () => {
    if(!reasonOfBan.trim() && !isReasonSkipped){
      alert("Indok megadása kötelező");
      return; 
    }

    if(reasonOfBan.trim() && isReasonSkipped){
      alert("Egyszere nem lehet nincs indok és az indok mező kitöltve");
      return;
    }

    if(banDuration === 0){
      alert("A kitiltás időtartama nem lehet nulla!");
    }

    try{
      await axiosInstance.post(SAVE_BAN_URL, 
        JSON.stringify({  
          userId: id,
          reason: isReasonSkipped ? "Nincs indok" : reasonOfBan,
          expirationTime: banDuration
        }),
        {
          headers: { Authorization: `Bearer ${auth.accessToken}`, 
                     Accept: 'application/json'
          },
          withCredentials: true
        }
      );
      navigate("/manage-user", {state: { id }});
      alert("Sikeres végrehajtás"); 
      
    }catch(error){
      if(error.response) {
        const { status, data } = error.response;
        if(status === 404){
          alert("A felhasználó nem található");
        }
        else{
          alert("Váratlan hiba történt a kitiltás végrehajtása során!");
          console.error(data.error);
        }
      }
      else{
        alert("Váratlan hiba!");
        console.error(error);
      }
    }
  }

  return (
    <div className='ban-user-main-container'>
      <div className='ban-user-header'>
        <h1 className='ban-user-title'>A felhasználó kitiltása</h1>
        <hr className='ban-user-title-line'/>
      </div>

      <div className='ban-user-content'>

        <div className='ban-user-option'>
          <p className='ban-mode-title'>Kitiltás módja</p>
          <label className='option1'>Ideiglenes
            <input type="radio" 
              name="radio-group1"
              value="temporary"
              checked={selectedBanMode === "temporary"}
              onChange={handleBanModeChange}
            />
            <span className='checkmark'></span>
          </label>
          <label className='option1'>Végleges
            <input type="radio" 
              name="radio-group1"
              value="permament"
              checked={selectedBanMode === "permament"}
              onChange={handleBanModeChange}
            />
            <span className='checkmark'></span>
          </label>
        </div>

        {selectedBanMode === "temporary" && (
          <div className='duration-of-ban-option'>
            <p className='duration-of-ban-title'>Kitiltás időtartama</p>
            <div className="duration-of-ban-content">
              <div className='ban-first-col'>
                <label className='option2'>15 perc
                  <input type="radio" 
                    name="radio-group2"
                    value="15"
                    checked={selectedBanDuration === "15"} 
                    onChange={handleBanDurationChange}
                  />
                  <span className='checkmark'></span>
                </label>

                <label className='option2'>30 perc
                  <input type="radio" 
                    name="radio-group2"
                    value="30"
                    checked={selectedBanDuration === "30"}
                    onChange={handleBanDurationChange} 
                  />
                  <span className='checkmark'></span>
                </label>

                <label className='option2'>1 óra
                  <input type="radio" 
                    name="radio-group2"
                    value="60"
                    checked={selectedBanDuration === "60"}
                    onChange={handleBanDurationChange}
                  />
                  <span className='checkmark'></span>
                </label>
              </div>

              <div className='ban-second-col'>
                <label className='option2'>24 óra
                  <input type="radio" 
                    name="radio-group2"
                    value="1440"
                    checked={selectedBanDuration === "1440"}
                    onChange={handleBanDurationChange}
                    />
                  <span className='checkmark'></span>
                </label>  

                <label className='option2'>7 nap
                  <input type="radio" 
                    name="radio-group2"
                    value="10080"
                    checked={selectedBanDuration === "10080"}
                    onChange={handleBanDurationChange}
                  />
                  <span className='checkmark'></span>
                </label>

                <label className='option2'>Egyéni
                  <input type="radio" 
                    name="radio-group2"
                    value="custom"
                    checked={selectedBanDuration === "custom"}
                    onChange={handleBanDurationChange}  
                  />
                  <span className='checkmark'></span>
                </label>
              </div>
              {selectedBanDuration === "custom" &&(
                <TimePicker day={time.day} hour={time.hour} minute={time.minute} onChange={handleTimeChange} />
              )}
            </div>
          </div>
        )}
      </div>
      <div className='ban-reason'>
        <p className='ban-reason-title'>A kitiltás megindoklása</p>
        <textarea className='ban-reason-textarea'
          type="text"
          value={reasonOfBan}
          placeholder='Indoklás hozzáfűzése'
          onChange={(e) => setReasonOfBan(e.target.value)}
        >
        </textarea>
        <div className='skip-reason-container'>
          <input name='skip-reason'
            type="checkbox" 
            id="skip-reason"
            checked={isReasonSkipped}
            onChange={toggleIsReasonSkipped} 
          />
          <label htmlFor="skip-reason" className="skip-reason-custom">Indoklás kihagyása</label>
          <button className='ban-submition'
            disabled={isDisabled}
            onClick={handleBanSubmit}
          >Végrehajtás
          </button>
        </div>
      </div>

    </div>
  )
}
