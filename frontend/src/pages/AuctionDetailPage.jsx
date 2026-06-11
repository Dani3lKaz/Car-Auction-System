import { useCallback, useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { useAuth } from "../components/auth-context";
import Footer from "../components/Footer";
import Navbar from "../components/Navbar";
import BidForm from "../components/BidForm";
import BidHistory from "../components/BidHistory";
import noCarImage from "../assets/no-car-image.jpeg";
import "../styles/auctiondetail.css";

const API_BASE = "http://localhost:8080";

function formatPrice(value) {
  if (value == null) return "—";
  return Number(value).toLocaleString("pl-PL");
}

function getAuctionTitle(auction) {
  const vehicle = auction?.vehicle;
  if (!vehicle) return "Aukcja";
  return [vehicle.brand, vehicle.model].filter(Boolean).join(" ");
}

function DetailRow({ label, value }) {
  if (value == null || value === "") return null;
  return (
    <div className="col-sm-6">
      <dt className="text-secondary small mb-1">{label}</dt>
      <dd className="fw-semibold mb-3">{value}</dd>
    </div>
  );
}

function AuctionDetailPage() {
  const { auctionId } = useParams();
  const { token } = useAuth();
  const [auction, setAuction] = useState(null);
  const [hasBids, setHasBids] = useState(false);
  const [bids, setBids] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isEnded, setIsEnded] = useState(false);
  const [stompClient, setStompClient] = useState(null);

  const loadAuction = useCallback(async () => {
    const response = await fetch(`${API_BASE}/api/auctions/${auctionId}`);
    if (!response.ok) {
      throw new Error("Nie udało się pobrać aukcji.");
    }
    return response.json();
  }, [auctionId]);

  const loadHasBids = useCallback(async (id) => {
    const response = await fetch(`${API_BASE}/api/bids`);
    if (!response.ok) {
      return false;
    }
    const bids = await response.json();
    return bids.some((bid) => bid.auction?.id === Number(id));
  }, []);

  const loadAuctionBids = useCallback(async (id) => {
    const response = await fetch(`${API_BASE}/api/bids/auction/${id}`);
    if (!response.ok) {
      return [];
    }
    return response.json();
  }, []);

  const refresh = useCallback(async () => {
    const data = await loadAuction();
    setAuction(data);
    setHasBids(await loadHasBids(auctionId));
    setBids(await loadAuctionBids(auctionId));
  }, [auctionId, loadAuction, loadHasBids, loadAuctionBids]);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);
      try {
        await refresh();
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [refresh]);

  useEffect(() => {
    if (!auction?.endTime) return;

    const end = new Date(auction.endTime).getTime();
    const tick = () => setIsEnded(Date.now() >= end);
    tick();
    const id = setInterval(tick, 1000);
    return () => clearInterval(id);
  }, [auction?.endTime]);

  useEffect(() => {
    if (!auctionId) return;

    const client = new Client({
      webSocketFactory: () => new SockJS(`${API_BASE}/ws`),
      connectHeaders: token ? { Authorization: `Bearer ${token}` } : {},
      reconnectDelay: 5000,
      onConnect: () => {
        client.subscribe(`/topic/auctions/${auctionId}/bids`, (message) => {
          if (message.body) {
            const newBid = JSON.parse(message.body);
            setAuction((prev) =>
              prev ? { ...prev, currentPrice: newBid.amount } : prev
            );
            setHasBids(true);
            setBids((prev) => [newBid, ...prev]);
          }
        });
        setStompClient(client);
      },
      onDisconnect: () => {
        setStompClient(null);
      },
      onStompError: (frame) => {
        console.error('STOMP error:', frame);
      },
    });

    client.activate();

    return () => {
      setStompClient(null);
      client.deactivate();
    };
  }, [auctionId, token]);

  const vehicle = auction?.vehicle;
  const title = getAuctionTitle(auction);

  return (
    <div className="d-flex flex-column min-vh-100 bg-body-tertiary">
      <Navbar />
      <main className="flex-grow-1 py-5">
        <div className="container">
          <Link to="/auctions" className="btn btn-link text-decoration-none ps-0 mb-4">
            ← Wróć do listy aukcji
          </Link>

          {loading && (
            <div className="text-center py-5 text-secondary">Ładowanie aukcji…</div>
          )}

          {error && (
            <div className="alert alert-danger" role="alert">
              {error}
            </div>
          )}

          {!loading && !error && auction && (
            <div className="row g-4">
              <div className="col-lg-8">
                <div className="card border-0 shadow-sm overflow-hidden mb-4">
                  <img
                    src={vehicle?.image || noCarImage}
                    alt={title}
                    className="auction-detail-image w-100 object-fit-cover"
                  />
                </div>

                <div className="card border-0 shadow-sm">
                  <div className="card-body p-4">
                    <div className="d-flex flex-wrap align-items-center gap-2 mb-3">
                      <span className="badge text-bg-secondary">
                        {vehicle?.year || "—"}
                      </span>
                      <span className="badge text-bg-light text-dark border">
                        {auction.status || "—"}
                      </span>
                    </div>

                    <h1 className="h2 fw-bold mb-4">{title}</h1>

                    <dl className="row mb-0">
                      <DetailRow label="Marka" value={vehicle?.brand} />
                      <DetailRow label="Model" value={vehicle?.model} />
                      <DetailRow label="Rok produkcji" value={vehicle?.year} />
                      <DetailRow label="Paliwo" value={vehicle?.fuelType} />
                      <DetailRow
                        label="Pojemność silnika"
                        value={
                          vehicle?.engineCapacity
                            ? `${vehicle.engineCapacity} cm³`
                            : null
                        }
                      />
                      <DetailRow label="VIN" value={vehicle?.vin} />
                      <DetailRow
                        label="Cena wywoławcza"
                        value={`${formatPrice(auction.startPrice)} PLN`}
                      />
                      <DetailRow
                        label="Sprzedawca"
                        value={
                          auction.seller
                            ? `${auction.seller.firstName} ${auction.seller.lastName}`
                            : null
                        }
                      />
                    </dl>

                    {vehicle?.description && (
                      <div className="mt-2">
                        <h2 className="h6 text-secondary mb-2">Opis</h2>
                        <p className="mb-0">{vehicle.description}</p>
                      </div>
                    )}
                  </div>
                </div>
              </div>

              <div className="col-lg-4">
                <div className="auction-detail-sidebar">
                  <BidForm
                    auction={auction}
                    hasBids={hasBids}
                    isEnded={isEnded}
                    stompClient={stompClient}
                  />
                  <BidHistory bids={bids} />
                </div>
              </div>
            </div>
          )}
        </div>
      </main>
      <Footer />
    </div>
  );
}

export default AuctionDetailPage;
