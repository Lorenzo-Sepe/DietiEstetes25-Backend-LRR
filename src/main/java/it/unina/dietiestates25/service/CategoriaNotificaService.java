package it.unina.dietiestates25.service;

import it.unina.dietiestates25.entity.CategoriaNotifica;
import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.repository.CategoriaNotificaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CategoriaNotificaService {

    private  final CategoriaNotificaRepository categoriaRepository;

    public CategoriaNotifica getCategoriaNotifica(CategoriaNotificaName name) {

        return categoriaRepository.findByCategoriaName(name).get();
    }

    public CategoriaNotifica getCategoriaNotifica(int id){

        return categoriaRepository.findById(id).get();
    }

}
