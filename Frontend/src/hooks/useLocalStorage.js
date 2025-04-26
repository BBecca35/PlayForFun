import { useState, useEffect } from "react";

const retrieveLocalData = (key, defaultValue) => {
    if (typeof window === "undefined") return defaultValue;
    
    try {
        const storedValue = localStorage.getItem(key);
        return storedValue ? JSON.parse(storedValue) : (typeof defaultValue === "function" ? defaultValue() : defaultValue);
    } catch (error) {
        console.error("Error reading localStorage key ", key, error);
        return defaultValue;
    }
};

const usePersistentState = (key, defaultValue) => {
    const [storedValue, setStoredValue] = useState(() => {
        return retrieveLocalData(key, defaultValue);
    });

    useEffect(() => {
        try {
            localStorage.setItem(key, JSON.stringify(storedValue));
        } catch (error) {
            console.error("Error setting localStorage key ", key, error);
        }
    }, [key, storedValue]);

    return [storedValue, setStoredValue];
};

export default usePersistentState;