package com.rafaelperez.tiendaonline.persistenceLayer.dao;

import com.rafaelperez.tiendaonline.business.dto.ProductDTO;
import com.rafaelperez.tiendaonline.persistenceLayer.entity.ProductEntity;
import com.rafaelperez.tiendaonline.persistenceLayer.mapper.ProductMapper;
import com.rafaelperez.tiendaonline.persistenceLayer.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductDAO {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * CREATE - Crear un nuevo producto
     *
     * FLUJO:
     * 1. ProductDTO -> ProductEntity (mapper usa toEntity())
     * 2. Guardar Entity en BD
     * 3. Entity guardada -> ProductDTO (con ID generado)
     *
     * NOTA:
     * - ProductDTO.id será null para CREATE
     * - Mapper ignora id, createdAt, updatedAt automáticamente
     * - Convierte sellerId -> SellerEntity con solo ID
     */
    public ProductDTO save(ProductDTO productDTO) {
        ProductEntity entity = productMapper.toEntity(productDTO);
        ProductEntity savedEntity = productRepository.save(entity);
        return productMapper.toDTO(savedEntity);
    }

    /**
     * READ - Buscar producto por ID
     *
     * @return Optional<ProductDTO> - empty si no existe
     */
    public Optional<ProductDTO> findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO);
    }

    /**
     * READ ALL - Buscar todos los productos
     *
     * NOTA: Lista completa de products -> DTOs
     * En aplicaciones grandes considerar paginación
     */
    public List<ProductDTO> findAll() {
        List<ProductEntity> entities = productRepository.findAll();
        return productMapper.toDTOList(entities);
    }

    /**
     * UPDATE - Actualizar producto existente usando @MappingTarget
     *
     * FLUJO:
     * 1. Buscar ProductEntity existente por ID
     * 2. Si existe, usar mapper.updateEntityFromDTO()
     * 3. Esto modifica la entity existente (no crea nueva)
     * 4. Guardar entity modificada
     * 5. Retornar DTO actualizado
     *
     * COMPORTAMIENTO:
     * - Campos null en ProductDTO se ignoran (IGNORE strategy)
     * - sellerId se ignora (no se puede cambiar vendedor)
     * - id, timestamps se ignoran automáticamente
     */
    public Optional<ProductDTO> update(Long id, ProductDTO productDTO) {
        return productRepository.findById(id)
                .map(existingEntity -> {
                    // Actualizar entity existente con datos del DTO
                    productMapper.updateEntityFromDTO(productDTO, existingEntity);
                    // Guardar cambios
                    ProductEntity updatedEntity = productRepository.save(existingEntity);
                    // Retornar DTO
                    return productMapper.toDTO(updatedEntity);
                });
    }

    /**
     * DELETE - Eliminar producto por ID
     *
     * @return boolean - true si se eliminó, false si no existía
     */
    public boolean deleteById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * UTILIDAD - Verificar si existe producto por ID
     *
     * Método auxiliar para validaciones rápidas sin cargar entity completa
     */
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    /**
     * UTILIDAD - Contar total de productos
     *
     * Para estadísticas básicas si es necesario
     */
    public long count() {
        return productRepository.count();
    }
}
