import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import { AuthProvider } from "./components/AuthContext";
import AuctionListPage from "./pages/AuctionListPage";
import CreateAuctionPage from "./pages/CreateAuctionPage";
import AccountPage from "./pages/AccountPage";

function App() {
  return (
    <Router>
      <AuthProvider>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/auctions" element={<AuctionListPage />} />
          <Route path="/auctions/new" element={<CreateAuctionPage />} />
          <Route path='/account' element={<AccountPage/>}/>
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;
