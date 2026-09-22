import { useEffect, useState } from 'react';
import Box from '@mui/material/Box';

const slides = [
    '/carousel/carousel-1.jpeg',
    '/carousel/carousel-2.jpeg',
    '/carousel/carousel-3.jpeg'
];

const SLIDE_INTERVAL_MS = 5000;

function HeroCarousel() {
    const [activeIndex, setActiveIndex] = useState(0);

    useEffect(() => {
        const timer = setInterval(() => {
            setActiveIndex((prev) => (prev + 1) % slides.length);
        }, SLIDE_INTERVAL_MS);
        return () => clearInterval(timer);
    }, []);

    return (
        <Box sx={{ position: 'absolute', inset: 0, overflow: 'hidden', backgroundColor: 'primary.dark' }}>
            {slides.map((src, index) => (
                <Box
                    key={src}
                    sx={{
                        position: 'absolute',
                        inset: 0,
                        backgroundImage: `url(${src})`,
                        backgroundSize: 'cover',
                        backgroundPosition: 'center',
                        opacity: index === activeIndex ? 1 : 0,
                        transition: 'opacity 1s ease-in-out',
                    }}
                />
            ))}

            {/* Przyciemnienie pod spodem, żeby tekst na wierzchu zawsze był czytelny */}
            <Box sx={{ position: 'absolute', inset: 0, backgroundColor: 'rgba(0, 0, 0, 0.5)' }} />

            {/* Kropki wskazujące aktywny slajd */}
            <Box
                sx={{
                    position: 'absolute',
                    bottom: 16,
                    left: 0,
                    right: 0,
                    display: 'flex',
                    justifyContent: 'center',
                    gap: 1,
                }}
            >
                {slides.map((_, index) => (
                    <Box
                        key={index}
                        onClick={() => setActiveIndex(index)}
                        sx={{
                            width: 10,
                            height: 10,
                            borderRadius: '50%',
                            cursor: 'pointer',
                            backgroundColor: index === activeIndex ? 'secondary.main' : 'rgba(255, 255, 255, 0.5)',
                            transition: 'background-color 0.3s ease-in-out',
                        }}
                    />
                ))}
            </Box>
        </Box>
    );
}

export default HeroCarousel;
