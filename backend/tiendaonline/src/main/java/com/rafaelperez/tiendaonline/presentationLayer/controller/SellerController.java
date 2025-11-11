package com.rafaelperez.tiendaonline.presentationLayer.controller;

import com.rafaelperez.tiendaonline.business.dto.SellerDTO;
import com.rafaelperez.tiendaonline.business.service.SellerService;
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
 * Controlador REST para operaciones CRUD básicas de vendedores
 *
 * ENDPOINTS SIMPLIFICADOS:
 * - POST /api/v1/sellers - Crear vendedor
 * - GET /api/v1/sellers/{id} - Obtener vendedor por ID
 * - GET /api/v1/sellers - Obtener todos los vendedores
 * - PUT /api/v1/sellers/{id} - Actualizar vendedor
 * - DELETE /api/v1/sellers/{id} - Eliminar vendedor
 */

@RestController
@RequestMapping("/api/v1/sellers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vendedores", description = "Operaciones CRUD para gestión de vendedores")
@CrossOrigin(origins = "*")
public class SellerController {

    private final SellerService sellerService;

    /**
     * CREATE - Crear un nuevo vendedor
     *
     * BODY: SellerDTO con id=null (será generado automáticamente)
     * RESPUESTA: SellerDTO con ID generado y timestamps
     * VALIDACIÓN: Email único en el sistema
     */
    @PostMapping
    @Operation(
            summary = "Crear nuevo vendedor",
            description = "Crea un nuevo vendedor en el sistema. El email debe ser único. El ID será generado automáticamente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Vendedor creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SellerDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o email duplicado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<SellerDTO> createSeller(
            @Parameter(description = "Datos del vendedor a crear (id debe ser null)", required = true)
            @RequestBody SellerDTO sellerDTO
    ) {
        log.info("POST /api/v1/sellers - Creando vendedor: {}", sellerDTO.getEmail());

        try {
            SellerDTO createdSeller = sellerService.createSeller(sellerDTO);
            log.info("Vendedor creado exitosamente con ID: {}", createdSeller.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSeller);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear vendedor: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Error al crear vendedor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * READ - Obtener vendedor por ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar vendedor por ID",
            description = "Obtiene la información completa de un vendedor específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vendedor encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SellerDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vendedor no encontrado"
            )
    })
    public ResponseEntity<SellerDTO> getSellerById(
            @Parameter(description = "ID del vendedor", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.debug("GET /api/v1/sellers/{} - Buscando vendedor", id);

        try {
            SellerDTO seller = sellerService.getSellerById(id);
            return ResponseEntity.ok(seller);
        } catch (RuntimeException e) {
            log.warn("Vendedor no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * READ ALL - Obtener todos los vendedores
     */
    @GetMapping
    @Operation(
            summary = "Listar todos los vendedores",
            description = "Obtiene la lista completa de vendedores registrados en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de vendedores obtenida exitosamente",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SellerDTO.class)
            )
    )
    public ResponseEntity<List<SellerDTO>> getAllSellers() {
        log.debug("GET /api/v1/sellers - Obteniendo todos los vendedores");

        List<SellerDTO> sellers = sellerService.getAllSellers();
        log.debug("Se encontraron {} vendedores", sellers.size());
        return ResponseEntity.ok(sellers);
    }

    /**
     * UPDATE - Actualizar vendedor existente
     *
     * BODY: SellerDTO con campos a actualizar (campos null se ignoran)
     * REGLA: No se puede cambiar el email (email se ignora)
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar vendedor",
            description = "Actualiza la información de un vendedor existente. Los campos null se ignoran (actualización parcial). El email no se puede modificar."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vendedor actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SellerDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vendedor no encontrado"
            )
    })
    public ResponseEntity<SellerDTO> updateSeller(
            @Parameter(description = "ID del vendedor a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos a actualizar (campos null se ignoran, email se ignora)", required = true)
            @RequestBody SellerDTO sellerDTO
    ) {
        log.info("PUT /api/v1/sellers/{} - Actualizando vendedor", id);

        try {
            SellerDTO updatedSeller = sellerService.updateSeller(id, sellerDTO);
            log.info("Vendedor actualizado exitosamente ID: {}", id);
            return ResponseEntity.ok(updatedSeller);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Vendedor no encontrado para actualizar ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar vendedor ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE - Eliminar vendedor
     *
     * REGLA DE NEGOCIO: No se puede eliminar si tiene productos asociados
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar vendedor",
            description = "Elimina un vendedor del sistema. No se puede eliminar si tiene productos asociados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Vendedor eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vendedor no encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "No se puede eliminar: vendedor tiene productos asociados"
            )
    })
    public ResponseEntity<Void> deleteSeller(
            @Parameter(description = "ID del vendedor a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.info("DELETE /api/v1/sellers/{} - Eliminando vendedor", id);

        try {
            sellerService.deleteSeller(id);
            log.info("Vendedor eliminado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Vendedor no encontrado para eliminar ID: {}", id);
                return ResponseEntity.notFound().build();
            } else if (e instanceof IllegalStateException && e.getMessage().contains("productos")) {
                log.warn("Intento de eliminar vendedor con productos ID: {}", id);
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            log.error("Error al eliminar vendedor ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
