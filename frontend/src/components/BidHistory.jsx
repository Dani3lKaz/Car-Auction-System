import React from "react";

function formatPrice(value) {
  if (value == null) return "—";
  return Number(value).toLocaleString("pl-PL");
}

function formatDate(dateString) {
  if (!dateString) return "—";
  const date = new Date(dateString);
  return date.toLocaleString("pl-PL", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit"
  });
}

function BidHistory({ bids }) {
  if (!bids || bids.length === 0) {
    return (
      <div className="card border-0 shadow-sm mt-4">
        <div className="card-body text-center py-4 text-secondary">
          <p className="mb-0">Brak ofert. Bądź pierwszy!</p>
        </div>
      </div>
    );
  }

  return (
    <div className="card border-0 shadow-sm mt-4">
      <div className="card-body">
        <h2 className="h5 fw-semibold mb-3">Historia ofert</h2>
        <div className="table-responsive">
          <table className="table table-hover mb-0">
            <thead className="table-light">
              <tr>
                <th>Użytkownik</th>
                <th>Kwota</th>
                <th className="text-end">Czas</th>
              </tr>
            </thead>
            <tbody>
              {bids.map((bid, index) => (
                <tr key={bid.id || index} className={index === 0 ? "table-success" : ""}>
                  <td>
                    {bid.user ? `${bid.user.firstName} ${bid.user.lastName}` : "Anonim"}
                  </td>
                  <td className="fw-semibold text-primary">
                    {formatPrice(bid.amount)} PLN
                  </td>
                  <td className="text-end text-secondary small">
                    {formatDate(bid.createdAt)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default BidHistory;
