package com.rafaelperez.tiendaonline.business.service.impl;

import com.rafaelperez.tiendaonline.business.dto.SellerDTO;
import com.rafaelperez.tiendaonline.business.service.SellerService;
import com.rafaelperez.tiendaonline.persistenceLayer.dao.SellerDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SellerServiceImpl implements SellerService {
    private final SellerDAO sellerDAO;

    /**
     * CREATE - Crear nuevo vendedor
     *
     * FLUJO:
     * 1. Validar datos de entrada
     * 2. Verificar email único usando DAO
     * 3. Usar DAO.save() que maneja toda la persistencia
     * 4. DAO retorna SellerDTO con ID generado
     */
    @Override
    public SellerDTO createSeller(SellerDTO sellerDTO) {
        log.info("Creando nuevo vendedor con email: {}", sellerDTO.getEmail());

        // 1. Validaciones de negocio
        validateSellerData(sellerDTO);

        // 2. Verificar email único usando DAO - REGLA DE NEGOCIO CRÍTICA
        if (sellerDAO.existsByEmail(sellerDTO.getEmail())) {
            log.warn("Intento de crear vendedor con email duplicado: {}", sellerDTO.getEmail());
            throw new IllegalArgumentException("Ya existe un vendedor con el email: " + sellerDTO.getEmail());
        }

        // 3. Crear vendedor usando DAO (maneja Entity+Mapper internamente)
        SellerDTO result = sellerDAO.save(sellerDTO);

        log.info("Vendedor creado exitosamente con ID: {}", result.getId());
        return result;
    }

    /**
     * READ - Buscar vendedor por ID
     */
    @Override
    @Transactional(readOnly = true)
    public SellerDTO getSellerById(Long id) {
        log.debug("Buscando vendedor por ID: {}", id);

        return sellerDAO.findById(id)
                .orElseThrow(() -> {
                    log.warn("Vendedor no encontrado con ID: {}", id);
                    return new RuntimeException("Vendedor no encontrado con ID: " + id);
                });
    }

    /**
     * READ ALL - Obtener todos los vendedores
     */
    @Override
    @Transactional(readOnly = true)
    public List<SellerDTO> getAllSellers() {
        log.debug("Obteniendo todos los vendedores");
        return sellerDAO.findAll();
    }

    /**
     * UPDATE - Actualizar vendedor existente
     *
     * FLUJO:
     * 1. Verificar que vendedor existe
     * 2. Validar datos de actualización
     * 3. Usar DAO.update() que maneja @MappingTarget internamente
     * 4. DAO retorna SellerDTO actualizado
     *
     * NOTA: Email NO se puede cambiar (ignorado por el mapper)
     */
    @Override
    public SellerDTO updateSeller(Long id, SellerDTO sellerDTO) {
        log.info("Actualizando vendedor ID: {}", id);

        // 1. Verificar que vendedor existe (reutilizamos getSellerById)
        getSellerById(id);

        // 2. Validar datos de actualización (solo campos no null)
        validateSellerUpdateData(sellerDTO);

        // 3. Actualizar usando DAO (maneja updateEntityFromDTO internamente)
        SellerDTO result = sellerDAO.update(id, sellerDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar vendedor con ID: " + id));

        log.info("Vendedor actualizado exitosamente ID: {}", id);
        return result;
    }

    /**
     * DELETE - Eliminar vendedor
     *
     * REGLA DE NEGOCIO IMPORTANTE:
     * No se puede eliminar vendedor que tiene productos asociados
     * Verificamos integridad referencial usando DAO
     */
    @Override
    public void deleteSeller(Long id) {
        log.info("Eliminando vendedor ID: {}", id);

        // 1. Verificar que vendedor existe
        getSellerById(id);

        // 2. REGLA DE NEGOCIO: Verificar que no tenga productos usando DAO
        if (sellerDAO.hasProducts(id)) {
            log.warn("Intento de eliminar vendedor con productos. ID: {}", id);
            throw new IllegalStateException("No se puede eliminar el vendedor porque tiene productos asociados");
        }

        // 3. Eliminar vendedor usando DAO
        boolean deleted = sellerDAO.deleteById(id);

        if (!deleted) {
            log.warn("Error al eliminar vendedor ID: {}", id);
            throw new RuntimeException("Error al eliminar vendedor con ID: " + id);
        }

        log.info("Vendedor eliminado exitosamente ID: {}", id);
    }

    /**
     * MÉTODO PRIVADO: Validaciones para CREATE
     * Valida todos los campos obligatorios y reglas de negocio
     */
    private void validateSellerData(SellerDTO sellerDTO) {
        // Nombre obligatorio y no vacío
        if (sellerDTO.getName() == null || sellerDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del vendedor es obligatorio");
        }

        // Validar longitud del nombre
        if (sellerDTO.getName().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }

        // Email obligatorio y no vacío
        if (sellerDTO.getEmail() == null || sellerDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del vendedor es obligatorio");
        }

        // Validación básica de formato de email
        if (!isValidEmailFormat(sellerDTO.getEmail())) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }
    }

    /**
     * MÉTODO PRIVADO: Validaciones para UPDATE
     * Solo valida campos que NO son null (actualización parcial)
     */
    private void validateSellerUpdateData(SellerDTO sellerDTO) {
        // Si name está presente, validarlo
        if (sellerDTO.getName() != null) {
            if (sellerDTO.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            if (sellerDTO.getName().length() > 100) {
                throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
            }
        }

        // Nota: Email se ignora en UPDATE (no se puede cambiar)
        // Si phone está presente, se podría validar formato si fuera necesario
        // Si address está presente, se podría validar longitud si fuera necesario
    }

    /**
     * MÉTODO PRIVADO: Validación básica de formato de email
     * Implementación simple para validar estructura mínima
     */
    private boolean isValidEmailFormat(String email) {
        // Validación básica: debe contener @ y al menos un punto después de @
        return email.contains("@") &&
                email.indexOf("@") < email.lastIndexOf(".") &&
                email.length() > 5;
    }
}
