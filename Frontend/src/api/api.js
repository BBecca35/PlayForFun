import axios from 'axios';
import { refreshAccessToken } from './services/authService';
import axiosInstance from './axios';

const API = axiosInstance;

API.interceptors.request.use((config) => {
    const token = localStorage.getItem('accessToken');
    const tokenType = localStorage.getItem('tokenType')
    if (token) {
        config.headers.Authorization = `${tokenType} ${token}`;
    }
    return config;
});

API.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            const refreshToken = localStorage.getItem('refreshToken');
            if (typeof refreshToken === 'string') {
                try {
                    const newAccessToken = await refreshAccessToken(refreshToken);
                    localStorage.setItem('accessToken', newAccessToken);
                    return axios(originalRequest);
                } catch (refreshError) {
                    console.error('Refresh token hibás:', refreshError);
                    localStorage.clear(); 
                }
            }
        }
        return Promise.reject(error);
    }
);

export default API;