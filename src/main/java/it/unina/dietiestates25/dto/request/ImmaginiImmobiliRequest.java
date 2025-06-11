package it.unina.dietiestates25.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImmaginiImmobiliRequest {
    @JsonIgnore
    private MultipartFile file;
    private String descrizione;
    private String urlImmagineEsistente;
}
