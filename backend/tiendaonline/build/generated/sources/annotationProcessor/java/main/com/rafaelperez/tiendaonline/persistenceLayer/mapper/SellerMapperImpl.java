package com.rafaelperez.tiendaonline.persistenceLayer.mapper;

import com.rafaelperez.tiendaonline.business.dto.SellerDTO;
import com.rafaelperez.tiendaonline.persistenceLayer.entity.SellerEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-07T11:04:02-0500",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 21.0.9-ea (Debian)"
)
@Component
public class SellerMapperImpl implements SellerMapper {

    @Override
    public SellerDTO toDTO(SellerEntity entity) {
        if ( entity == null ) {
            return null;
        }

        SellerDTO sellerDTO = new SellerDTO();

        sellerDTO.setId( entity.getId() );
        sellerDTO.setName( entity.getName() );
        sellerDTO.setEmail( entity.getEmail() );
        sellerDTO.setPhone( entity.getPhone() );
        sellerDTO.setAddress( entity.getAddress() );
        sellerDTO.setCreatedAt( entity.getCreatedAt() );
        sellerDTO.setUpdatedAt( entity.getUpdatedAt() );

        return sellerDTO;
    }

    @Override
    public List<SellerDTO> toDTOList(List<SellerEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<SellerDTO> list = new ArrayList<SellerDTO>( entities.size() );
        for ( SellerEntity sellerEntity : entities ) {
            list.add( toDTO( sellerEntity ) );
        }

        return list;
    }

    @Override
    public SellerEntity toEntity(SellerDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SellerEntity sellerEntity = new SellerEntity();

        sellerEntity.setName( dto.getName() );
        sellerEntity.setEmail( dto.getEmail() );
        sellerEntity.setPhone( dto.getPhone() );
        sellerEntity.setAddress( dto.getAddress() );

        return sellerEntity;
    }

    @Override
    public void updateEntityFromDTO(SellerDTO dto, SellerEntity entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getPhone() != null ) {
            entity.setPhone( dto.getPhone() );
        }
        if ( dto.getAddress() != null ) {
            entity.setAddress( dto.getAddress() );
        }
    }
}
