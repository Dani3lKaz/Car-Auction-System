import "../styles/auctioncard.css";
import noCarImage from "../assets/no-car-image.jpeg";
import AuctionCountdown from "./AuctionCountdown";
import { useEffect, useState } from "react";

function AuctionCard({ title, image, price, year, endTime }) {
  const [isEnded, setIsEnded] = useState(false);

  useEffect(() => {
    if (!endTime) return;
    const end = new Date(endTime).getTime();
    const tick = () => {
      setIsEnded(Date.now() >= end);
    };

    tick();
    const id = setInterval(tick, 1000);
    return () => clearInterval(id);
  },[endTime])

  return (
    <article className="card border-0 shadow-sm h-100 auction-card">
      
      <div className="card-img-top auction-card-image bg-light overflow-hidden" style={{ height: '200px' }}>
        <img 
          src={image || noCarImage} 
          alt={title} 
          className="w-100 h-100 object-fit-cover" 
        />
      </div>

      <div className="card-body d-flex flex-column">
        <div className="mb-2">
          <span className="badge text-bg-secondary">{year || "MotoTrade"}</span>
        </div>
        
        <h3 className="h5 fw-semibold mb-3">{title}</h3>
        
        <div className="mb-3">
          <AuctionCountdown endTime={endTime} />
        </div>
        
        <div className="d-flex justify-content-between align-items-center mt-auto">
          <p className="h5 text-primary mb-0 fw-bold">{price} PLN</p>
          <button className="btn btn-sm btn-primary" disabled={isEnded}>
            {isEnded ? "Zakończono" : "Licytuj"}
          </button>
        </div>
      </div>

    </article>
  );
}

export default AuctionCard;