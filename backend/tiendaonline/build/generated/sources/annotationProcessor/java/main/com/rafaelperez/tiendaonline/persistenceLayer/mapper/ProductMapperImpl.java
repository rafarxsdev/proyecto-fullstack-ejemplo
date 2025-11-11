package com.rafaelperez.tiendaonline.persistenceLayer.mapper;

import com.rafaelperez.tiendaonline.business.dto.ProductDTO;
import com.rafaelperez.tiendaonline.persistenceLayer.entity.ProductEntity;
import com.rafaelperez.tiendaonline.persistenceLayer.entity.SellerEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-07T11:04:01-0500",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 21.0.9-ea (Debian)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDTO toDTO(ProductEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setId( entity.getId() );
        productDTO.setName( entity.getName() );
        productDTO.setDescription( entity.getDescription() );
        productDTO.setPrice( entity.getPrice() );
        productDTO.setStock( entity.getStock() );
        productDTO.setSellerId( entitySellerEntityId( entity ) );
        productDTO.setCreatedAt( entity.getCreatedAt() );
        productDTO.setUpdatedAt( entity.getUpdatedAt() );

        return productDTO;
    }

    @Override
    public List<ProductDTO> toDTOList(List<ProductEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ProductDTO> list = new ArrayList<ProductDTO>( entities.size() );
        for ( ProductEntity productEntity : entities ) {
            list.add( toDTO( productEntity ) );
        }

        return list;
    }

    @Override
    public ProductEntity toEntity(ProductDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ProductEntity productEntity = new ProductEntity();

        productEntity.setSellerEntity( idToSeller( dto.getSellerId() ) );
        productEntity.setName( dto.getName() );
        productEntity.setDescription( dto.getDescription() );
        productEntity.setPrice( dto.getPrice() );
        productEntity.setStock( dto.getStock() );

        return productEntity;
    }

    @Override
    public void updateEntityFromDTO(ProductDTO dto, ProductEntity entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getDescription() != null ) {
            entity.setDescription( dto.getDescription() );
        }
        if ( dto.getPrice() != null ) {
            entity.setPrice( dto.getPrice() );
        }
        if ( dto.getStock() != null ) {
            entity.setStock( dto.getStock() );
        }
    }

    private Long entitySellerEntityId(ProductEntity productEntity) {
        SellerEntity sellerEntity = productEntity.getSellerEntity();
        if ( sellerEntity == null ) {
            return null;
        }
        return sellerEntity.getId();
    }
}
