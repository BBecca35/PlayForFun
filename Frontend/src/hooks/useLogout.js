import useAuth from "./useAuth";
import axiosInstance from "../api/axiosInstance";
import { useNavigate } from "react-router-dom";

const useLogout = () => {
    const { setPersist, setAuth } = useAuth();
    const accessToken = sessionStorage.getItem('accessToken');
    const navigate = useNavigate();
 
    const logoutUser = async () => {
        try{
            const response = await axiosInstance.post("/api/auth/logout", {} ,{
                headers: {
                    Authorization : `Bearer ${accessToken}`, 
                    Accept: 'application/json'
                },
                withCredentials: true
            });

        }catch(error){
            console.error(error)
        }
        setAuth({});
        setPersist(false);
        localStorage.clear();
        sessionStorage.clear();
        navigate("/login");
        
    };

    return logoutUser;
};

export default useLogout;