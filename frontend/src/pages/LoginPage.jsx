import Footer from "../components/Footer";
import Navbar from "../components/Navbar";
import LoginForm from "../components/LoginForm";
import "../styles/loginpage.css";

function LoginPage() {
  return (
    <div className="login-page d-flex flex-column min-vh-100">
      <Navbar />
      <main className="login-page__main flex-grow-1 d-flex align-items-center justify-content-center py-4">
        <LoginForm />
      </main>
      <Footer />
    </div>
  );
}

export default LoginPage;