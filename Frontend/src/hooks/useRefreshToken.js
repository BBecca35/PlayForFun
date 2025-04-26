import axiosInstance from "../api/axiosInstance";
import useAuth from "./useAuth";
import useLogout from "./useLogout";
import { jwtDecode } from "jwt-decode";

const useRefreshToken = () => {
    const { setAuth } = useAuth();
    const logoutUser = useLogout();
    const accessToken = sessionStorage.getItem('accessToken');

    const refresh = async () => {
        try {
            const response = await axiosInstance.post("/api/auth/refresh", {},{
                headers: { 
                    Authorization : `Bearer ${accessToken}`, 
                    Accept: 'application/json' 
            },
                withCredentials: true,
            });

            const newAccessToken = response.headers['authorization'];
            const decodedToken = jwtDecode(newAccessToken);
            sessionStorage.setItem("accessToken", newAccessToken);
            setAuth(prev => {
                return {
                    ...prev,
                    user: decodedToken.sub,
                    userId: decodedToken.userId,
                    roles: [decodedToken.role],
                    accessToken: newAccessToken
                }
            });
            return newAccessToken;
        } catch (error) {
            console.error(error);
            logoutUser();
            return null;
            
        }
    };

    return refresh;
};

export default useRefreshToken;