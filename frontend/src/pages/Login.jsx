import React, { useState } from 'react';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Alert from '@mui/material/Alert';
import { useQueryClient } from "@tanstack/react-query";
import { Stack } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import { login } from '../api/userApi.js';

function Login() {
    const navigate = useNavigate();
    const queryClient = useQueryClient();

    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    // Form data
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();

        setError(null);
        if (!email.trim() || !password.trim()) {
            setError("Wypełnij wszystkie pola");
            return;
        }

        setIsLoading(true);
        try {
            await login(email, password);
            await queryClient.invalidateQueries({queryKey: ["currentUser"]})
            navigate("/")
        }catch(err) {
            setError("Nieprawidłowy email lub hasło");
        }
        setIsLoading(false);
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
                            error={error}
                            value={email}
                            onChange={(e) => {setEmail(e.target.value)}}
                        />
                        <TextField
                            label="Hasło"
                            type="password"
                            variant="outlined"
                            fullWidth
                            error={error}
                            value={password}
                            onChange={(e) => {setPassword(e.target.value)}}
                        />

                        {error && <Alert severity="error">{error}</Alert>}

                        <Button
                            type="submit"
                            variant="contained"
                            color="secondary"
                            size="large"
                            fullWidth
                            disabled={isLoading}
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