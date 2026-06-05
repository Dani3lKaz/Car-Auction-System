import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import AccountInfo from "../components/AccountInfo";
import { useAuth } from "../components/AuthContext";

function AccountPage() {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate("/login");
    }
  }, [isAuthenticated, navigate]);

  if (!isAuthenticated) {
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
