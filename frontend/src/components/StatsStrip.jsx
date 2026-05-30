const stats = [
  { value: "12k+", label: "aktywnych użytkowników" },
  { value: "1.8k+", label: "aukcji miesięcznie" },
  { value: "96%", label: "zadowolonych klientów" },
];

function StatsStrip() {
  return (
    <section className="py-5 bg-white border-bottom">
      <div className="container">
        <div className="row g-3 text-center justify-content-center">
          {stats.map((item) => (
            <div key={item.label} className="col-6 col-lg-3">
              <div className="p-3 rounded-3 bg-light h-100">
                <p className="h2 fw-bold mb-1 text-primary">{item.value}</p>
                <p className="text-secondary mb-0 small text-uppercase">{item.label}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

export default StatsStrip;
