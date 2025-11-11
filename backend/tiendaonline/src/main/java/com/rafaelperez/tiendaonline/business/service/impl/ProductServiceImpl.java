package com.rafaelperez.tiendaonline.business.service.impl;

import com.rafaelperez.tiendaonline.business.dto.ProductDTO;
import com.rafaelperez.tiendaonline.business.service.ProductService;
import com.rafaelperez.tiendaonline.business.service.SellerService;
import com.rafaelperez.tiendaonline.persistenceLayer.dao.ProductDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;
    private final SellerService sellerService;

    /**
     * CREATE - Crear nuevo producto
     *
     * FLUJO:
     * 1. Validar datos de entrada
     * 2. Verificar que vendedor existe
     * 3. Usar DAO.save() que maneja toda la persistencia
     * 4. DAO retorna ProductDTO con ID generado
     */
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        log.info("Creando nuevo producto: {}", productDTO.getName());

        // 1. Validaciones de negocio
        validateProductData(productDTO);

        // 2. Verificar que el vendedor existe (lanza excepción si no existe)
        sellerService.getSellerById(productDTO.getSellerId());

        // 3. Crear producto usando DAO (maneja Entity+Mapper internamente)
        ProductDTO result = productDAO.save(productDTO);

        log.info("Producto creado exitosamente con ID: {}", result.getId());
        /*System.out.println("Creando producto: " +
                productDTO.getName());*/
        return result;
    }

    /**
     * READ - Buscar producto por ID
     */
    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        log.debug("Buscando producto por ID: {}", id);

        return productDAO.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado con ID: {}", id);
                    return new RuntimeException("Producto no encontrado con ID: " + id);
                });
    }

    /**
     * READ ALL - Obtener todos los productos
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        log.debug("Obteniendo todos los productos");
        return productDAO.findAll();
    }

    /**
     * UPDATE - Actualizar producto existente
     *
     * FLUJO:
     * 1. Verificar que producto existe
     * 2. Validar datos de actualización
     * 3. Usar DAO.update() que maneja @MappingTarget internamente
     * 4. DAO retorna ProductDTO actualizado
     */
    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        log.info("Actualizando producto ID: {}", id);

        // 1. Verificar que producto existe (reutilizamos getProductById)
        getProductById(id);

        // 2. Validar datos de actualización (solo campos no null)
        validateProductUpdateData(productDTO);

        // 3. Actualizar usando DAO (maneja updateEntityFromDTO internamente)
        ProductDTO result = productDAO.update(id, productDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar producto con ID: " + id));

        log.info("Producto actualizado exitosamente ID: {}", id);
        return result;
    }

    /**
     * DELETE - Eliminar producto
     */
    @Override
    public void deleteProduct(Long id) {
        log.info("Eliminando producto ID: {}", id);

        // Usar DAO que verifica existencia y elimina
        boolean deleted = productDAO.deleteById(id);

        if (!deleted) {
            log.warn("Intento de eliminar producto inexistente ID: {}", id);
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }

        log.info("Producto eliminado exitosamente ID: {}", id);
    }

    /**
     * MÉTODO PRIVADO: Validaciones para CREATE
     * Valida todos los campos obligatorios y reglas de negocio
     */
    private void validateProductData(ProductDTO productDTO) {
        // Nombre obligatorio y no vacío
        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

        // Validar longitud del nombre
        if (productDTO.getName().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }

        // Precio obligatorio y positivo
        if (productDTO.getPrice() == null || productDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        // Stock no puede ser negativo
        if (productDTO.getStock() == null || productDTO.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        // SellerId obligatorio
        if (productDTO.getSellerId() == null) {
            throw new IllegalArgumentException("El vendedor es obligatorio");
        }

        // duplicado intencional
        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

    }

    /**
     * MÉTODO PRIVADO: Validaciones para UPDATE
     * Solo valida campos que NO son null (actualización parcial)
     */
    private void validateProductUpdateData(ProductDTO productDTO) {
        // Si name está presente, validarlo
        if (productDTO.getName() != null) {
            if (productDTO.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            if (productDTO.getName().length() > 100) {
                throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
            }
        }

        // Si price está presente, validarlo
        if (productDTO.getPrice() != null && productDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        // Si stock está presente, validarlo
        if (productDTO.getStock() != null && productDTO.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        // Nota: sellerId se ignora en UPDATE (no se puede cambiar vendedor)
    }
}
