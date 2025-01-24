import axiosInstance from '../api/axios';

const REFRESH_URL = 'api/auth/refresh';

export const refreshAccessToken = async (refreshToken) => {
    try {
        const response = await axiosInstance.post(REFRESH_URL, {
            refreshToken
        });
        const { accessToken } = response.data;
        return accessToken;
    } catch (error) {
        console.error('Nem sikerült a token frissítése:', error);
        throw error; 
    }
};
