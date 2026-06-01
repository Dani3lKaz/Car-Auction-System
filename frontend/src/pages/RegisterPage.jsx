import Footer from "../components/Footer";
import Navbar from "../components/Navbar";
import RegisterForm from "../components/RegisterForm";
import "../styles/loginpage.css";

function RegisterPage() {
  return (
    <div className="login-page d-flex flex-column min-vh-100">
      <Navbar />
      <main className="login-page__main flex-grow-1 d-flex align-items-center justify-content-center py-4">
        <RegisterForm />
      </main>
      <Footer />
    </div>
  );
}

export default RegisterPage;