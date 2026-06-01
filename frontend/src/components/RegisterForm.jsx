import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import { useAuth } from "./AuthContext";

function RegisterForm() {
    const { register } = useAuth();
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);

    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleShowPassword = () => {
        setShowPassword(!showPassword);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
          await register(formData.firstName, formData.lastName, formData.email, formData.password);
          navigate("/");
        } catch (error) {
          console.error(error.message);
        }
    };

  return (
    <>
      <div className="login-form-wrapper w-100 px-3">
        <div className="card login-card shadow border-0 mx-auto">
          <div className="card-body p-4 p-md-5">
            <h1 className="h4 mb-1 fw-bold">Zarejestruj się</h1>
            <p className="text-muted small mb-4">Dołącz do społeczności MotoTrade</p>
            <form onSubmit={handleSubmit}>
            <div className="mb-3">
                <label htmlFor="firstName" className="form-label">
                  Imię
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="firstName"
                  name="firstName"
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="lastName" className="form-label">
                  Nazwisko
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="lastName"
                  name="lastName"
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="email" className="form-label">
                  Adres e-mail
                </label>
                <input
                  type="email"
                  className="form-control"
                  id="email"
                  name="email"
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="password" className="form-label">
                  Hasło
                </label>
                <input
                  type={showPassword ? "text" : "password"}
                  className="form-control"
                  id="password"
                  name="password"
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="passwordConfirmation" className="form-label">
                  Powtórz hasło
                </label>
                <input
                  type={showPassword ? "text" : "password"}
                  className="form-control"
                  id="passwordConfirmation"
                  required
                />
              </div>
              <div className="mb-3 form-check">
                <input
                  type="checkbox"
                  className="form-check-input"
                  id="showPassword"
                  checked={showPassword}
                  onChange={handleShowPassword}
                />
                <label className="form-check-label" htmlFor="showPassword">
                  Pokaż hasło
                </label>
              </div>
              <div className="d-flex gap-2">
                <button type="submit" className="btn btn-primary">Zarejestruj się</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </>
  );
}

export default RegisterForm;
