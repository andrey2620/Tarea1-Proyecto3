package com.project.demo.rest.producto;

import com.project.demo.logic.entity.producto.Producto;
import com.project.demo.logic.entity.producto.ProductoRepository;
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
@RequestMapping("/productos")
public class ProductoRestController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()") // Permitir a todos los usuarios autenticados
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        List<Producto> productos = productoRepository.findAll();
        return new GlobalResponseHandler().handleResponse("Productos recuperados correctamente",
                productos, HttpStatus.OK, request);
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')") // Solo SUPER_ADMIN puede crear productos
    public ResponseEntity<?> addProducto(@RequestBody Producto producto, HttpServletRequest request) {
        productoRepository.save(producto);
        return new GlobalResponseHandler().handleResponse("Producto creado correctamente",
                producto, HttpStatus.CREATED, request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')") // Solo SUPER_ADMIN puede actualizar productos
    public ResponseEntity<?> updateProducto(@PathVariable Long id, @RequestBody Producto producto, HttpServletRequest request) {
        Optional<Producto> productoExistente = productoRepository.findById(id);
        if (productoExistente.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Producto no encontrado", HttpStatus.NOT_FOUND, request);
        }

        Producto actualizado = productoExistente.get();
        actualizado.setNombre(producto.getNombre());
        actualizado.setDescripcion(producto.getDescripcion());
        actualizado.setPrecio(producto.getPrecio());
        actualizado.setCantidadStock(producto.getCantidadStock());
        actualizado.setCategoria(producto.getCategoria());

        productoRepository.save(actualizado);
        return new GlobalResponseHandler().handleResponse("Producto actualizado correctamente",
                actualizado, HttpStatus.OK, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')") // Solo SUPER_ADMIN puede eliminar productos
    public ResponseEntity<?> deleteProducto(@PathVariable Long id, HttpServletRequest request) {
        Optional<Producto> productoExistente = productoRepository.findById(id);
        if (productoExistente.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Producto no encontrado", HttpStatus.NOT_FOUND, request);
        }

        productoRepository.deleteById(id);
        return new GlobalResponseHandler().handleResponse("Producto eliminado correctamente",
                HttpStatus.OK, request);
    }
}
