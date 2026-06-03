import Footer from "../components/Footer";
import Navbar from "../components/Navbar";
import AuctionCard from "../components/AuctionCard";

const auctions = [
  // { title: "Audi RS6", price: 50000, year: 2010 },
  // { title: "BMW M3", price: 50000, year: 2010 },
  // { title: "Mercedes-Benz C63 AMG", price: 50000, year: 2010 },
];

function AuctionListPage() {
  return (
    <div className="d-flex flex-column min-vh-100 bg-body-tertiary">
      <Navbar />
      <main className="flex-grow-1 py-5">
        <div className="container">
          <div className="mb-4">
            <p className="text-primary fw-semibold mb-1">Aukcje</p>
            <h2 className="fw-bold mb-0">Wszystkie aktywne aukcje</h2>
          </div>
          <div className="row g-4">
            {auctions.map((auction) => (
              <div key={auction.title} className="col-md-6 col-xl-4">
                <AuctionCard
                  title={auction.title}
                  price={auction.price}
                  year={auction.year}
                />
              </div>
            ))}
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
}

export default AuctionListPage;