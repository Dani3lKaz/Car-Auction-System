import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "./AuthContext";

const API_BASE = "http://localhost:8080";
const MAX_IMAGE_SIZE_MB = 15;
const MAX_IMAGE_BYTES = MAX_IMAGE_SIZE_MB * 1024 * 1024;
const FUEL_TYPES = ["Petrol", "Diesel", "Electric", "Hybrid", "LPG"];

function formatFileSize(bytes) {
  if (bytes < 1024 * 1024) {
    return `${Math.round(bytes / 1024)} KB`;
  }
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

const initialForm = {
  brand: "",
  model: "",
  year: "",
  fuelType: "Petrol",
  engineCapacity: "",
  description: "",
  vin: "",
  image: "",
  startPrice: "",
  minIncrement: "",
  endTime: "",
};

function CreateAuctionForm() {
  const { token } = useAuth();
  const navigate = useNavigate();
  const [formData, setFormData] = useState(initialForm);
  const [imageFile, setImageFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setErrorMessage(null);
  };

  const handleImageChange = (e) => {
    const file = e.target.files?.[0];
    if (file && file.size > MAX_IMAGE_BYTES) {
      setImageFile(null);
      e.target.value = "";
      setErrorMessage(
        `Zdjęcie jest za duże (${formatFileSize(file.size)}). Maksymalny rozmiar to ${MAX_IMAGE_SIZE_MB} MB.`
      );
      return;
    }
    setImageFile(file || null);
    setErrorMessage(null);
  };

  const uploadImage = async () => {
    if (!imageFile) return formData.image || null;

    const body = new FormData();
    body.append("file", imageFile);

    const response = await fetch(`${API_BASE}/api/upload`, {
      method: "POST",
      body,
    });

    if (!response.ok) {
      if (response.status === 413) {
        throw new Error(
          `Zdjęcie jest za duże. Maksymalny rozmiar to ${MAX_IMAGE_SIZE_MB} MB.`
        );
      }
      const err = await response.json().catch(() => ({}));
      throw new Error(err.message || "Nie udało się przesłać zdjęcia");
    }

    const data = await response.json();
    return `${API_BASE}${data.path}`;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setErrorMessage(null);

    try {
      setUploading(!!imageFile);
      const imageUrl = await uploadImage();
      setUploading(false);

      const payload = {
        vehicle: {
          brand: formData.brand.trim(),
          model: formData.model.trim(),
          year: Number(formData.year),
          fuelType: formData.fuelType,
          engineCapacity: Number(formData.engineCapacity),
          description: formData.description.trim() || null,
          vin: formData.vin.trim(),
          image: imageUrl,
        },
        startPrice: Number(formData.startPrice),
        minIncrement: Number(formData.minIncrement),
        endTime: formData.endTime,
        status: "ACTIVE",
      };

      const response = await fetch(`${API_BASE}/api/auctions`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const err = await response.json().catch(() => ({}));
        throw new Error(err.message || "Nie udało się utworzyć aukcji");
      }

      navigate("/auctions");
    } catch (error) {
      setErrorMessage(error.message);
    } finally {
      setUploading(false);
      setSubmitting(false);
    }
  };

  const minEndTime = new Date(Date.now() + 60 * 60 * 1000)
    .toISOString()
    .slice(0, 16);

  return (
    <div className="w-100 px-3">
      <div className="card border-0 shadow mx-auto" style={{ maxWidth: "720px" }}>
        <div className="card-body p-4 p-md-5">
          <h1 className="h4 fw-bold mb-1">Wystaw aukcję</h1>
          <p className="text-muted small mb-4">
            Uzupełnij dane pojazdu i parametry licytacji.
          </p>

          {errorMessage && (
            <div className="alert alert-danger" role="alert">
              {errorMessage}
            </div>
          )}

          <form onSubmit={handleSubmit} noValidate>
            <h2 className="h6 fw-semibold mb-3">Pojazd</h2>
            <div className="row g-3">
              <div className="col-md-6">
                <label htmlFor="brand" className="form-label">
                  Marka
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="brand"
                  name="brand"
                  value={formData.brand}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="col-md-6">
                <label htmlFor="model" className="form-label">
                  Model
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="model"
                  name="model"
                  value={formData.model}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="col-md-4">
                <label htmlFor="year" className="form-label">
                  Rok produkcji
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="year"
                  name="year"
                  min="1900"
                  max="2100"
                  value={formData.year}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="col-md-4">
                <label htmlFor="fuelType" className="form-label">
                  Paliwo
                </label>
                <select
                  className="form-select"
                  id="fuelType"
                  name="fuelType"
                  value={formData.fuelType}
                  onChange={handleChange}
                  required
                >
                  {FUEL_TYPES.map((fuel) => (
                    <option key={fuel} value={fuel}>
                      {fuel}
                    </option>
                  ))}
                </select>
              </div>
              <div className="col-md-4">
                <label htmlFor="engineCapacity" className="form-label">
                  Pojemność (cm³)
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="engineCapacity"
                  name="engineCapacity"
                  min="1"
                  value={formData.engineCapacity}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="col-12">
                <label htmlFor="vin" className="form-label">
                  VIN
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="vin"
                  name="vin"
                  value={formData.vin}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="col-12">
                <label htmlFor="description" className="form-label">
                  Opis (opcjonalnie)
                </label>
                <textarea
                  className="form-control"
                  id="description"
                  name="description"
                  rows="3"
                  value={formData.description}
                  onChange={handleChange}
                />
              </div>
              <div className="col-12">
                <label htmlFor="imageFile" className="form-label">
                  Zdjęcie (opcjonalnie)
                </label>
                <input
                  type="file"
                  className="form-control"
                  id="imageFile"
                  accept="image/jpeg,image/png,image/webp"
                  onChange={handleImageChange}
                />
                <div className="form-text">
                  JPEG, PNG lub WebP, maks. {MAX_IMAGE_SIZE_MB} MB.
                </div>
              </div>
            </div>

            <h2 className="h6 fw-semibold mb-3 mt-4">Aukcja</h2>
            <div className="row g-3">
              <div className="col-md-6">
                <label htmlFor="startPrice" className="form-label">
                  Cena wywoławcza (PLN)
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="startPrice"
                  name="startPrice"
                  min="1"
                  step="0.01"
                  value={formData.startPrice}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="col-md-6">
                <label htmlFor="minIncrement" className="form-label">
                  Minimalna podwyżka (PLN)
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="minIncrement"
                  name="minIncrement"
                  min="1"
                  step="0.01"
                  value={formData.minIncrement}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="col-12">
                <label htmlFor="endTime" className="form-label">
                  Koniec aukcji
                </label>
                <input
                  type="datetime-local"
                  className="form-control"
                  id="endTime"
                  name="endTime"
                  min={minEndTime}
                  value={formData.endTime}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>

            <div className="d-flex flex-wrap gap-2 mt-4">
              <button
                type="submit"
                className="btn btn-primary"
                disabled={submitting}
              >
                {submitting
                  ? uploading
                    ? "Przesyłanie zdjęcia…"
                    : "Zapisywanie…"
                  : "Opublikuj aukcję"}
              </button>
              <button
                type="button"
                className="btn btn-outline-secondary"
                onClick={() => navigate("/auctions")}
                disabled={submitting}
              >
                Anuluj
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default CreateAuctionForm;
