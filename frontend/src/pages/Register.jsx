import React, { useState } from 'react';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import { Stack } from '@mui/material';
import { Link } from 'react-router-dom';

function Register() {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

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
                    Rejestracja
                </Typography>

                <form onSubmit={handleSubmit} noValidate>
                    <Stack spacing={3} sx={{ mt: 2 }}>
                        <TextField
                            label="Imię"
                            variant="outlined"
                            fullWidth
                            required
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                        />
                        <TextField
                            label="Nazwisko"
                            variant="outlined"
                            fullWidth
                            required
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
                        />
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
                        <TextField
                            label="Powtórz hasło"
                            type="password"
                            variant="outlined"
                            fullWidth
                            required
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                        />

                        <Button
                            type="submit"
                            variant="contained"
                            color="secondary"
                            size="large"
                            fullWidth
                        >
                            Zarejestruj się
                        </Button>
                        <Typography variant="subtitle2" component="p" align="center">Masz już konto?</Typography>
                        <Button
                            variant="outlined"
                            color="secondary"
                            size="large"
                            fullWidth
                            component={Link}
                            to="/login"
                        >
                            Zaloguj się
                        </Button>
                    </Stack>
                </form>
            </Paper>
        </Box>
    );
}
export default Register;