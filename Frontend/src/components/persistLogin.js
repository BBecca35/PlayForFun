import { Outlet } from "react-router-dom";
import { useEffect, useState } from "react";
import useRefreshToken from "../hooks/useRefreshToken";
import useAuth from '../hooks/useAuth';

const PersistLogin = () => {
  const [isLoading, setIsLoading] = useState(true);
  const refresh = useRefreshToken();
  const { auth, persist } = useAuth();

  useEffect(() => {
    let isMounted = true;

    const verifyRefreshToken = async () => {
      try {
        await refresh();
        
      } catch (error) {
        if (error.response) {
          const { status } = error.response;
          if (status === 401) {
            alert("A munkamenet lejárt! Kérem jelentkezzen be újból!");
          } else {
            console.error("A token frissítése közben hiba történt", error);
          }
        } else {
          console.error("Váratlan hiba:", error);
        }
      } finally {
        isMounted && setIsLoading(false);
      }
    };

    !auth?.accessToken ? verifyRefreshToken() : setIsLoading(false);

    return () => (isMounted = false);
  }, []);

  return (
    <>
        {!persist
            ? <Outlet />
            : isLoading
            ? <p>Loading...</p>
            : <Outlet />
        }
    </>
  );
};

export default PersistLogin;