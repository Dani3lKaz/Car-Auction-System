import Footer from "../components/Footer";
import Navbar from "../components/Navbar";
import AuctionCard from "../components/AuctionCard";
import { useEffect, useMemo, useState } from "react";

const PAGE_SIZE = 9;

function formatPrice(value) {
  if (value == null) return "—";
  return Number(value).toLocaleString("pl-PL");
}

function getAuctionTitle(auction) {
  const vehicle = auction.vehicle;
  if (!vehicle) return "Aukcja";
  return [vehicle.brand, vehicle.model].filter(Boolean).join(" ");
}

function AuctionListPage() {
  const [auctions, setAuctions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    const fetchAuctions = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/auctions");
        if (!response.ok) {
          throw new Error("Nie udało się pobrać aukcji");
        }
        const data = await response.json();
        setAuctions(data);
        setCurrentPage(1);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    fetchAuctions();
  }, []);

  const totalPages = Math.max(1, Math.ceil(auctions.length / PAGE_SIZE));

  const pageAuctions = useMemo(() => {
    const start = (currentPage - 1) * PAGE_SIZE;
    return auctions.slice(start, start + PAGE_SIZE);
  }, [auctions, currentPage]);

  const goToPage = (page) => {
    setCurrentPage(Math.min(Math.max(1, page), totalPages));
  };

  return (
    <div className="d-flex flex-column min-vh-100 bg-body-tertiary">
      <Navbar />
      <main className="flex-grow-1 py-5">
        <div className="container">
          <div className="mb-4">
            <p className="text-primary fw-semibold mb-1">Aukcje</p>
            <h2 className="fw-bold mb-0">Wszystkie aktywne aukcje</h2>
            {!loading && !error && auctions.length > 0 && (
              <p className="text-secondary mb-0 mt-2">
                {auctions.length}{" "}
                {auctions.length === 1 ? "aukcja" : auctions.length < 5 ? "aukcje" : "aukcji"}
                {totalPages > 1 && (
                  <>
                    {" "}
                    · strona {currentPage} z {totalPages}
                  </>
                )}
              </p>
            )}
          </div>

          {loading && (
            <div className="text-center py-5 text-secondary">Ładowanie aukcji…</div>
          )}

          {error && (
            <div className="alert alert-danger" role="alert">
              {error}
            </div>
          )}

          {!loading && !error && auctions.length === 0 && (
            <div className="text-center py-5 text-secondary">
              Brak aukcji do wyświetlenia.
            </div>
          )}

          {!loading && !error && auctions.length > 0 && (
            <>
              <div className="row g-4">
                {pageAuctions.map((auction) => (
                  <div key={auction.id} className="col-12 col-md-4">
                    <AuctionCard
                      auctionId={auction.id}
                      title={getAuctionTitle(auction)}
                      image={auction.vehicle?.image}
                      price={formatPrice(auction.currentPrice)}
                      year={auction.vehicle?.year}
                      endTime={auction.endTime}
                    />
                  </div>
                ))}
              </div>

              {totalPages > 1 && (
                <nav
                  className="mt-5"
                  aria-label="Paginacja listy aukcji"
                >
                  <ul className="pagination justify-content-center flex-wrap gap-1 mb-0">
                    <li
                      className={`page-item ${currentPage === 1 ? "disabled" : ""}`}
                    >
                      <button
                        type="button"
                        className="page-link"
                        onClick={() => goToPage(currentPage - 1)}
                        disabled={currentPage === 1}
                        aria-label="Poprzednia strona"
                      >
                        Poprzednia
                      </button>
                    </li>
                    {Array.from({ length: totalPages }, (_, i) => i + 1).map(
                      (page) => (
                        <li
                          key={page}
                          className={`page-item ${page === currentPage ? "active" : ""}`}
                        >
                          <button
                            type="button"
                            className="page-link"
                            onClick={() => goToPage(page)}
                            aria-label={`Strona ${page}`}
                            aria-current={
                              page === currentPage ? "page" : undefined
                            }
                          >
                            {page}
                          </button>
                        </li>
                      )
                    )}
                    <li
                      className={`page-item ${currentPage === totalPages ? "disabled" : ""}`}
                    >
                      <button
                        type="button"
                        className="page-link"
                        onClick={() => goToPage(currentPage + 1)}
                        disabled={currentPage === totalPages}
                        aria-label="Następna strona"
                      >
                        Następna
                      </button>
                    </li>
                  </ul>
                </nav>
              )}
            </>
          )}
        </div>
      </main>
      <Footer />
    </div>
  );
}

export default AuctionListPage;
