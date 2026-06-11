import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Footer from "../components/Footer";
import Navbar from "../components/Navbar";
import CreateAuctionForm from "../components/CreateAuctionForm";
import { useAuth } from "../components/auth-context";

function CreateAuctionPage() {
  const { canCreateAuction, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated || !canCreateAuction) {
      navigate("/login");
    }
  }, [isAuthenticated, canCreateAuction, navigate]);

  if (!canCreateAuction) {
    return null;
  }

  return (
    <div className="d-flex flex-column min-vh-100 bg-body-tertiary">
      <Navbar />
      <main className="flex-grow-1 d-flex align-items-center justify-content-center py-5">
        <CreateAuctionForm />
      </main>
      <Footer />
    </div>
  );
}

export default CreateAuctionPage;
