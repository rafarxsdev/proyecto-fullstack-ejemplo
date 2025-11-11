package com.rafaelperez.tiendaonline.persistenceLayer.mapper;

import com.rafaelperez.tiendaonline.business.dto.SellerDTO;
import com.rafaelperez.tiendaonline.persistenceLayer.entity.SellerEntity;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper único para todas las operaciones CRUD con vendedores
 * Usa mapeo explícito e inheritInverseConfiguration
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface SellerMapper {

    /**
     * Entity -> DTO (para READ)
     * Mapeo explícito de todos los campos
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    SellerDTO toDTO(SellerEntity entity);

    /**
     * Lista de entities -> Lista de DTOs (para READ ALL)
     */
    List<SellerDTO> toDTOList(List<SellerEntity> entities);

    /**
     * DTO -> Entity (para CREATE)
     * Usa inheritInverseConfiguration del método toDTO y sobrescribe específicos
     */
    @InheritInverseConfiguration(name = "toDTO")
    @Mapping(target = "id", ignore = true)                    // Se autogenera
    @Mapping(target = "createdAt", ignore = true)            // Lo maneja JPA
    @Mapping(target = "updatedAt", ignore = true)            // Lo maneja JPA
    @Mapping(target = "products", ignore = true)             // No se mapea la lista de productos
    SellerEntity toEntity(SellerDTO dto);

    /**
     * DTO -> Entity existente (para UPDATE)
     * Hereda la configuración inversa y añade configuraciones específicas para UPDATE
     */
    @InheritInverseConfiguration(name = "toDTO")
    @Mapping(target = "id", ignore = true)                    // No se modifica
    @Mapping(target = "email", ignore = true)                // Email no se puede cambiar
    @Mapping(target = "createdAt", ignore = true)            // No se modifica
    @Mapping(target = "updatedAt", ignore = true)            // Lo maneja JPA
    @Mapping(target = "products", ignore = true)             // No se toca la lista de productos
    // Si un campo en el DTO de origen es null, NO toques el campo correspondiente en el entity de destino
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(SellerDTO dto, @MappingTarget SellerEntity entity);
}
