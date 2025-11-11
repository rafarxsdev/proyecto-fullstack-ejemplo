package com.rafaelperez.tiendaonline.presentationLayer.controller;

import com.rafaelperez.tiendaonline.business.dto.ProductDTO;
import com.rafaelperez.tiendaonline.business.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST simplificado para operaciones CRUD básicas de productos
 *
 * ENDPOINTS SIMPLIFICADOS:
 * - POST /api/v1/products - Crear producto
 * - GET /api/v1/products/{id} - Obtener producto por ID
 * - GET /api/v1/products - Obtener todos los productos
 * - PUT /api/v1/products/{id} - Actualizar producto
 * - DELETE /api/v1/products/{id} - Eliminar producto
 */

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Productos", description = "Operaciones CRUD para gestión de productos e inventario")
@CrossOrigin(origins = "*")
public class ProductController {
    private final ProductService productService;

    /**
     * CREATE - Crear un nuevo producto
     *
     * BODY: ProductDTO con id=null (será generado automáticamente)
     * RESPUESTA: ProductDTO con ID generado y timestamps
     */
    @PostMapping
    @Operation(
            summary = "Crear nuevo producto",
            description = "Crea un nuevo producto asociado a un vendedor existente. El ID será generado automáticamente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o vendedor no existe"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<ProductDTO> createProduct(
            @Parameter(description = "Datos del producto a crear (id debe ser null)", required = true)
            @RequestBody ProductDTO productDTO
    ) {
        log.info("POST /api/v1/products - Creando producto: {}", productDTO.getName());

        try {
            ProductDTO createdProduct = productService.createProduct(productDTO);
            log.info("Producto creado exitosamente con ID: {}", createdProduct.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear producto: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Error al crear producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * READ - Obtener producto por ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar producto por ID",
            description = "Obtiene la información completa de un producto específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado"
            )
    })
    public ResponseEntity<ProductDTO> getProductById(
            @Parameter(description = "ID del producto", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.debug("GET /api/v1/products/{} - Buscando producto", id);

        try {
            ProductDTO product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            log.warn("Producto no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * READ ALL - Obtener todos los productos
     */
    @GetMapping
    @Operation(
            summary = "Listar todos los productos",
            description = "Obtiene la lista completa de productos registrados en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de productos obtenida exitosamente",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductDTO.class)
            )
    )
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        log.debug("GET /api/v1/products - Obteniendo todos los productos");

        List<ProductDTO> products = productService.getAllProducts();
        log.debug("Se encontraron {} productos", products.size());
        return ResponseEntity.ok(products);
    }

    /**
     * UPDATE - Actualizar producto existente
     *
     * BODY: ProductDTO con campos a actualizar (campos null se ignoran)
     * REGLA: No se puede cambiar el vendedor (sellerId se ignora)
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar producto",
            description = "Actualiza la información de un producto existente. Los campos null se ignoran (actualización parcial). No se puede cambiar el vendedor."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado"
            )
    })
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(description = "ID del producto a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos a actualizar (campos null se ignoran)", required = true)
            @RequestBody ProductDTO productDTO
    ) {
        log.info("PUT /api/v1/products/{} - Actualizando producto", id);

        try {
            ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
            log.info("Producto actualizado exitosamente ID: {}", id);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Producto no encontrado para actualizar ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar producto ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE - Eliminar producto
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar producto",
            description = "Elimina un producto del sistema de forma permanente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado"
            )
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID del producto a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.info("DELETE /api/v1/products/{} - Eliminando producto", id);

        try {
            productService.deleteProduct(id);
            log.info("Producto eliminado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Producto no encontrado para eliminar ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}