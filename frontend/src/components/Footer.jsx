function Footer() {
  return (
    <footer className="bg-dark text-light py-4 mt-4">
      <div className="container d-flex flex-column flex-md-row justify-content-between align-items-center gap-2">
        <p className="mb-0">&copy; {new Date().getFullYear()} Car Auctions. Wszelkie prawa zastrzeżone.</p>
        <div className="d-flex gap-3">
          <a className="text-light text-decoration-none" href="#">Regulamin</a>
          <a className="text-light text-decoration-none" href="#">Polityka prywatności</a>
          <a className="text-light text-decoration-none" href="#">Kontakt</a>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
