import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import SearchOffIcon from '@mui/icons-material/SearchOff';
import { Link } from 'react-router-dom';
import AuctionCard from '../components/AuctionCard.jsx';
import HeroCarousel from '../components/HeroCarousel.jsx';
import { useAuctions } from '../hooks/useAuctions.js';


function Home() {
    const { data: auctions, isLoading } = useAuctions();
    const recentAuctions = auctions ? auctions.slice(0, 3) : [];

    return (
        <>
            {/* Sekcja hero */}
            <Box
                sx={{
                    position: 'relative',
                    color: 'primary.contrastText',
                    py: { xs: 6, md: 10 },
                    overflow: 'hidden',
                }}
            >
                <HeroCarousel />
                <Container maxWidth="md" sx={{ position: 'relative', zIndex: 1, textAlign: 'center' }}>
                    <Typography variant="h3" component="h1" fontWeight={700} gutterBottom>
                        Znajdź swój wymarzony samochód już dziś!
                    </Typography>
                    <Typography variant="h6" component="p" sx={{ opacity: 0.85, mb: 4 }}>
                        Licytuj pojazdy od zweryfikowanych sprzedawców albo wystaw własny samochód na sprzedaż.
                    </Typography>
                    <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} sx={{ justifyContent: 'center' }}>
                        <Button component={Link} to="/auctions" variant="contained" color="secondary" size="large">
                            Przeglądaj aukcje
                        </Button>
                        <Button
                            component={Link}
                            to="/auctions/new"
                            variant="outlined"
                            size="large"
                            sx={{ color: 'inherit', borderColor: 'inherit' }}
                        >
                            Wystaw pojazd
                        </Button>
                    </Stack>
                </Container>
            </Box>

            {/* Sekcja aktualnych aukcji */}
            <Container maxWidth="lg" sx={{ py: 6 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                    <Typography variant="h4" component="h2">
                        Ostatnie aukcje
                    </Typography>
                    <Button component={Link} to="/auctions" color="secondary">
                        Zobacz wszystkie
                    </Button>
                </Box>

                {isLoading ? (
                    <Typography color="text.secondary">Ładowanie aukcji…</Typography>
                ) : recentAuctions.length === 0 ? (
                    <Typography color="text.secondary" sx={{display: 'flex', justifyContent: 'center', alignItems: 'center', fontSize: '20px'}}><SearchOffIcon/>Brak aktualnych aukcji.</Typography>
                ) : (
                    <Box
                        sx={{
                            display: 'grid',
                            gridTemplateColumns: {
                                xs: '1fr',
                                sm: 'repeat(2, 1fr)',
                                md: 'repeat(3, 1fr)',
                            },
                            gap: 3,
                        }}
                    >
                        {recentAuctions.map((auction) => (
                            <AuctionCard key={auction.id} auction={auction} />
                        ))}
                    </Box>
                )}
            </Container>

            <Box sx={{ backgroundColor: 'background.paper', py: 6 }}>
                <Container maxWidth="sm" sx={{ textAlign: 'center' }}>
                    <Typography variant="h5" component="h2" gutterBottom>
                        Masz pojazd do sprzedania?
                    </Typography>
                    <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
                        Wystaw go na aukcji i dotrzyj do tysięcy potencjalnych kupujących.
                    </Typography>
                    <Button component={Link} to="/auctions/new" variant="contained" color="secondary" size="large">
                        Wystaw pojazd
                    </Button>
                </Container>
            </Box>
        </>
    );
}

export default Home;
