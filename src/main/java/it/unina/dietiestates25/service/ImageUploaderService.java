package it.unina.dietiestates25.service;

import it.unina.dietiestates25.utils.ImageContainerUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ImageUploaderService {

    private final ImageContainerUtil imageContainerUtil;

    public String salvaFotoProfilo(MultipartFile file, int userId) {
        String nomePath = "fotoprofilo" + userId;
        return imageContainerUtil.uploadImage(file, nomePath);
    }

    public List<String> salvaImmaginiAnnuncio(List<MultipartFile> files, int annuncioId) {
        return files.stream()
                .map(file -> {
                    String nomePath = "annuncio" + annuncioId;
                    return imageContainerUtil.uploadImage(file, nomePath);
                })
                .collect(Collectors.toList());
    }
}