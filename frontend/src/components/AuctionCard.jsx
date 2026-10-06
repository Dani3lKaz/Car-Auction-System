import Card from '@mui/material/Card';
import CardActionArea from '@mui/material/CardActionArea';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import Chip from '@mui/material/Chip';
import Box from '@mui/material/Box';
import DirectionsCarIcon from '@mui/icons-material/DirectionsCar';
import { Link } from 'react-router-dom';

const statusLabels = {
    NEW: 'Nowa',
    ACTIVE: 'Trwa',
    ENDED: 'Zakończona',
    CANCELLED: 'Anulowana',
};

const statusColors = {
    NEW: 'info',
    ACTIVE: 'success',
    ENDED: 'default',
    CANCELLED: 'error',
};

function AuctionCard({ auction }) {
    const { id, currentPrice, endTime, status, vehicle } = auction;

    return (
        <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
            <CardActionArea
                component={Link}
                to={`/auctions/${id}`}
                sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column', alignItems: 'stretch' }}
            >
                {vehicle?.image ? (
                    <CardMedia
                        component="img"
                        height="160"
                        image={vehicle.image}
                        alt={`${vehicle.brand} ${vehicle.model}`}
                    />
                ) : (
                    <Box
                        sx={{
                            height: 160,
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            backgroundColor: 'primary.light',
                        }}
                    >
                        <DirectionsCarIcon sx={{ fontSize: 64, color: 'primary.contrastText', opacity: 0.6 }} />
                    </Box>
                )}
                <CardContent sx={{ flexGrow: 1, width: '100%' }}>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 1 }}>
                        <Typography variant="h6" component="h3">
                            {vehicle?.brand} {vehicle?.model}
                        </Typography>
                        <Chip label={statusLabels[status] ?? status} size="small" color={statusColors[status] ?? 'default'} />
                    </Box>
                    <Typography variant="body2" color="text.secondary">
                        Rok produkcji: {vehicle?.year ?? '—'}
                    </Typography>
                    <Typography variant="h6" color="secondary" sx={{ mt: 1 }}>
                        {currentPrice != null ? `${currentPrice} zł` : '—'}
                    </Typography>
                    {endTime && (
                        <Typography variant="caption" color="text.secondary">
                            Koniec aukcji: {new Date(endTime).toLocaleString('pl-PL')}
                        </Typography>
                    )}
                </CardContent>
            </CardActionArea>
        </Card>
    );
}

export default AuctionCard;
