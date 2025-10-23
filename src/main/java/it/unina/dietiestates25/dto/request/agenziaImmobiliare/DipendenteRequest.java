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
        @NotBlank
        private String nome;

        @NotBlank
        private String cognome;

        // ruolo del dipendente
        @NotBlank
        @Schema(allowableValues = {"MANAGER", "AGENT"}, description = "Il ruolo deve essere 'MANAGER' o 'AGENT'")
        private String ruolo;

        public static DipendenteRequest creaFondatoreAgenzia(AgenziaImmobiliareRequest request) {
            return DipendenteRequest.builder()
                    .nome(request.getNomeFondatore())
                    .cognome(request.getCognomeFondatore())
                    .ruolo("MANAGER")
                    .build();
        }
    }