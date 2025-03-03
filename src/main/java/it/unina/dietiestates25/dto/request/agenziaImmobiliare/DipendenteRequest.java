package it.unina.dietiestates25.dto.request.agenziaImmobiliare;

    import io.swagger.v3.oas.annotations.media.Schema;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Pattern;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.springframework.web.multipart.MultipartFile;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
    public class DipendenteRequest {

        MultipartFile fotoProfilo;

        @NotBlank
        private String nome;

        @NotBlank
        private String cognome;

        // ruolo del dipendente
        @NotBlank
        @Pattern(regexp = "ADMIN|AGENT", message = "Il ruolo deve essere 'ADMIN' o 'AGENT'")
        @Schema(allowableValues = {"ADMIN", "AGENT"}, description = "Il ruolo deve essere 'ADMIN' o 'AGENT'")
        private String ruolo;
    }