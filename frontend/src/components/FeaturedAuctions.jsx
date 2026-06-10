import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import AuctionCard from "./AuctionCard";

function formatPrice(value) {
  if (value == null) return "—";
  return Number(value).toLocaleString("pl-PL");
}

function getAuctionTitle(auction) {
  const vehicle = auction.vehicle;
  if (!vehicle) return "Aukcja";
  return [vehicle.brand, vehicle.model].filter(Boolean).join(" ");
}

function FeaturedAuctions() {
  const [auctions, setAuctions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchFeatured = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/auctions?status=ACTIVE");
        if (response.ok) {
          const data = await response.json();
          // Sort to get newest (assuming backend returns array, reverse to take last ones)
          const latest = data.reverse().slice(0, 3);
          setAuctions(latest);
        }
      } catch (err) {
        console.error("Failed to fetch featured auctions", err);
      } finally {
        setLoading(false);
      }
    };
    fetchFeatured();
  }, []);

  return (
    <section id="featured" className="py-5">
      <div className="container">
        <div className="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-3 mb-4">
          <div>
            <p className="text-primary fw-semibold mb-1">Wyróżnione oferty</p>
            <h2 className="fw-bold mb-0">Najciekawsze aukcje tego tygodnia</h2>
          </div>
          <Link to="/auctions" className="btn btn-outline-dark">
            Zobacz wszystkie
          </Link>
        </div>
        
        <div className="row g-4">
          {loading ? (
            [1, 2, 3].map((slot) => (
              <div key={slot} className="col-md-6 col-xl-4">
                <article className="card border-0 shadow-sm h-100 auction-card">
                  <div className="card-img-top auction-card-image bg-secondary-subtle placeholder-glow" style={{ height: '200px' }}>
                    <span className="placeholder w-100 h-100 d-block"></span>
                  </div>
                  <div className="card-body">
                    <span className="badge text-bg-secondary mb-3">Ładowanie</span>
                    <h3 className="h5 fw-semibold placeholder-glow mb-3">
                      <span className="placeholder col-8"></span>
                    </h3>
                    <p className="text-secondary mb-3 placeholder-glow">
                      <span className="placeholder col-10"></span>
                      <span className="placeholder col-7"></span>
                    </p>
                    <div className="d-flex justify-content-between align-items-center">
                      <p className="h5 text-primary mb-0 placeholder-glow">
                        <span className="placeholder col-6"></span>
                      </p>
                      <button className="btn btn-sm btn-primary" disabled>
                        Licytuj
                      </button>
                    </div>
                  </div>
                </article>
              </div>
            ))
          ) : auctions.length > 0 ? (
            auctions.map((auction) => (
              <div key={auction.id} className="col-md-6 col-xl-4">
                <AuctionCard
                  auctionId={auction.id}
                  title={getAuctionTitle(auction)}
                  image={auction.vehicle?.image}
                  price={formatPrice(auction.currentPrice)}
                  year={auction.vehicle?.year}
                  endTime={auction.endTime}
                />
              </div>
            ))
          ) : (
            <div className="col-12 text-center py-5 text-secondary">
              Brak aktywnych aukcji w tym momencie.
            </div>
          )}
        </div>
      </div>
    </section>
  );
}

export default FeaturedAuctions;
