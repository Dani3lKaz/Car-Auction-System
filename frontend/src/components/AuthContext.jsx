import { useCallback, useMemo, useState } from "react";
import { AuthContext } from "./auth-context";

function getStoredSession() {
  const savedToken = localStorage.getItem("token");
  const savedUser = localStorage.getItem("user");

  if (!savedToken || !savedUser) {
    return { token: null, user: null };
  }

  try {
    return { token: savedToken, user: JSON.parse(savedUser) };
  } catch {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    return { token: null, user: null };
  }
}

export function AuthProvider({ children }) {
    const [session, setSession] = useState(getStoredSession);
    const { token, user } = session;
      
      const saveSession = useCallback((authResponse) => {
        const { token: newToken, user: newUser } = authResponse;
        setSession({ token: newToken, user: newUser });
        localStorage.setItem("token", newToken);
        localStorage.setItem("user", JSON.stringify(newUser));
      }, []);

      const login = useCallback(async (email, password) => {
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
      }, [saveSession]);

      const register = useCallback(async (firstName, lastName, email, password) => {
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
      }, [saveSession]);

      const logout = useCallback(() => {
        setSession({ token: null, user: null });
        localStorage.removeItem("token");
        localStorage.removeItem("user");
      }, []);

      const updateUser = useCallback((updatedUser, newToken = null) => {
        setSession((prev) => ({
          token: newToken || prev.token,
          user: updatedUser,
        }));
        localStorage.setItem("user", JSON.stringify(updatedUser));
        if (newToken) {
          localStorage.setItem("token", newToken);
        }
      }, []);

      const canCreateAuction =
        user?.role === "ADMIN" || user?.role === "SELLER";

      const value = useMemo(() => ({
        user,
        token,
        isLoading: false,
        isAuthenticated: !!token,
        isAdmin: user?.role === "ADMIN",
        isSeller: user?.role === "SELLER",
        canCreateAuction,
        login,
        register,
        logout,
        updateUser,
      }), [canCreateAuction, login, logout, register, token, updateUser, user]);

      return(<AuthContext.Provider value={value}>{children}</AuthContext.Provider>);
};
