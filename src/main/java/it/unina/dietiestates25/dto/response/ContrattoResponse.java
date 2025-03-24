package it.unina.dietiestates25.dto.response;


import it.unina.dietiestates25.entity.Contratto;
import it.unina.dietiestates25.entity.ContrattoAffitto;
import it.unina.dietiestates25.entity.ContrattoVendita;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ContrattoResponse {

    String tipoContratto;
    ContrattoVenditaResponse contrattoVenditaResponse;
    ContrattoAffittoResponse contrattoAffittoResponse;

    public static ContrattoResponse fromEntityToDto(Contratto contratto){
        ContrattoResponse contrattoResponse = ContrattoResponse.builder().build();

        if(contratto instanceof ContrattoAffitto contrattoAffitto){
            contrattoResponse.setContrattoAffittoResponse(ContrattoAffittoResponse.fromEntityToDto(contrattoAffitto));
            contrattoResponse.setTipoContratto("AFFITTO");

        }else if(contratto instanceof ContrattoVendita contrattoVendita){
            contrattoResponse.setTipoContratto("VENDITA");
            contrattoResponse.setContrattoVenditaResponse(ContrattoVenditaResponse.getContrattoVendita(contrattoVendita)); 
        }
        return contrattoResponse;
    }

}
