import { useMemo, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "./AuthContext";
import AuctionCountdown from "./AuctionCountdown";

const API_BASE = "http://localhost:8080";

function formatPrice(value) {
  if (value == null) return "—";
  return Number(value).toLocaleString("pl-PL");
}

function BidForm({ auction, hasBids, isEnded, onBidPlaced }) {
  const { user, token, isAuthenticated, updateUser } = useAuth();
  const [amount, setAmount] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);

  const minBid = useMemo(() => {
    const current = Number(auction.currentPrice);
    const increment = Number(auction.minIncrement);
    return hasBids ? current + increment : current;
  }, [auction.currentPrice, auction.minIncrement, hasBids]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMessage(null);
    setSuccessMessage(null);

    const bidAmount = Number(amount);
    if (!Number.isFinite(bidAmount) || bidAmount <= 0) {
      setErrorMessage("Podaj poprawną kwotę oferty.");
      return;
    }

    if (bidAmount < minBid) {
      setErrorMessage(`Minimalna oferta to ${formatPrice(minBid)} PLN.`);
      return;
    }

    if (user?.balance != null && bidAmount > Number(user.balance)) {
      setErrorMessage("Niewystarczające saldo konta.");
      return;
    }

    setSubmitting(true);
    try {
      const response = await fetch(`${API_BASE}/api/bids`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          amount: bidAmount,
          auction: { id: auction.id },
          user: { id: user.id },
        }),
      });

      if (!response.ok) {
        const err = await response.json().catch(() => ({}));
        throw new Error(err.message || "Nie udało się złożyć oferty.");
      }

      const accountResponse = await fetch(`${API_BASE}/api/account`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (accountResponse.ok) {
        const account = await accountResponse.json();
        updateUser(account);
      }

      setSuccessMessage("Oferta została złożona.");
      setAmount("");
      onBidPlaced?.();
    } catch (err) {
      setErrorMessage(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  if (!isAuthenticated) {
    return (
      <div className="card border-0 shadow-sm">
        <div className="card-body">
          <h2 className="h5 fw-semibold mb-3">Złóż ofertę</h2>
          <p className="text-secondary mb-3">
            Zaloguj się, aby licytować ten pojazd.
          </p>
          <Link to="/login" className="btn btn-primary w-100">
            Zaloguj się
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="card border-0 shadow-sm">
      <div className="card-body">
        <h2 className="h5 fw-semibold mb-3">Złóż ofertę</h2>

        <div className="mb-3">
          <p className="text-secondary small mb-1">Aktualna cena</p>
          <p className="h4 text-primary fw-bold mb-0">
            {formatPrice(auction.currentPrice)} PLN
          </p>
        </div>

        <div className="mb-3">
          <p className="text-secondary small mb-1">Minimalny krok licytacji</p>
          <p className="mb-0 fw-semibold">
            {formatPrice(auction.minIncrement)} PLN
          </p>
        </div>

        <div className="mb-3">
          <AuctionCountdown endTime={auction.endTime} />
        </div>

        <div className="mb-4">
          <p className="text-secondary small mb-1">Twoje saldo</p>
          <p className="mb-0 fw-semibold">{formatPrice(user?.balance)} PLN</p>
        </div>

        {errorMessage && (
          <div className="alert alert-danger py-2" role="alert">
            {errorMessage}
          </div>
        )}

        {successMessage && (
          <div className="alert alert-success py-2" role="alert">
            {successMessage}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label htmlFor="bidAmount" className="form-label">
              Twoja oferta (PLN)
            </label>
            <input
              type="number"
              id="bidAmount"
              className="form-control"
              min={minBid}
              step="0.01"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              placeholder={String(minBid)}
              disabled={isEnded || submitting}
              required
            />
            <div className="form-text">
              Minimalna oferta: {formatPrice(minBid)} PLN
            </div>
          </div>

          <button
            type="submit"
            className="btn btn-primary w-100"
            disabled={isEnded || submitting}
          >
            {isEnded
              ? "Aukcja zakończona"
              : submitting
                ? "Składanie oferty…"
                : "Złóż ofertę"}
          </button>
        </form>
      </div>
    </div>
  );
}

export default BidForm;
