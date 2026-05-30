function CallToAction() {
  return (
    <section id="contact" className="py-5">
      <div className="container">
        <div className="p-4 p-md-5 rounded-4 cta-panel text-white shadow">
          <div className="row align-items-center g-4">
            <div className="col-lg-8">
              <p className="text-uppercase small mb-2 cta-kicker">Dla sprzedających</p>
              <h2 className="fw-bold mb-2">Sprzedajesz auto? Dotrzyj do tysięcy kupujących</h2>
              <p className="mb-0 opacity-75">
                Dodaj informacje o pojeździe, ustaw cenę wywoławczą i rozpocznij aukcje jeszcze dzisiaj.
              </p>
            </div>
            <div className="col-lg-4 text-lg-end">
              <button className="btn btn-light btn-lg px-4">Wystaw samochód</button>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}

export default CallToAction;
