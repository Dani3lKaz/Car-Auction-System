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
import {login, register} from '../api/userApi.js'

function Register() {
    const navigate = useNavigate();
    const queryClient = useQueryClient();

    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(false);


    // Form data
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError(null);
        if(!firstName.trim() || !lastName.trim() || !email.trim() || !password.trim() || !confirmPassword.trim()) {
            setError("Wypełnij wszystkie pola");
            return;
        }

        if(password !== confirmPassword) {
            setError("Hasła muszą być identyczne");
            return;
        }
        setIsLoading(true);
        try{
            await register(firstName, lastName, email, password);
            await login(email, password);
            await queryClient.invalidateQueries({queryKey: ["currentUser"]})
            navigate("/")
        }catch(err){
            setError("Użytkownik z takim adresem e-mail już istnieje")
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
                    Rejestracja
                </Typography>

                <form onSubmit={handleSubmit} noValidate>
                    <Stack spacing={3} sx={{ mt: 2 }}>
                        <TextField
                            label="Imię"
                            variant="outlined"
                            fullWidth
                            error={error}
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                        />
                        <TextField
                            label="Nazwisko"
                            variant="outlined"
                            fullWidth
                            error={error}
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
                        />
                        <TextField
                            label="Adres e-mail"
                            type="email"
                            variant="outlined"
                            fullWidth
                            error={error}
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                        <TextField
                            label="Hasło"
                            type="password"
                            variant="outlined"
                            fullWidth
                            error={error}
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                        <TextField
                            label="Powtórz hasło"
                            type="password"
                            variant="outlined"
                            fullWidth
                            error={error}
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
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