import Typography from "@mui/material/Typography";
import React, {useState} from "react";
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import {Grid} from "@mui/material";
import TextField from "@mui/material/TextField";
import Select from "@mui/material/Select"
import MenuItem from "@mui/material/MenuItem"
import Alert from "@mui/material/Alert";
import Button from "@mui/material/Button";
import {Link, useNavigate} from "react-router-dom";
import { useQueryClient } from "@tanstack/react-query";
import { useBrands } from "../hooks/useBrands.js"
import { createAuction } from "../api/auctionApi.js"

const MAX_IMAGE_SIZE_BYTES = 5 * 1024 * 1024;
const ALLOWED_IMAGE_TYPES = ["image/jpeg", "image/png", "image/webp"];

function NewAuction() {
    const [brand, setBrand] = useState("");
    const [model, setModel] = useState("");
    const [year, setYear] = useState("");
    const [fuelType, setFuelType] = useState("");
    const [engineCapacity, setEngineCapacity] = useState("");
    const [description, setDescription] = useState("");
    const [vin, setVin] = useState("");
    const [image, setImage] = useState(null);
    const [startPrice, setStartPrice] = useState("");
    const [minIncrement, setMinIncrement] = useState("");
    const [endTime, setEndTime] = useState("");
    const [error, setError] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const navigate = useNavigate();
    const queryClient = useQueryClient();

    const {data: brands, isLoading} = useBrands()

    const fuelTypes = [
        {value: "PETROL", label: "Benzyna"},
        {value: "DIESEL", label: "Diesel"},
        {value: "LPG", label: "LPG"},
        {value: "HYBRID", label: "Hybryda"},
        {value: "ELECTRIC", label: "Elektryczny"},
    ];

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (!file) return;
        if (!ALLOWED_IMAGE_TYPES.includes(file.type)) {
            setError("Dozwolone formaty zdjęć: JPG, PNG, WEBP");
            e.target.value = "";
            return;
        }
        if (file.size > MAX_IMAGE_SIZE_BYTES) {
            setError("Zdjęcie może mieć maksymalnie 5 MB");
            e.target.value = "";
            return;
        }
        setError(null);
        setImage(file);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError(null);

        const required = [brand, model, year, fuelType, engineCapacity, vin, startPrice, minIncrement, endTime];
        if (required.some((value) => String(value).trim() === "")) {
            setError("Wypełnij wszystkie wymagane pola");
            return;
        }

        setIsSubmitting(true);
        try {
            await createAuction({
                vehicle: {
                    brand,
                    model: model.trim(),
                    year: Number(year),
                    fuelType,
                    engineCapacity: Number(engineCapacity),
                    description,
                    vin: vin.trim(),
                },
                startPrice: Number(startPrice),
                minIncrement: Number(minIncrement),
                endTime,
            }, image);
            await queryClient.invalidateQueries({queryKey: ["auctions"]});
            navigate("/");
        } catch (err) {
            setError(err.data?.message ?? "Nie udało się dodać aukcji. Spróbuj ponownie.");
        } finally {
            setIsSubmitting(false);
        }
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
                    Nowa aukcja
                </Typography>
                <form onSubmit={handleSubmit} noValidate>
                    <Grid container spacing={2}>
                        <Grid size={6}>
                            <TextField
                                label="Marka"
                                select
                                variant="outlined"
                                fullWidth
                                value={brand}
                                disabled={isLoading}
                                onChange={(e) => {setBrand(e.target.value)}}
                            >
                                {brands?.map((item) => (
                                    <MenuItem key={item.id} value={item.name}>{ item.name }</MenuItem>
                                ))}
                            </TextField>
                        </Grid>
                        <Grid size={6}>
                            <TextField
                                label="Model"
                                variant="outlined"
                                fullWidth
                                value={model}
                                onChange={(e) => {setModel(e.target.value)}}
                            />
                        </Grid>
                        <Grid size={6}>
                            <TextField
                                label="Rok produkcji"
                                variant="outlined"
                                type="number"
                                fullWidth
                                value={year}
                                onChange={(e) => {setYear(e.target.value)}}
                            />
                        </Grid>
                        <Grid size={6}>
                            <TextField
                                label="Rodzaj paliwa"
                                select
                                variant="outlined"
                                fullWidth
                                value={fuelType}
                                onChange={(e) => {setFuelType(e.target.value)}}
                            >
                                {fuelTypes.map((item) => (
                                    <MenuItem key={item.value} value={item.value}>{item.label}</MenuItem>
                                ))}
                            </TextField>
                        </Grid>
                        <Grid size={6}>
                            <TextField
                                label="Pojemność silnika (cm³)"
                                variant="outlined"
                                type="number"
                                fullWidth
                                value={engineCapacity}
                                onChange={(e) => {setEngineCapacity(e.target.value)}}
                            />
                        </Grid>
                        <Grid size={6}>
                            <TextField
                                label="Numer VIN"
                                variant="outlined"
                                fullWidth
                                value={vin}
                                onChange={(e) => {setVin(e.target.value)}}
                            />
                        </Grid>
                        <Grid size={12}>
                            <TextField
                                label="Opis"
                                variant="outlined"
                                multiline
                                minRows={4}
                                fullWidth
                                value={description}
                                onChange={(e) => {setDescription(e.target.value)}}
                            />
                        </Grid>
                        <Grid size={12}>
                            <TextField
                                label="Zdjęcie pojazdu"
                                variant="outlined"
                                fullWidth
                                value={image ? image.name : ""}
                                placeholder="Nie wybrano pliku (JPG, PNG, WEBP, do 5 MB)"
                                slotProps={{
                                    inputLabel: { shrink: true },
                                    input: {
                                        readOnly: true,
                                        endAdornment: (
                                            <Button component="label" size="small" color="secondary" sx={{ whiteSpace: 'nowrap' }}>
                                                Wybierz plik
                                                <input
                                                    hidden
                                                    type="file"
                                                    accept="image/jpeg,image/png,image/webp"
                                                    onChange={handleImageChange}
                                                />
                                            </Button>
                                        ),
                                    },
                                }}
                            />
                        </Grid>
                        <Grid size={6}>
                            <TextField
                                label="Cena wywoławcza (zł)"
                                variant="outlined"
                                type="number"
                                fullWidth
                                value={startPrice}
                                onChange={(e) => {setStartPrice(e.target.value)}}
                            />
                        </Grid>
                        <Grid size={6}>
                            <TextField
                                label="Minimalne przebicie (zł)"
                                variant="outlined"
                                type="number"
                                fullWidth
                                value={minIncrement}
                                onChange={(e) => {setMinIncrement(e.target.value)}}
                            />
                        </Grid>
                        {error && (
                            <Grid size={12}>
                                <Alert severity="error">{error}</Alert>
                            </Grid>
                        )}
                        <Grid size={8}>
                            <TextField
                                label="Koniec aukcji"
                                variant="outlined"
                                type="datetime-local"
                                fullWidth
                                slotProps={{ inputLabel: { shrink: true } }}
                                value={endTime}
                                onChange={(e) => {setEndTime(e.target.value)}}
                            />
                        </Grid>
                        <Grid size={4}>
                            <Button
                                type="submit"
                                variant="contained"
                                color="secondary"
                                size="large"
                                fullWidth
                                disabled={isLoading || isSubmitting}
                            >Dodaj aukcję</Button>
                        </Grid>
                    </Grid>
                </form>
            </Paper>
        </Box>
    )
}
export default NewAuction;