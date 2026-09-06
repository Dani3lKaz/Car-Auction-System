import { createTheme } from '@mui/material/styles';

export const theme = createTheme({
    palette: {
        primary: {
            main: '#1A2530',
            light: '#2C3E50',
            dark: '#11171E',
            contrastText: '#ffffff',
        },
        secondary: {
            main: '#FF5722',
            light: '#FF8A50',
            dark: '#C41C00',
            contrastText: '#ffffff',
        },
        background: {
            default: '#F4F6F8',
            paper: '#FFFFFF',
        },
        text: {
            primary: '#2D3748',
            secondary: '#718096',
        },
        success: {
            main: '#2E7D32',
        },
    },
});