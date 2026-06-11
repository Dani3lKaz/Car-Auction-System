import { useEffect, useState } from "react";

function formatRemaining(ms) {
  if (ms <= 0) return "Zakończono";

  const totalSeconds = Math.floor(ms / 1000);
  const days = Math.floor(totalSeconds / 86400);
  const hours = Math.floor((totalSeconds % 86400) / 3600);
  const minutes = Math.floor((totalSeconds % 3600) / 60);
  const seconds = totalSeconds % 60;

  const pad = (n) => String(n).padStart(2, "0");

  if (days > 0) {
    return `${days}d ${pad(hours)}:${pad(minutes)}:${pad(seconds)}`;
  }
  return `${pad(hours)}:${pad(minutes)}:${pad(seconds)}`;
}

function AuctionCountdown({ endTime }) {
  const [now, setNow] = useState(() => Date.now());

  useEffect(() => {
    if (!endTime) {
      return;
    }

    const timeoutId = setTimeout(() => setNow(Date.now()), 0);
    const intervalId = setInterval(() => setNow(Date.now()), 1000);

    return () => {
      clearTimeout(timeoutId);
      clearInterval(intervalId);
    };
  }, [endTime]);

  const label = endTime
    ? formatRemaining(new Date(endTime).getTime() - now)
    : "—";
  const isEnded = label === "Zakończono";

  return (
    <span className={`small fw-semibold ${isEnded ? "text-secondary" : "text-danger"}`}>
      {isEnded ? label : `Kończy za: ${label}`}
    </span>
  );
}

export default AuctionCountdown;
