package com.rafaelperez.tiendaonline.business.service;

import com.rafaelperez.tiendaonline.business.dto.ProductDTO;

import java.util.List;


public interface ProductService {

    /**
     * Crear un nuevo producto
     *
     * VALIDACIONES:
     * - Vendedor debe existir
     * - Datos obligatorios presentes
     * - Precio positivo, stock no negativo
     *
     * @param productDTO Datos del producto (id será null)
     * @return DTO del producto creado con ID generado
     */
    ProductDTO createProduct(ProductDTO productDTO);

    /**
     * Buscar producto por ID
     *
     * @param id ID del producto
     * @return DTO del producto encontrado
     * @throws RuntimeException Si el producto no existe
     */
    ProductDTO getProductById(Long id);

    /**
     * Obtener todos los productos
     *
     * @return Lista de todos los productos
     */
    List<ProductDTO> getAllProducts();

    /**
     * Actualizar producto existente
     *
     * REGLAS:
     * - No se puede cambiar el vendedor
     * - Solo actualiza campos no null del DTO
     * - Mantiene validaciones de negocio
     *
     * @param id ID del producto a actualizar
     * @param productDTO Datos a actualizar (campos null se ignoran)
     * @return DTO del producto actualizado
     * @throws RuntimeException Si el producto no existe
     */
    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    /**
     * Eliminar producto
     *
     * @param id ID del producto a eliminar
     * @throws RuntimeException Si el producto no existe
     */
    void deleteProduct(Long id);
}