import Navbar from "../components/Navbar";
import MainPageCarousel from "../components/MainPageCarousel";
import StatsStrip from "../components/StatsStrip";
import FeaturedAuctions from "../components/FeaturedAuctions";
import HowItWorks from "../components/HowItWorks";
import CallToAction from "../components/CallToAction";
import Footer from "../components/Footer";
import "../styles/homepage.css";

function HomePage() {
  return (
    <main className="bg-body-tertiary min-vh-100">
      <Navbar />
      <MainPageCarousel />
      <StatsStrip />
      <FeaturedAuctions />
      <HowItWorks />
      <CallToAction />
      <Footer />
    </main>
  );
}

export default HomePage;
