package org.example.Controllers;

import jakarta.validation.Valid;
import org.example.Entities.Base;
import org.example.Repositories.BaseRepository;
import org.example.Services.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@CrossOrigin(origins = "*")
public abstract class BaseController<T extends Base, ID, Repo extends BaseRepository<T,ID> ,
        Service extends BaseService<T, ID, Repo > > {

    protected final Service service;

    public BaseController(Service service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            return ResponseEntity.ok(service.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable ID id) {
        try {
            T entity = service.findById(id);
            if (entity == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(entity);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody T entity) {
        try {
            T createdEntity = service.save(entity);
            return ResponseEntity.ok(createdEntity);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable ID id, @Valid @RequestBody T entity) {
        try {
            T updatedEntity = service.update(id, entity);
            if (updatedEntity == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedEntity);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable ID id) {
        try {
            T deleted = service.deleteById(id);

            return ResponseEntity.ok(deleted);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}
