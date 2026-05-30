import img1 from '../assets/main-page-1.jpg';
import img2 from '../assets/main-page-2.jpg';
import img3 from '../assets/main-page-3.jpg';

const slides = [
  {
    image: img1,
    title: "Znajdź idealne auto na aukcji",
    description: "Codziennie nowe oferty premium, klasyków i aut miejskich w atrakcyjnych cenach."
  },
  {
    image: img2,
    title: "Licytuj pewnie i bezpiecznie",
    description: "Zweryfikowani sprzedawcy, przejrzyste warunki i szybka finalizacja transakcji."
  },
  {
    image: img3,
    title: "Sprzedaj swój samochód szybko",
    description: "Wystaw ogłoszenie, dotrzyj do kupujących i uzyskaj najlepszą ofertę."
  }
];

function MainPageCarousel() {
  return (
    <section className="hero-carousel">
      <div id="heroCarousel" className="carousel slide carousel-fade" data-bs-ride="carousel">
        <div className="carousel-indicators">
          <button type="button" data-bs-target="#heroCarousel" data-bs-slide-to="0" className="active" aria-current="true" aria-label="Slide 1"></button>
          <button type="button" data-bs-target="#heroCarousel" data-bs-slide-to="1" aria-label="Slide 2"></button>
          <button type="button" data-bs-target="#heroCarousel" data-bs-slide-to="2" aria-label="Slide 3"></button>
        </div>
        <div className="carousel-inner">
          {slides.map((slide, index) => (
            <div key={slide.title} className={`carousel-item ${index === 0 ? "active" : ""}`}>
              <img src={slide.image} className="d-block w-100 hero-image" alt={slide.title} />
              <div className="carousel-overlay"></div>
              <div className="carousel-caption text-start pb-5">
                <span className="badge rounded-pill text-bg-light text-dark mb-3">Platforma aukcji samochodowych</span>
                <h1 className="display-5 fw-bold">{slide.title}</h1>
                <p className="lead d-none d-md-block">{slide.description}</p>
                <div className="d-flex flex-wrap gap-2 mt-3">
                  <a href="#featured" className="btn btn-primary btn-lg">Przeglądaj aukcje</a>
                  <a href="#contact" className="btn btn-outline-light btn-lg">Skontaktuj się</a>
                </div>
              </div>
            </div>
          ))}
        </div>
        <button className="carousel-control-prev" type="button" data-bs-target="#heroCarousel" data-bs-slide="prev">
          <span className="carousel-control-prev-icon" aria-hidden="true"></span>
          <span className="visually-hidden">Previous</span>
        </button>
        <button className="carousel-control-next" type="button" data-bs-target="#heroCarousel" data-bs-slide="next">
          <span className="carousel-control-next-icon" aria-hidden="true"></span>
          <span className="visually-hidden">Next</span>
        </button>
      </div>
    </section>
  );
}

export default MainPageCarousel;
