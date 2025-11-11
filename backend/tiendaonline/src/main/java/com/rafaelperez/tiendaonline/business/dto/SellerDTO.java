package com.rafaelperez.tiendaonline.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO COMPLETO de vendedores - Para LECTURA/RESPUESTAS
 * 
 * ¿POR QUÉ ESTE DTO?
 * - Se usa cuando DEVOLVEMOS información completa al cliente
 * - Incluye campos auto-generados (ID, fechas) que el cliente necesita ver
 * - Contiene toda la información disponible del vendedor
 * - Usado en: GET /sellers/{id}, GET /sellers, respuestas de POST/PUT
 * 
 * NOTA: Este DTO incluye campos READ_ONLY que el cliente no puede modificar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del vendedor")
public class SellerDTO {

    @Schema(description = "ID único del vendedor", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre completo del vendedor", example = "María González Tech", required = true, maxLength = 100)
    private String name;

    @Schema(description = "Correo electrónico del vendedor", example = "maria.gonzalez@techstore.com", required = true, maxLength = 150)
    private String email;

    @Schema(description = "Número de teléfono del vendedor", example = "+57-300-1234567", maxLength = 20)
    private String phone;

    @Schema(description = "Dirección física del vendedor", example = "Carrera 15 #23-45, Bogotá, Colombia", maxLength = 255)
    private String address;

    @Schema(description = "Fecha y hora de creación del registro", example = "2025-09-07T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de última actualización", example = "2025-09-07T15:45:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}
