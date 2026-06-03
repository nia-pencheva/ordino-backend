package com.ordino.domain.products.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.products.model.dto.ProductRequestDTO;
import com.ordino.domain.products.model.dto.ProductResponseDTO;
import com.ordino.domain.products.service.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
@Validated
public class ProductsController {
    private ProductService productService;

    @GetMapping()
    public ResponseEntity<List<ProductResponseDTO>> getProducts(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) Boolean active
    ) {
        return ResponseEntity.ok().body(productService.getProducts(name, active, page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(productService.getProduct(id));
    }
    

    @PostMapping()
    public ResponseEntity<Void> addProduct(@Valid @RequestBody ProductRequestDTO dto) {
        productService.addProduct(dto);

        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}")
    public ResponseEntity<Void> saveProduct(@PathVariable @Positive Long id, @Valid @RequestBody ProductRequestDTO dto) {
        productService.saveProduct(id, dto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateProduct(@PathVariable @Positive Long id) {
        productService.activateProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateProduct(@PathVariable @Positive Long id) {
        productService.deactivateProduct(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable @Positive Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
