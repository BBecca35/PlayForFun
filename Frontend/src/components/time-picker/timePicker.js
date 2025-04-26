import React from "react";
import "./timePicker.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faClock } from "@fortawesome/free-solid-svg-icons";

const TimePicker = ({ day, hour, minute, onChange }) => {
  return (
    <div className="time-picker">
      <FontAwesomeIcon icon={faClock} className="clock-icon" />
      <div className="time-inputs">
        <div className="time-input">
            <input
                type="number"
                value={day}
                min="0"
                max="1000"
                onChange={(e) => onChange("day", e.target.value)}
            />
            <label>Nap</label>
        </div>

        <div className="time-input">
            <input
                type="number"
                value={hour}
                min="0"
                max="23"
                onChange={(e) => onChange("hour", e.target.value)}
            />
            <label>Óra</label>
        </div>
        
        <div className="time-input">
            <input
                type="number"
                value={minute}
                min="0"
                max="59"
                onChange={(e) => onChange("minute", e.target.value)}
            />
            <label>Perc</label>
        </div>
      </div>
    </div>
  );
};

export default TimePicker;