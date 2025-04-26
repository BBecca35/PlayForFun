import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import useAuth from '../../hooks/useAuth';
import axiosInstance from '../../api/axiosInstance';
import './editBan.css';
import TimePicker from '../time-picker/timePicker';
import Swal from 'sweetalert2';

export default function EditBan() {
  const location = useLocation();
  const navigate = useNavigate();
  const id = location.state?.id;
  const EDIT_BAN_URL = "/moderate-api/ban/update";
  const LOAD_BAN_URL = `/moderate-api/ban/${id}`;
  const UNDO_BAN_URL = `/moderate-api/ban/undo/${id}`;
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
    if (!time || typeof time !== "object") return 0;
    const totalSeconds =
      Number(time.day) * 86400 + Number(time.hour) * 3600 + Number(time.minute) * 60;
    return totalSeconds;
  };

  const convertSecondsToTime = (seconds) => {
    const day = Math.floor(seconds / 86400);
    const hour = Math.floor((seconds % 86400) / 3600);
    const minute = Math.floor((seconds % 3600) / 60);

    const duration = (day * 1440) + (hour * 60) + minute;
    if(duration !== 15 && 
        duration !== 30 &&
        duration !== 60 &&
        duration !== 1440 &&
        duration !== 10080
    ){
        setSelectedBanDuration("custom");
        setTime({ day: day, hour: hour, minute: minute });
    }
    else{
        setSelectedBanDuration(duration.toString());
        setBanDuration(duration);
    }
    
    
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

  useEffect(() => {
    const loadBan = async () => {
        try{
            const response = await axiosInstance.get(LOAD_BAN_URL, {
                headers: { Authorization: `Bearer ${auth.accessToken}`, 
                     Accept: 'application/json'
                },
                withCredentials: true
            })
            const data = response.data;
            setIsReasonSkipped(data.reason === "Nincs indok");

            if (data.reason !== "Nincs indok") {
                setReasonOfBan(data.reason);
            }
            
            if(data.banExpiration === -1){
                setSelectedBanMode("permament");
            }else{
                setSelectedBanMode("temporary");
                if(data.banExpiration !== 0){
                    convertSecondsToTime(data.banExpiration);
                    
                }
            }
            
        }
        catch(error){
            console.error(error);
        }
    }
    loadBan();
    
  }, [LOAD_BAN_URL, auth.accessToken]);

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
      await axiosInstance.put(EDIT_BAN_URL, 
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
      alert("A módosítás végrehajtás!");

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

  const showAlert = () => {
    Swal.fire({
        title: 'Kérlek, erősítse meg a visszavonást!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Visszavonás',
        cancelButtonText: 'Mégse'
    }).then(async (result) => {
        if (result.isConfirmed) {
            try {
              await handleUndoBan();
              navigate("/manage-user", {state: { id }});
              Swal.fire("Siker!", "A kitiltás visszavonásra került.", "success");
            } catch (error) {
              Swal.fire("Hiba!", "Nem sikerült visszavonni a kitiltást.", "error");
            }
          }
      });
  }

  const handleUndoBan = async () => {
    try{
        const response = await axiosInstance.put(UNDO_BAN_URL, {}, {
            headers: { Authorization: `Bearer ${auth.accessToken}`, 
                       Accept: 'application/json'
            },
            withCredentials: true
          }

        );
      }
      catch(error){
        console.error(error);
      }
  }

  

  return (
    <div className='ban-user-main-container'>
      <div className='ban-user-header'>
        <h1 className='ban-user-title'>Kitiltás módosítása</h1>
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
          <button className='undo-ban' 
            onClick={() => showAlert()}>Kitiltás visszavonása
          </button>
          <button className='edit-ban-submition'
            disabled={isDisabled}
            onClick={handleBanSubmit}
          >Módosítás mentése
          </button>
        </div>
      </div>

    </div>
  )
}
