import { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext(null);

export function AuthProvider({children}) {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const savedToken = localStorage.getItem("token");
        const savedUser = localStorage.getItem("user");
        if (savedToken && savedUser) {
          try {
            setToken(savedToken);
            setUser(JSON.parse(savedUser));
          } catch {
            localStorage.removeItem("token");
            localStorage.removeItem("user");
          }
        }
        setIsLoading(false);
      }, []);
      
      const saveSession = (authResponse) => {
        const { token: newToken, user: newUser } = authResponse;
        setToken(newToken);
        setUser(newUser);
        localStorage.setItem("token", newToken);
        localStorage.setItem("user", JSON.stringify(newUser));
      };

      const login = async (email, password) => {
        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password }),
        });

        if (!response.ok) {
            const err = await response.json().catch(() => ({}));
            throw new Error(err.errorCode || err.message || "LOGIN_FAILED");
        }
          const data = await response.json();
          saveSession(data);
          return data;
      }

      const register = async (firstName, lastName, email, password) => {
        const response = await fetch(`http://localhost:8080/api/auth/register`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ firstName, lastName, email, password }),
        });
        if (!response.ok) {
          const err = await response.json().catch(() => ({}));
          throw new Error(err.errorCode || err.message || "REGISTRATION_FAILED");
        }
        const data = await response.json();
        saveSession(data);
        return data;
      };

      const logout = () => {
        setToken(null);
        setUser(null);
        localStorage.removeItem("token");
        localStorage.removeItem("user");
      }

      const updateUser = (updatedUser, newToken = null) => {
        setUser(updatedUser);
        localStorage.setItem("user", JSON.stringify(updatedUser));
        if (newToken) {
          setToken(newToken);
          localStorage.setItem("token", newToken);
        }
      };

      const canCreateAuction =
        user?.role === "ADMIN" || user?.role === "SELLER";

      const value = {
        user,
        token,
        isLoading,
        isAuthenticated: !!token,
        isAdmin: user?.role === "ADMIN",
        isSeller: user?.role === "SELLER",
        canCreateAuction,
        login,
        register,
        logout,
        updateUser,
      }

      return(<AuthContext.Provider value={value}>{children}</AuthContext.Provider>);
};

export function useAuth() {
    return useContext(AuthContext)
}