package com.rafaelperez.tiendaonline.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del producto")
public class ProductDTO {

    @Schema(description = "ID único del producto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre del producto", example = "Laptop ASUS VivoBook 15", required = true, maxLength = 100)
    private String name;

    @Schema(description = "Descripción detallada del producto", 
            example = "Laptop con procesador Intel Core i5, 8GB RAM, 256GB SSD, pantalla 15.6 pulgadas")
    private String description;

    @Schema(description = "Precio del producto en pesos colombianos", example = "2150000.00", required = true, minimum = "0.01")
    private BigDecimal price;

    @Schema(description = "Cantidad disponible en inventario", example = "15", required = true, minimum = "0")
    private Integer stock;

    @Schema(description = "ID del vendedor que ofrece el producto", example = "1", required = true)
    private Long sellerId;

    //@Schema(description = "Nombre del vendedor", example = "María González Tech", accessMode = Schema.AccessMode.READ_ONLY)
    //private String sellerName;

    //@Schema(description = "Email del vendedor", example = "maria.gonzalez@techstore.com", accessMode = Schema.AccessMode.READ_ONLY)
    //private String sellerEmail;

    @Schema(description = "Fecha y hora de creación del registro", example = "2025-09-07T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de última actualización", example = "2025-09-07T15:45:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}
