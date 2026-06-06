import { useEffect, useState } from "react";
import { useAuth } from "./AuthContext";

const API_BASE = "http://localhost:8080";
const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

const ROLES = ["USER", "SELLER", "ADMIN"];
const roleLabels = {
  USER: "Użytkownik",
  ADMIN: "Administrator",
  SELLER: "Sprzedawca",
};

function formatBalance(value) {
  if (value == null) return "—";
  return `${Number(value).toLocaleString("pl-PL")} PLN`;
}

const emptyForm = {
  firstName: "",
  lastName: "",
  email: "",
  role: "USER",
  balance: "",
};

function AdminUsersList() {
  const { token, user: currentUser } = useAuth();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState(emptyForm);
  const [submitting, setSubmitting] = useState(false);
  const [deletingId, setDeletingId] = useState(null);

  const fetchUsers = async () => {
    setLoading(true);
    setErrorMessage(null);
    try {
      const response = await fetch(`${API_BASE}/api/users`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!response.ok) {
        const err = await response.json().catch(() => ({}));
        throw new Error(err.message || "Nie udało się pobrać użytkowników");
      }
      const data = await response.json();
      setUsers(data);
    } catch (err) {
      setErrorMessage(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (token) {
      fetchUsers();
    }
  }, [token]);

  const handleEdit = (account) => {
    setEditingId(account.id);
    setErrorMessage(null);
    setSuccessMessage(null);
    setFormData({
      firstName: account.firstName || "",
      lastName: account.lastName || "",
      email: account.email || "",
      role: account.role || "USER",
      balance: account.balance != null ? String(account.balance) : "",
    });
  };

  const handleCancel = () => {
    setEditingId(null);
    setFormData(emptyForm);
    setErrorMessage(null);
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const validate = () => {
    if (!formData.firstName.trim() || !formData.lastName.trim() || !formData.email.trim()) {
      setErrorMessage("Imię, nazwisko i e-mail są wymagane.");
      return false;
    }
    if (!EMAIL_REGEX.test(formData.email.trim())) {
      setErrorMessage("Nieprawidłowy format adresu e-mail.");
      return false;
    }
    if (formData.balance !== "" && Number.isNaN(Number(formData.balance))) {
      setErrorMessage("Saldo musi być liczbą.");
      return false;
    }
    return true;
  };

  const handleSave = async (account) => {
    setErrorMessage(null);
    setSuccessMessage(null);
    if (!validate()) {
      return;
    }

    setSubmitting(true);
    try {
      const payload = {
        id: account.id,
        firstName: formData.firstName.trim(),
        lastName: formData.lastName.trim(),
        email: formData.email.trim(),
        role: formData.role,
        balance: formData.balance === "" ? null : Number(formData.balance),
        version: account.version,
      };

      const response = await fetch(`${API_BASE}/api/users`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const err = await response.json().catch(() => ({}));
        throw new Error(err.message || "Nie udało się zapisać zmian");
      }

      const updated = await response.json();
      setUsers((prev) => prev.map((u) => (u.id === updated.id ? updated : u)));
      setEditingId(null);
      setFormData(emptyForm);
      setSuccessMessage("Dane użytkownika zostały zapisane.");
    } catch (err) {
      setErrorMessage(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (account) => {
    setErrorMessage(null);
    setSuccessMessage(null);
    const confirmed = window.confirm(
      `Czy na pewno chcesz usunąć użytkownika ${account.firstName} ${account.lastName}?`
    );
    if (!confirmed) {
      return;
    }

    setDeletingId(account.id);
    try {
      const response = await fetch(`${API_BASE}/api/users/${account.id}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!response.ok) {
        const err = await response.json().catch(() => ({}));
        throw new Error(err.message || "Nie udało się usunąć użytkownika");
      }

      setUsers((prev) => prev.filter((u) => u.id !== account.id));
      if (editingId === account.id) {
        handleCancel();
      }
      setSuccessMessage("Użytkownik został usunięty.");
    } catch (err) {
      setErrorMessage(err.message);
    } finally {
      setDeletingId(null);
    }
  };

  if (loading) {
    return (
      <div className="text-center py-5 text-secondary">
        Ładowanie użytkowników…
      </div>
    );
  }

  return (
    <div className="container py-5">
      <div className="mb-4">
        <p className="text-primary fw-semibold mb-1">Administracja</p>
        <h2 className="fw-bold mb-0">Zarządzanie użytkownikami</h2>
        <p className="text-secondary mb-0 mt-2">
          {users.length} {users.length === 1 ? "użytkownik" : "użytkowników"}
        </p>
      </div>

      {successMessage && (
        <div className="alert alert-success" role="alert">
          {successMessage}
        </div>
      )}

      {errorMessage && (
        <div className="alert alert-danger" role="alert">
          {errorMessage}
        </div>
      )}

      <div className="card border-0 shadow-sm">
        <div className="table-responsive">
          <table className="table table-hover align-middle mb-0">
            <thead className="table-light">
              <tr>
                <th scope="col">ID</th>
                <th scope="col">Imię</th>
                <th scope="col">Nazwisko</th>
                <th scope="col">E-mail</th>
                <th scope="col">Rola</th>
                <th scope="col">Saldo</th>
                <th scope="col" className="text-end">
                  Akcje
                </th>
              </tr>
            </thead>
            <tbody>
              {users.length === 0 && (
                <tr>
                  <td colSpan={7} className="text-center text-secondary py-4">
                    Brak użytkowników do wyświetlenia.
                  </td>
                </tr>
              )}

              {users.map((account) => {
                const isEditing = editingId === account.id;
                const isSelf = currentUser?.id === account.id;
                return (
                  <tr key={account.id}>
                    <td>{account.id}</td>
                    {isEditing ? (
                      <>
                        <td>
                          <input
                            type="text"
                            className="form-control form-control-sm"
                            name="firstName"
                            value={formData.firstName}
                            onChange={handleChange}
                          />
                        </td>
                        <td>
                          <input
                            type="text"
                            className="form-control form-control-sm"
                            name="lastName"
                            value={formData.lastName}
                            onChange={handleChange}
                          />
                        </td>
                        <td>
                          <input
                            type="email"
                            className="form-control form-control-sm"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                          />
                        </td>
                        <td>
                          <select
                            className="form-select form-select-sm"
                            name="role"
                            value={formData.role}
                            onChange={handleChange}
                          >
                            {ROLES.map((role) => (
                              <option key={role} value={role}>
                                {roleLabels[role]}
                              </option>
                            ))}
                          </select>
                        </td>
                        <td>
                          <input
                            type="number"
                            step="0.01"
                            className="form-control form-control-sm"
                            name="balance"
                            value={formData.balance}
                            onChange={handleChange}
                          />
                        </td>
                        <td className="text-end text-nowrap">
                          <button
                            type="button"
                            className="btn btn-success btn-sm me-2"
                            onClick={() => handleSave(account)}
                            disabled={submitting}
                          >
                            {submitting ? "Zapisywanie…" : "Zapisz"}
                          </button>
                          <button
                            type="button"
                            className="btn btn-outline-secondary btn-sm"
                            onClick={handleCancel}
                            disabled={submitting}
                          >
                            Anuluj
                          </button>
                        </td>
                      </>
                    ) : (
                      <>
                        <td>{account.firstName}</td>
                        <td>{account.lastName}</td>
                        <td>{account.email}</td>
                        <td>{roleLabels[account.role] || account.role}</td>
                        <td>{formatBalance(account.balance)}</td>
                        <td className="text-end text-nowrap">
                          <button
                            type="button"
                            className="btn btn-outline-primary btn-sm me-2"
                            onClick={() => handleEdit(account)}
                          >
                            Edytuj
                          </button>
                          <button
                            type="button"
                            className="btn btn-outline-danger btn-sm"
                            onClick={() => handleDelete(account)}
                            disabled={deletingId === account.id || isSelf}
                            title={
                              isSelf
                                ? "Nie możesz usunąć własnego konta"
                                : undefined
                            }
                          >
                            {deletingId === account.id ? "Usuwanie…" : "Usuń"}
                          </button>
                        </td>
                      </>
                    )}
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default AdminUsersList;
