package com.rafaelperez.tiendaonline.persistenceLayer.mapper;

import com.rafaelperez.tiendaonline.business.dto.ProductDTO;
import com.rafaelperez.tiendaonline.persistenceLayer.entity.ProductEntity;
import com.rafaelperez.tiendaonline.persistenceLayer.entity.SellerEntity;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper único para todas las operaciones CRUD con productos
 * Usa mapeo explícito e inheritInverseConfiguration
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ProductMapper {

    /**
     * Entity -> DTO (para READ)
     * Mapeo explícito de todos los campos
     */
    // Target es el DTO y el source es el Entity
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "stock", source = "stock")
    @Mapping(target = "sellerId", source = "sellerEntity.id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ProductDTO toDTO(ProductEntity entity);
    //ProductDTO convertirEntityADTO(ProductEntity entity);

    /**
     * Lista de entities -> Lista de DTOs (para READ ALL)
     */
    List<ProductDTO> toDTOList(List<ProductEntity> entities);

    /**
     * DTO -> Entity (para CREATE)
     * Usa inheritInverseConfiguration del método toDTO y sobrescribe específicos
     */
    // Target es el Entity y el source es el DTO
    @InheritInverseConfiguration(name = "toDTO")
    @Mapping(target = "id", ignore = true)                    // Se autogenera
    @Mapping(target = "createdAt", ignore = true)            // Lo maneja JPA
    @Mapping(target = "updatedAt", ignore = true)            // Lo maneja JPA
    @Mapping(target = "sellerEntity", source = "sellerId", qualifiedByName = "idToSeller")
    ProductEntity toEntity(ProductDTO dto);
    // ProductEntity covertirDTOAEntity(ProductDTO dto);
    /**
     * DTO -> Entity existente (para UPDATE)
     * Hereda la configuración inversa y añade configuraciones específicas para UPDATE
     */
    @InheritInverseConfiguration(name = "toDTO")
    @Mapping(target = "id", ignore = true)                    // No se modifica
    @Mapping(target = "createdAt", ignore = true)            // No se modifica
    @Mapping(target = "updatedAt", ignore = true)            // Lo maneja JPA
    @Mapping(target = "sellerEntity", ignore = true)         // No se cambia vendedor
    // Si un campo en el DTO de origen es null, NO toques el campo correspondiente en el entity de destino
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ProductDTO dto, @MappingTarget ProductEntity entity);

    /**
     * Método auxiliar: Convierte sellerId en SellerEntity con solo el ID
     * JPA manejará la relación correctamente con este stub
     */
    @Named("idToSeller")
    default SellerEntity idToSeller(Long sellerId) {
        if (sellerId == null) {
            return null;
        }
        SellerEntity seller = new SellerEntity();
        seller.setId(sellerId);
        return seller;
    }
}
