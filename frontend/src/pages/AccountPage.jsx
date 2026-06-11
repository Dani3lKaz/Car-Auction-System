import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import AccountInfo from "../components/AccountInfo";
import { useAuth } from "../components/auth-context";

function AccountPage() {
  const { isAuthenticated, isLoading } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      navigate("/login");
    }
  }, [isLoading, isAuthenticated, navigate]);

  if (isLoading || !isAuthenticated) {
    return null;
  }

  return (
    <div className="d-flex flex-column min-vh-100 bg-body-tertiary">
      <Navbar />
      <main className="flex-grow-1 d-flex align-items-center justify-content-center py-5">
        <AccountInfo />
      </main>
      <Footer />
    </div>
  );
}

export default AccountPage;
