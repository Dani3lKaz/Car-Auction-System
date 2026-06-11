import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import AdminUsersList from "../components/AdminUsersList";
import { useAuth } from "../components/auth-context";

function UsersPage() {
  const { isAuthenticated, isAdmin, isLoading } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (isLoading) {
      return;
    }
    if (!isAuthenticated) {
      navigate("/login");
    } else if (!isAdmin) {
      navigate("/account");
    }
  }, [isLoading, isAuthenticated, isAdmin, navigate]);

  if (isLoading || !isAuthenticated || !isAdmin) {
    return null;
  }

  return (
    <div className="d-flex flex-column min-vh-100 bg-body-tertiary">
      <Navbar />
      <main className="flex-grow-1">
        <AdminUsersList />
      </main>
      <Footer />
    </div>
  );
}

export default UsersPage;
