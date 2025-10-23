package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.ImmaginiImmobiliRequest;
import it.unina.dietiestates25.entity.AnnuncioImmobiliare;
import it.unina.dietiestates25.entity.ImmaginiImmobile;
import it.unina.dietiestates25.entity.Immobile;
import it.unina.dietiestates25.utils.ImageContainerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageUploaderService {

    private final ImageContainerUtil imageContainerUtil;

    public String getDefaultAvatar(String inputString) {
        int width = 400;
        int height = 400;

        if (inputString == null || inputString.trim().isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }

        // if the string starts with Agente or Manager, remove it
        if (inputString.startsWith("agente") || inputString.startsWith("manager")) {
            inputString = inputString.substring(inputString.indexOf(" ") + 1);
        }

        // Get the initial letter, uppercase
        char initial = Character.toUpperCase(inputString.trim().charAt(0));

        // Simple hash function
        int hash = 0;
        for (char c : inputString.toCharArray()) {
            hash = (hash << 5) - hash + c;
        }

        // Convert hash to HSL values
        float hue = (Math.abs(hash) % 360); // Hue: 0 - 359
        float saturation = 0.6f; // Saturation: 60%
        float lightness = 0.6f; // Lightness: 60%

        // Convert HSL to RGB
        int[] rgb = hslToRgb(hue, saturation, lightness);
        String colorHex = String.format("%02X%02X%02X", rgb[0], rgb[1], rgb[2]);

        return String.format("https://dummyimage.com/%dx%d/%s/FFFFFF?text=%c", width, height, colorHex, initial);
    }

    public int[] hslToRgb(float h, float s, float l) {
        float c = (1 - Math.abs(2 * l - 1)) * s;
        float x = c * (1 - Math.abs((h / 60) % 2 - 1));
        float m = l - c / 2;

        float r = 0;
        float g = 0;
        float b = 0;

        if (0 <= h && h < 60) {
            r = c;
            g = x;
            b = 0;
        } else if (60 <= h && h < 120) {
            r = x;
            g = c;
            b = 0;
        } else if (120 <= h && h < 180) {
            r = 0;
            g = c;
            b = x;
        } else if (180 <= h && h < 240) {
            r = 0;
            g = x;
            b = c;
        } else if (240 <= h && h < 300) {
            r = x;
            g = 0;
            b = c;
        } else if (300 <= h && h < 360) {
            r = c;
            g = 0;
            b = x;
        }

        int red = Math.round((r + m) * 255);
        int green = Math.round((g + m) * 255);
        int blue = Math.round((b + m) * 255);

        return new int[] { red, green, blue };
    }

    public List<MultipartFile> getListMultipartFilesFromRequest(List<ImmaginiImmobiliRequest> immaginiRequests) {
        if (immaginiRequests == null) {
            throw new IllegalArgumentException("The list of immaginiRequests cannot be null");
        }

        return immaginiRequests.stream()
                .filter(request -> request != null && request.getFile() != null) // Ensure non-null elements and files
                .map(ImmaginiImmobiliRequest::getFile)
                .toList(); // Creates an unmodifiable list
    }

    public List<String> salvaImmaginiAnnuncioToBlob(List<MultipartFile> files, int annuncioId,
            int valoreInizialeCounter) {
        AtomicInteger counter = new AtomicInteger(valoreInizialeCounter);
        return files.stream()
                .map(file -> {
                    String nomePath = "annuncio" + annuncioId + "-" + counter.getAndIncrement();
                    return imageContainerUtil.uploadImage(file, nomePath);
                })
                .toList();
    }

    public void updateImmagini(List<ImmaginiImmobiliRequest> request, AnnuncioImmobiliare annuncio) {

        annuncio.getImmobile().getImmagini().clear();
        int numeroImmaginiGiaInserite = addImmaginiGiaEsistentiToNuovaListaImmagini(request, annuncio);
        List<String> urls = salvaImmaginiAnnuncioToBlob(getListaImmaginiFile(request), annuncio.getId(),
                numeroImmaginiGiaInserite + 1);
        addNuoveImmaginiToNuovaListaImmagini(urls, request, annuncio);

    }

    public int addImmaginiGiaEsistentiToNuovaListaImmagini(List<ImmaginiImmobiliRequest> request,
            AnnuncioImmobiliare annuncio) {

        int countInserimenti = 0;

        for (ImmaginiImmobiliRequest immagine : request) {

            if (immagine.getUrlImmagineEsistente() != null) {
                ImmaginiImmobile immagineGiaEsistente = ImmaginiImmobile.builder()
                        .immobile(annuncio.getImmobile())
                        .descrizione(immagine.getDescrizione())
                        .url(immagine.getUrlImmagineEsistente())
                        .build();

                annuncio.getImmobile().getImmagini().add(immagineGiaEsistente);

                countInserimenti++;
            }
        }

        return countInserimenti;
    }

    public List<MultipartFile> getListaImmaginiFile(List<ImmaginiImmobiliRequest> request) {

        List<MultipartFile> immaginiFile = new ArrayList<>();

        for (ImmaginiImmobiliRequest immagine : request) {

            if (immagine.getFile() != null) {

                immaginiFile.add(immagine.getFile());
            }
        }

        return immaginiFile;
    }

    public void updateImmaginiAnnuncio(List<ImmaginiImmobiliRequest> immaginiRequest, AnnuncioImmobiliare annuncio) {

        int immobileId = annuncio.getImmobile().getId();

        List<MultipartFile> immaginiFile = getListMultipartFilesFromRequest(immaginiRequest);

        List<String> urlImmagini = salvaImmaginiAnnuncioToBlob(immaginiFile, immobileId, 0);

        if (annuncio.getImmobile().getImmagini() != null)
            annuncio.getImmobile().getImmagini().clear();

        List<ImmaginiImmobile> immaginiImmobili = getListaImmaginiImmobili(urlImmagini, immaginiRequest,
                annuncio.getImmobile());

        if (annuncio.getImmobile().getImmagini() != null)
            annuncio.getImmobile().getImmagini().addAll(immaginiImmobili);
        else
            annuncio.getImmobile().setImmagini(immaginiImmobili);
    }

    private List<ImmaginiImmobile> getListaImmaginiImmobili(List<String> urlImmagini,
            List<ImmaginiImmobiliRequest> request, Immobile immobile) {

        List<ImmaginiImmobile> immaginiImmobile = new ArrayList<>();

        if (urlImmagini == null || request == null) {
            throw new IllegalArgumentException("Le liste di URL immagini o richieste sono null.");
        }

        if (urlImmagini.size() != request.size()) {
            throw new IllegalArgumentException("Il numero di URL immagini non corrisponde al numero di richieste.");
        }

        for (int i = 0; i < request.size(); i++) {
            ImmaginiImmobile immagine = ImmaginiImmobile.builder()
                    .immobile(immobile)
                    .descrizione(request.get(i).getDescrizione())
                    .url(urlImmagini.get(i))
                    .build();
            immaginiImmobile.add(immagine);
        }

        return immaginiImmobile;
    }

    public void addNuoveImmaginiToNuovaListaImmagini(List<String> urls, List<ImmaginiImmobiliRequest> request,
            AnnuncioImmobiliare annuncio) {

        int countFile = 0;

        for (ImmaginiImmobiliRequest immaginiImmobiliRequest : request) {
            if (immaginiImmobiliRequest.getFile() != null) {
                ImmaginiImmobile immaginiImmobile = ImmaginiImmobile.builder()
                        .immobile(annuncio.getImmobile())
                        .descrizione(immaginiImmobiliRequest.getDescrizione())
                        .url(urls.get(countFile))
                        .build();

                annuncio.getImmobile().getImmagini().add(immaginiImmobile);
                countFile++;
            }
        }
    }
}