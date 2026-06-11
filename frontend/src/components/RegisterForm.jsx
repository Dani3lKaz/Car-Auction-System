import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "./auth-context";
import Alert from "./Alert";

function RegisterForm() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [errorMessage, setErrorMessage] = useState(null);
  const [invalidFields, setInvalidFields] = useState({});
  const [showPassword, setShowPassword] = useState(false);

  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    passwordConfirmation: "",
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });

    if (invalidFields[e.target.name]) {
      setInvalidFields({ ...invalidFields, [e.target.name]: false });
    }
  };

  const handleShowPassword = () => {
    setShowPassword(!showPassword);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    let errors = {};

    if (!formData.firstName) errors.firstName = true;
    if (!formData.lastName) errors.lastName = true;
    if (!formData.email) errors.email = true;
    if (!formData.password) errors.password = true;
    if (!formData.passwordConfirmation) errors.passwordConfirmation = true;

    if (Object.keys(errors).length > 0) {
      setInvalidFields(errors);
      setErrorMessage("REQUIRED_FIELDS_MISSING");
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if(!emailRegex.test(formData.email)) {
      setInvalidFields({email: true});
      setErrorMessage("INVALID_EMAIL_FORMAT");
      return;
    }

    const passwordRegex = /^(?=.*[A-Z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,}$/;
    if(!passwordRegex.test(formData.password)){
      setInvalidFields({password: true});
      setErrorMessage("WEAK_PASSWORD");
      return;
    }

    if(formData.password !== formData.passwordConfirmation) {
      setInvalidFields({password: true, passwordConfirmation: true});
      setErrorMessage("PASSWORDS_DO_NOT_MATCH");
      return;
    }

    try {
      await register(
        formData.firstName,
        formData.lastName,
        formData.email,
        formData.password,
      );
      setErrorMessage(null);
      navigate("/");
    } catch (error) {
      console.error(error.message);
      if (error.message === "EMAIL_ALREADY_TAKEN") {
        setInvalidFields({ email: true });
      }
      setErrorMessage(error.message);
    }
  };

  return (
    <>
      <div className="login-form-wrapper w-100 px-3">
        <div className="card login-card shadow border-0 mx-auto">
          <div className="m-3">
            <Alert message={errorMessage} />
          </div>
          <div className="card-body p-4 p-md-5">
            <h1 className="h4 mb-1 fw-bold">Zarejestruj się</h1>
            <p className="text-muted small mb-4">
              Dołącz do społeczności MotoTrade
            </p>
            <form onSubmit={handleSubmit} noValidate>
              <div className="mb-3">
                <label htmlFor="firstName" className="form-label">
                  Imię
                </label>
                <input
                  type="text"
                  className={`form-control ${invalidFields.firstName ? "is-invalid" : ""}`}
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
                  className={`form-control ${invalidFields.lastName ? "is-invalid" : ""}`}
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
                  className={`form-control ${invalidFields.email ? "is-invalid" : ""}`}
                  id="email"
                  name="email"
                  onChange={handleChange}
                  required
                />
              </div>
              <div>
                <label htmlFor="password" className="form-label">
                  Hasło
                </label>
                <input
                  type={showPassword ? "text" : "password"}
                  className={`form-control ${invalidFields.password ? "is-invalid" : ""}`}
                  id="password"
                  name="password"
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="col-auto mb-3">
                <span id="passwordHelpInline" className="form-text">
                  Hasło musi mieć długość min. 8 znaków, min. 1 dużą literę,
                  min. 1 cyfrę
                </span>
              </div>
              <div className="mb-3">
                <label htmlFor="passwordConfirmation" className="form-label">
                  Powtórz hasło
                </label>
                <input
                  type={showPassword ? "text" : "password"}
                  className={`form-control ${invalidFields.passwordConfirmation ? "is-invalid" : ""}`}
                  id="passwordConfirmation"
                  name="passwordConfirmation"
                  onChange={handleChange}
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
                <button type="submit" className="btn btn-primary">
                  Zarejestruj się
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </>
  );
}

export default RegisterForm;
