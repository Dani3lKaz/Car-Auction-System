import { Link } from "react-router-dom";
import { useAuth } from "./AuthContext";

function Navbar() {
  const { user,isAuthenticated, logout } = useAuth();

  return (
    <nav className="navbar navbar-expand-lg bg-white border-bottom shadow-sm sticky-top">
      <div className="container py-1">
        <Link className="navbar-brand fw-bold text-dark" to="/">
          <i className="bi bi-car-front-fill fs-3 me-2" />
          MotoTrade
        </Link>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarMain"
          aria-controls="navbarMain"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarMain">
          <ul className="navbar-nav mx-auto mb-2 mb-lg-0">
            <li className="nav-item">
              <Link
                className="nav-link active fw-semibold"
                aria-current="page"
                to="/"
              >
                Strona główna
              </Link>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="#featured">
                Aukcje
              </a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="#how-it-works">
                Jak to działa
              </a>
            </li>
          </ul>
          <div className="d-flex gap-2">
            {isAuthenticated ? (
              <>
                <span className="align-self-center small">
                  Cześć, {user.firstName}
                </span>
                <button
                  className="btn btn-outline-dark btn-sm"
                  onClick={logout}
                >
                  Wyloguj
                </button>
              </>
            ) : (
              <>
                <Link className="btn btn-outline-dark btn-sm" to="/login">
                  Zaloguj
                </Link>
                <Link className="btn btn-primary btn-sm px-3" to="/register">
                  Zarejestruj
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}
export default Navbar;
