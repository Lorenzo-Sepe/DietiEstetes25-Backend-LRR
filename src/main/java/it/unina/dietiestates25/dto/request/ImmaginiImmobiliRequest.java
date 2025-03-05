package it.unina.dietiestates25.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImmaginiImmobiliRequest {
    private MultipartFile file;
    private String descrizione;
}
