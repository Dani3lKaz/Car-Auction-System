const steps = [
  {
    title: "Utwórz konto",
    description: "Rejestrujesz się w kilka minut i uzyskujesz dostęp do wszystkich aukcji.",
  },
  {
    title: "Wybierz samochód",
    description: "Filtrujesz oferty po marce, cenie i roczniku, aby szybko trafić na idealny model.",
  },
  {
    title: "Licytuj i finalizuj",
    description: "Składasz ofertę, otrzymujesz potwierdzenie i bezpiecznie finalizujesz zakup.",
  },
];

function HowItWorks() {
  return (
    <section id="how-it-works" className="py-5 bg-light">
      <div className="container">
        <div className="text-center mb-5">
          <p className="text-primary fw-semibold mb-1">Proces</p>
          <h2 className="fw-bold">Jak działa nasza platforma</h2>
        </div>
        <div className="row g-4">
          {steps.map((step, index) => (
            <div key={step.title} className="col-md-4">
              <article className="card border-0 shadow-sm h-100">
                <div className="card-body p-4">
                  <span className="badge rounded-pill text-bg-primary mb-3">Krok {index + 1}</span>
                  <h3 className="h5 fw-semibold">{step.title}</h3>
                  <p className="text-secondary mb-0">{step.description}</p>
                </div>
              </article>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

export default HowItWorks;
