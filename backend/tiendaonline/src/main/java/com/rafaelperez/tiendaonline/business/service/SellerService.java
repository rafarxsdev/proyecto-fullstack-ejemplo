package com.rafaelperez.tiendaonline.business.service;

import com.rafaelperez.tiendaonline.business.dto.SellerDTO;

import java.util.List;

public interface SellerService {

    /**
     * Crear un nuevo vendedor
     *
     * VALIDACIONES:
     * - Email único en el sistema
     * - Datos obligatorios presentes
     * - Formato básico de email
     *
     * @param sellerDTO Datos del vendedor (id será null)
     * @return DTO del vendedor creado con ID generado
     */
    SellerDTO createSeller(SellerDTO sellerDTO);

    /**
     * Buscar vendedor por ID
     *
     * @param id ID del vendedor
     * @return DTO del vendedor encontrado
     * @throws RuntimeException Si el vendedor no existe
     */
    SellerDTO getSellerById(Long id);

    /**
     * Obtener todos los vendedores
     *
     * @return Lista de todos los vendedores
     */
    List<SellerDTO> getAllSellers();

    /**
     * Actualizar vendedor existente
     *
     * REGLAS:
     * - Email no se puede cambiar (regla de negocio)
     * - Solo actualiza campos no null del DTO
     * - Mantiene validaciones de negocio
     *
     * @param id ID del vendedor a actualizar
     * @param sellerDTO Datos a actualizar (campos null se ignoran)
     * @return DTO del vendedor actualizado
     * @throws RuntimeException Si el vendedor no existe
     */
    SellerDTO updateSeller(Long id, SellerDTO sellerDTO);

    /**
     * Eliminar vendedor
     *
     * REGLA DE NEGOCIO:
     * - No se puede eliminar vendedor que tiene productos asociados
     * - Verificación de integridad referencial
     *
     * @param id ID del vendedor a eliminar
     * @throws RuntimeException Si el vendedor no existe
     * @throws IllegalStateException Si el vendedor tiene productos
     */
    void deleteSeller(Long id);
}
