package it.unina.dietiestates25.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureBlobConfig {

    @Bean
    public BlobContainerClient blobContainerClient() {
        //recuperalo dallo enviromet di intelij
        String connectionString = System.getenv("stringBlobContainer");
        String containerName = "upload"; // Sostituisci con il nome del tuo contenitore

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        return blobServiceClient.getBlobContainerClient(containerName);
    }
}