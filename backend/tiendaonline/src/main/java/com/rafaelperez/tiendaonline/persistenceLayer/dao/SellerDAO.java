package com.rafaelperez.tiendaonline.persistenceLayer.dao;

import com.rafaelperez.tiendaonline.business.dto.SellerDTO;
import com.rafaelperez.tiendaonline.persistenceLayer.entity.SellerEntity;
import com.rafaelperez.tiendaonline.persistenceLayer.mapper.SellerMapper;
import com.rafaelperez.tiendaonline.persistenceLayer.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SellerDAO {
    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;

    /**
     * CREATE - Crear un nuevo vendedor
     *
     * FLUJO:
     * 1. SellerDTO -> SellerEntity (mapper usa toEntity())
     * 2. Guardar Entity en BD
     * 3. Entity guardada -> SellerDTO (con ID generado)
     *
     * NOTA:
     * - SellerDTO.id será null para CREATE
     * - Mapper ignora id, createdAt, updatedAt automáticamente
     * - Mapper ignora products (lista vacía por defecto)
     */
    public SellerDTO save(SellerDTO sellerDTO) {
        SellerEntity entity = sellerMapper.toEntity(sellerDTO);
        SellerEntity savedEntity = sellerRepository.save(entity);
        return sellerMapper.toDTO(savedEntity);
    }

    /**
     * READ - Buscar vendedor por ID
     *
     * @return Optional<SellerDTO> - empty si no existe
     */
    public Optional<SellerDTO> findById(Long id) {
        return sellerRepository.findById(id)
                .map(sellerMapper::toDTO);
    }

    /**
     * READ ALL - Buscar todos los vendedores
     *
     * NOTA: Lista completa de sellers -> DTOs
     */
    public List<SellerDTO> findAll() {
        List<SellerEntity> entities = sellerRepository.findAll();
        return sellerMapper.toDTOList(entities);
    }

    /**
     * UPDATE - Actualizar vendedor existente usando @MappingTarget
     *
     * FLUJO:
     * 1. Buscar SellerEntity existente por ID
     * 2. Si existe, usar mapper.updateEntityFromDTO()
     * 3. Esto modifica la entity existente (no crea nueva)
     * 4. Guardar entity modificada
     * 5. Retornar DTO actualizado
     *
     * COMPORTAMIENTO:
     * - Campos null en SellerDTO se ignoran (IGNORE strategy)
     * - email se ignora (no se puede cambiar email)
     * - id, timestamps, products se ignoran automáticamente
     */
    public Optional<SellerDTO> update(Long id, SellerDTO sellerDTO) {
        return sellerRepository.findById(id)
                .map(existingEntity -> {
                    // Actualizar entity existente con datos del DTO
                    sellerMapper.updateEntityFromDTO(sellerDTO, existingEntity);
                    // Guardar cambios
                    SellerEntity updatedEntity = sellerRepository.save(existingEntity);
                    // Retornar DTO
                    return sellerMapper.toDTO(updatedEntity);
                });
    }

    /**
     * DELETE - Eliminar vendedor por ID
     *
     * @return boolean - true si se eliminó, false si no existía
     */
    public boolean deleteById(Long id) {
        if (sellerRepository.existsById(id)) {
            sellerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * VALIDACIÓN - Verificar si existe vendedor con ese email
     *
     * CRÍTICO para regla de negocio: email único
     * Usado en CREATE para verificar unicidad
     *
     * IMPLEMENTACIÓN ALTERNATIVA:
     * Si SellerRepository no tiene findByEmail() o existsByEmail(),
     * podemos usar una consulta manual o cargar todos y filtrar
     * (no recomendado para muchos registros, pero funciona para CRUD básico)
     */
    public boolean existsByEmail(String email) {
        return sellerRepository.findAll().stream()
                .anyMatch(entity -> email.equals(entity.getEmail()));
    }

    /**
     * UTILIDAD - Verificar si existe vendedor por ID
     *
     * Método auxiliar para validaciones rápidas sin cargar entity completa
     */
    public boolean existsById(Long id) {
        return sellerRepository.existsById(id);
    }

    /**
     * UTILIDAD - Contar total de vendedores
     *
     * Para estadísticas básicas si es necesario
     */
    public long count() {
        return sellerRepository.count();
    }

    /**
     * VALIDACIÓN - Verificar integridad referencial
     *
     * Verifica si seller tiene productos antes de permitir eliminación
     * Usado en DELETE para aplicar regla de negocio
     *
     * IMPLEMENTACIÓN CORREGIDA:
     * Carga la entity y verifica si la lista de productos no está vacía
     * Si la relación está configurada como LAZY, se puede verificar sin cargar productos
     */
    public boolean hasProducts(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .map(entity -> {
                    // Si products es null o está vacío, no tiene productos
                    return entity.getProducts() != null && !entity.getProducts().isEmpty();
                })
                .orElse(false); // Si el seller no existe, retorna false
    }
}
