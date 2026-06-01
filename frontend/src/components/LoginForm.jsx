import { useState } from "react";
import { useAuth } from "./AuthContext";
import { useNavigate } from "react-router-dom";

function LoginForm() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);

  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await login(formData.email, formData.password);
      navigate("/");
    } catch (error) {
      console.error(error.message);
    }
  };

  const handleShowPassword = () => {
    setShowPassword(!showPassword);
  };

  return (
    <>
      <div className="login-form-wrapper w-100 px-3">
        <div className="card login-card shadow border-0 mx-auto">
          <div className="card-body p-4 p-md-5">
            <h1 className="h4 mb-1 fw-bold">Zaloguj się</h1>
            <p className="text-muted small mb-4">Witaj ponownie w MotoTrade</p>
            <form onSubmit={handleSubmit}>
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
              <div className="mb-3 form-check">
                <input
                  type="checkbox"
                  className="form-check-input"
                  id="exampleCheck1"
                  checked={showPassword}
                  onChange={handleShowPassword}
                />
                <label className="form-check-label" htmlFor="exampleCheck1">
                  Pokaż hasło
                </label>
              </div>
              <button type="submit" className="btn btn-primary">
                Zaloguj się
              </button>
            </form>
          </div>
        </div>
      </div>
    </>
  );
}

export default LoginForm;
