const placeholderCards = [1, 2, 3];

function FeaturedAuctions() {
  return (
    <section id="featured" className="py-5">
      <div className="container">
        <div className="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-3 mb-4">
          <div>
            <p className="text-primary fw-semibold mb-1">Wyróżnione oferty</p>
            <h2 className="fw-bold mb-0">Najciekawsze aukcje tego tygodnia</h2>
          </div>
          <button className="btn btn-outline-dark" disabled>
            Wkrótce
          </button>
        </div>
        <div className="row g-4">
          {placeholderCards.map((slot) => (
            <div key={slot} className="col-md-6 col-xl-4">
              <article className="card border-0 shadow-sm h-100 auction-card">
                <div className="card-img-top auction-card-image bg-secondary-subtle placeholder-glow">
                  <span className="placeholder w-100 h-100 d-block"></span>
                </div>
                <div className="card-body">
                  <span className="badge text-bg-secondary mb-3">Placeholder</span>
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
          ))}
        </div>
      </div>
    </section>
  );
}

export default FeaturedAuctions;
