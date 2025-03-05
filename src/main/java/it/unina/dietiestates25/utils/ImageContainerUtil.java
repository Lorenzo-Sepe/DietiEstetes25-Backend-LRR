package it.unina.dietiestates25.utils;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import it.unina.dietiestates25.exception.InternalServerErrorException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Component
@AllArgsConstructor
public class ImageContainerUtil {

    private final BlobContainerClient blobContainerClient;

    public String uploadImage(MultipartFile file, String nomePath) {
        try {
            String estensione = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String currentDate = LocalDate.now().toString();
            nomePath += "-" + currentDate + estensione;
            // Get a BlobClient to upload the image file
            BlobClient blobClient = blobContainerClient.getBlobClient(nomePath);
            blobClient.upload(file.getInputStream(), file.getSize(), true);
            return blobClient.getBlobUrl();
        } catch (IOException e) {
            throw new InternalServerErrorException("Errore nel caricamento dell'immagine: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Errore nel caricamento dell'immagine: " + e.getMessage());
        }
    }
}