package com.rafaelperez.tiendaonline.persistenceLayer.repository;

import com.rafaelperez.tiendaonline.persistenceLayer.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio para operaciones de base de datos con productos
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    // - save(entity) - CREATE/UPDATE
    // - findById(id) - READ
    // - findAll() - READ ALL
    // - deleteById(id) - DELETE
}