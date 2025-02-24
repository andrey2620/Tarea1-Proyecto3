package com.project.demo.rest.categoria;

import com.project.demo.logic.entity.categoria.Categoria;
import com.project.demo.logic.entity.categoria.CategoriaRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categorias")
public class CategoriaRestController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        List<Categoria> categorias = categoriaRepository.findAll();
        return new GlobalResponseHandler().handleResponse("Categorías recuperadas correctamente",
                categorias, HttpStatus.OK, request);
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> addCategoria(@RequestBody Categoria categoria, HttpServletRequest request) {
        categoriaRepository.save(categoria);
        return new GlobalResponseHandler().handleResponse("Categoría creada correctamente",
                categoria, HttpStatus.CREATED, request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateCategoria(@PathVariable Long id, @RequestBody Categoria categoria, HttpServletRequest request) {
        Optional<Categoria> categoriaExistente = categoriaRepository.findById(id);
        if (categoriaExistente.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Categoría no encontrada", HttpStatus.NOT_FOUND, request);
        }

        Categoria actualizada = categoriaExistente.get();
        actualizada.setNombre(categoria.getNombre());
        actualizada.setDescripcion(categoria.getDescripcion());

        categoriaRepository.save(actualizada);
        return new GlobalResponseHandler().handleResponse("Categoría actualizada correctamente",
                actualizada, HttpStatus.OK, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteCategoria(@PathVariable Long id, HttpServletRequest request) {
        Optional<Categoria> categoriaExistente = categoriaRepository.findById(id);
        if (categoriaExistente.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Categoría no encontrada", HttpStatus.NOT_FOUND, request);
        }

        categoriaRepository.deleteById(id);
        return new GlobalResponseHandler().handleResponse("Categoría eliminada correctamente",
                HttpStatus.OK, request);
    }
}
