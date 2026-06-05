import { useEffect, useState } from "react";
import { useAuth } from "./AuthContext";

const API_BASE = "http://localhost:8080";
const PASSWORD_REGEX = /^(?=.*[A-Z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,}$/;
const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

const roleLabels = {
  USER: "Użytkownik",
  ADMIN: "Administrator",
  SELLER: "Sprzedawca",
};

function formatBalance(value) {
  if (value == null) return "—";
  return `${Number(value).toLocaleString("pl-PL")} PLN`;
}

function AccountInfo() {
  const { user, token, isAuthenticated, updateUser } = useAuth();
  const [isEditing, setIsEditing] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    currentPassword: "",
    newPassword: "",
    newPasswordConfirmation: "",
  });

  const syncFormFromUser = (accountUser) => {
    setFormData((prev) => ({
      ...prev,
      firstName: accountUser.firstName || "",
      lastName: accountUser.lastName || "",
      email: accountUser.email || "",
    }));
  };

  useEffect(() => {
    if (!token || !isAuthenticated) {
      setLoading(false);
      return;
    }

    const fetchAccount = async () => {
      setLoading(true);
      setErrorMessage(null);

      try {
        const response = await fetch(`${API_BASE}/api/account`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!response.ok) {
          const err = await response.json().catch(() => ({}));
          throw new Error(err.message || "Nie udało się pobrać danych konta");
        }

        const accountUser = await response.json();
        updateUser(accountUser);
        syncFormFromUser(accountUser);
      } catch (err) {
        setErrorMessage(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchAccount();
  }, [token, isAuthenticated]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setErrorMessage(null);
    setSuccessMessage(null);
  };

  const handleEditState = () => {
    setIsEditing(true);
    setErrorMessage(null);
    setSuccessMessage(null);
    syncFormFromUser(user);
  };

  const handleCancel = () => {
    setIsEditing(false);
    setFormData({
      firstName: user?.firstName || "",
      lastName: user?.lastName || "",
      email: user?.email || "",
      currentPassword: "",
      newPassword: "",
      newPasswordConfirmation: "",
    });
    setErrorMessage(null);
    setSuccessMessage(null);
  };

  const validateProfile = () => {
    if (!formData.firstName.trim() || !formData.lastName.trim() || !formData.email.trim()) {
      setErrorMessage("Imię, nazwisko i e-mail są wymagane.");
      return false;
    }

    if (!EMAIL_REGEX.test(formData.email.trim())) {
      setErrorMessage("Nieprawidłowy format adresu e-mail.");
      return false;
    }

    return true;
  };

  const validatePasswordChange = () => {
    const { newPassword, newPasswordConfirmation, currentPassword } = formData;
    const wantsPasswordChange = newPassword.length > 0 || newPasswordConfirmation.length > 0;

    if (!wantsPasswordChange) {
      return true;
    }

    if (!currentPassword) {
      setErrorMessage("Podaj obecne hasło, aby je zmienić.");
      return false;
    }

    if (!PASSWORD_REGEX.test(newPassword)) {
      setErrorMessage(
        "Hasło musi mieć min. 8 znaków, min. 1 dużą literę i min. 1 cyfrę."
      );
      return false;
    }

    if (newPassword !== newPasswordConfirmation) {
      setErrorMessage("Nowe hasła nie są identyczne.");
      return false;
    }

    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setErrorMessage(null);
    setSuccessMessage(null);

    if (!validateProfile() || !validatePasswordChange()) {
      setSubmitting(false);
      return;
    }

    try {
      const payload = {
        firstName: formData.firstName.trim(),
        lastName: formData.lastName.trim(),
        email: formData.email.trim(),
      };

      if (formData.newPassword) {
        payload.currentPassword = formData.currentPassword;
        payload.newPassword = formData.newPassword;
      }

      const response = await fetch(`${API_BASE}/api/account`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const err = await response.json().catch(() => ({}));
        throw new Error(err.message || "Nie udało się zapisać danych");
      }

      const data = await response.json();
      updateUser(data.user, data.token || null);
      syncFormFromUser(data.user);
      setFormData((prev) => ({
        ...prev,
        currentPassword: "",
        newPassword: "",
        newPasswordConfirmation: "",
      }));
      setIsEditing(false);
      if (data.token) {
        setSuccessMessage("E-mail został zmieniony. Sesja została odświeżona.");
      } else {
        setSuccessMessage("Dane konta zostały zapisane.");
      }
    } catch (err) {
      setErrorMessage(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  if (!isAuthenticated || !user) {
    return (
      <div className="text-center text-secondary">
        Zaloguj się, aby zobaczyć dane konta.
      </div>
    );
  }

  if (loading) {
    return (
      <div className="text-center text-secondary py-5">
        Ładowanie danych konta…
      </div>
    );
  }

  return (
    <div className="w-100 px-3">
      <div className="card border-0 shadow mx-auto" style={{ maxWidth: "720px" }}>
        <div className="card-body p-4 p-md-5">
          {successMessage && !isEditing && (
            <div className="alert alert-success" role="alert">
              {successMessage}
            </div>
          )}

          {errorMessage && !isEditing && (
            <div className="alert alert-danger" role="alert">
              {errorMessage}
            </div>
          )}

          {isEditing ? (
            <>
              <h1 className="h4 fw-bold mb-4">Edytuj dane</h1>

              {errorMessage && (
                <div className="alert alert-danger" role="alert">
                  {errorMessage}
                </div>
              )}

              <form onSubmit={handleSubmit} noValidate>
                <div className="mb-3">
                  <label htmlFor="firstName" className="form-label">
                    Imię
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id="firstName"
                    name="firstName"
                    value={formData.firstName}
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
                    value={formData.lastName}
                    onChange={handleChange}
                    required
                  />
                </div>

                <div className="mb-3">
                  <label htmlFor="email" className="form-label">
                    E-mail
                  </label>
                  <input
                    type="email"
                    className="form-control"
                    id="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    required
                  />
                </div>

                <h2 className="h6 fw-semibold mb-3">Zmiana hasła (opcjonalnie)</h2>

                <div className="mb-3">
                  <label htmlFor="currentPassword" className="form-label">
                    Obecne hasło
                  </label>
                  <input
                    type="password"
                    className="form-control"
                    id="currentPassword"
                    name="currentPassword"
                    value={formData.currentPassword}
                    onChange={handleChange}
                    autoComplete="current-password"
                  />
                </div>

                <div className="mb-3">
                  <label htmlFor="newPassword" className="form-label">
                    Nowe hasło
                  </label>
                  <input
                    type="password"
                    className="form-control"
                    id="newPassword"
                    name="newPassword"
                    value={formData.newPassword}
                    onChange={handleChange}
                    autoComplete="new-password"
                  />
                  <div className="form-text">
                    Min. 8 znaków, 1 duża litera i 1 cyfra.
                  </div>
                </div>

                <div className="mb-4">
                  <label htmlFor="newPasswordConfirmation" className="form-label">
                    Powtórz nowe hasło
                  </label>
                  <input
                    type="password"
                    className="form-control"
                    id="newPasswordConfirmation"
                    name="newPasswordConfirmation"
                    value={formData.newPasswordConfirmation}
                    onChange={handleChange}
                    autoComplete="new-password"
                  />
                </div>

                <div className="d-flex flex-wrap gap-2">
                  <button
                    type="submit"
                    className="btn btn-primary"
                    disabled={submitting}
                  >
                    {submitting ? "Zapisywanie…" : "Zapisz"}
                  </button>
                  <button
                    type="button"
                    className="btn btn-outline-secondary"
                    onClick={handleCancel}
                    disabled={submitting}
                  >
                    Anuluj
                  </button>
                </div>
              </form>
            </>
          ) : (
            <>
              <h1 className="h4 fw-bold mb-4">Dane konta</h1>

              <div className="mb-2">
                <strong>Imię:</strong> {user.firstName}
              </div>
              <div className="mb-2">
                <strong>Nazwisko:</strong> {user.lastName}
              </div>
              <div className="mb-2">
                <strong>E-mail:</strong> {user.email}
              </div>
              <div className="mb-2">
                <strong>Saldo:</strong> {formatBalance(user.balance)}
              </div>
              <div className="mb-4">
                <strong>Rola:</strong> {roleLabels[user.role] || user.role}
              </div>

              <button className="btn btn-primary" onClick={handleEditState}>
                Edytuj
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
}

export default AccountInfo;
