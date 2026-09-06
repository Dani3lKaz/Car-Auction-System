import React, { useState } from 'react';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import { Stack } from '@mui/material';
import { Link } from 'react-router-dom';

function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
    };

    return (
        <Box
            sx={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                minHeight: '80vh'
            }}
        >
            <Paper elevation={3} sx={{ p: 4, width: '100%', maxWidth: 600 }}>
                <Typography variant="h5" component="h1" gutterBottom>
                    Logowanie
                </Typography>

                <form onSubmit={handleSubmit} noValidate>
                    <Stack spacing={3} sx={{ mt: 2 }}>
                        <TextField
                            label="Adres e-mail"
                            type="email"
                            variant="outlined"
                            fullWidth
                            required
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                        <TextField
                            label="Hasło"
                            type="password"
                            variant="outlined"
                            fullWidth
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />

                        <Button
                            type="submit"
                            variant="contained"
                            color="secondary"
                            size="large"
                            fullWidth
                        >
                            Zaloguj się
                        </Button>
                        <Typography variant="subtitle2" component="p" align="center">Nie masz jeszcze konta?</Typography>
                        <Button
                            variant="outlined"
                            color="secondary"
                            size="large"
                            fullWidth
                            component={Link}
                            to="/register"
                        >
                            Zarejestruj się
                        </Button>
                    </Stack>
                </form>
            </Paper>
        </Box>
    )
}

export default Login