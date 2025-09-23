package org.example.Services;

import org.example.Entities.Categoria;
import org.example.Repositories.CategoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService extends BaseService<Categoria,Long, CategoriaRepository>{

    public Page<Categoria> getCategoriasPaginados(int page, int size) throws Exception {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return repository.findAll(pageable);
        }catch (Exception e){
            throw new Exception("Error al obtener categorías paginados: " + e.getMessage());
        }
    }

}
