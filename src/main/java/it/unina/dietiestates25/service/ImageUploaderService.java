package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.ImmaginiImmobiliRequest;
import it.unina.dietiestates25.entity.AnnuncioImmobiliare;
import it.unina.dietiestates25.entity.ImmaginiImmobile;
import it.unina.dietiestates25.utils.ImageContainerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageUploaderService {

    private final ImageContainerUtil imageContainerUtil;

    public String salvaFotoProfilo(MultipartFile file, int userId) {
        String nomePath = "fotoprofilo" + userId;
        return imageContainerUtil.uploadImage(file, nomePath);
    }


   public List<String> salvaImmaginiAnnuncioToBlob(List<MultipartFile> files, int annuncioId,int valoreInizialeCounter) {
       AtomicInteger counter = new AtomicInteger(valoreInizialeCounter);
       return files.stream()
               .map(file -> {
                   String nomePath = "annuncio" + annuncioId + "-" + counter.getAndIncrement();
                   return imageContainerUtil.uploadImage(file, nomePath);
               })
               .collect(Collectors.toList());
   }

    public void updateImmagini(List<ImmaginiImmobiliRequest> request, AnnuncioImmobiliare annuncio){

        annuncio.getImmobile().getImmagini().clear();
        int numeroImmaginiGiaInserite = addImmaginiGiaEsistentiToNuovaListaImmagini(request,annuncio);
        List<String> urls = salvaImmaginiAnnuncioToBlob(getListaImmaginiFile(request), annuncio.getId(),numeroImmaginiGiaInserite+1);
        addNuoveImmaginiToNuovaListaImmagini(urls,request,annuncio);

    }

    public int addImmaginiGiaEsistentiToNuovaListaImmagini(List<ImmaginiImmobiliRequest> request, AnnuncioImmobiliare annuncio){

        int countInserimenti = 0;

        for(ImmaginiImmobiliRequest immagine : request ){

            if(immagine.getUrlImmagineEsistente() != null){
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

    public List<MultipartFile> getListaImmaginiFile(List<ImmaginiImmobiliRequest> request){

        List<MultipartFile> immaginiFile = new ArrayList<>();

        for(ImmaginiImmobiliRequest immagine : request ){

            if(immagine.getFile() != null){

                immaginiFile.add(immagine.getFile());
            }
        }

        return immaginiFile;
    }

    public void addNuoveImmaginiToNuovaListaImmagini(List<String> urls, List<ImmaginiImmobiliRequest> request,AnnuncioImmobiliare annuncio) {

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