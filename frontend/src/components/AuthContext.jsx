import { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext(null);

export function AuthProvider({children}) {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(null);

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
            throw new Error(err.message || "Login failed");
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
          throw new Error(err.message || "Registration failed");
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

      const value = {
        user,
        token,
        isAuthenticated: !!token,
        isAdmin: user?.role === "ADMIN",
        login,
        register,
        logout,
      }

      return(<AuthContext.Provider value={value}>{children}</AuthContext.Provider>);
};

export function useAuth() {
    return useContext(AuthContext)
}