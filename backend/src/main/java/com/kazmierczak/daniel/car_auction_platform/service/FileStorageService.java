package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.exception.InvalidFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageService {

    public static final String URL_PREFIX = "/uploads/";

    private static final Map<String, String> ALLOWED_IMAGE_TYPES = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp"
    );

    private final Path uploadDir;

    public FileStorageService(@Value("${app.upload-dir}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not create upload directory " + this.uploadDir, e);
        }
    }

    public Path getUploadDir() {
        return uploadDir;
    }

    // Zwraca publiczny adres zapisanego pliku, np. "/uploads/3f2a...c1.jpg"
    public String storeImage(MultipartFile file) {
        String extension = ALLOWED_IMAGE_TYPES.get(file.getContentType());
        if (extension == null) {
            throw new InvalidFileException("Dozwolone formaty zdjęć: JPG, PNG, WEBP");
        }

        // Nazwa jest generowana, a nie brana od klienta, żeby nie dało się
        // podać ścieżki typu "../../plik" i zapisać czegoś poza uploads/
        String filename = UUID.randomUUID() + extension;
        try {
            Files.copy(file.getInputStream(), uploadDir.resolve(filename));
        } catch (IOException e) {
            throw new UncheckedIOException("Could not store file " + filename, e);
        }
        return URL_PREFIX + filename;
    }

    public void delete(String imageUrl) {
        if (imageUrl == null || !imageUrl.startsWith(URL_PREFIX)) {
            return;
        }
        Path filename = Paths.get(imageUrl.substring(URL_PREFIX.length())).getFileName();
        try {
            Files.deleteIfExists(uploadDir.resolve(filename));
        } catch (IOException ignored) {
        }
    }
}
