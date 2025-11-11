package com.rafaelperez.tiendaonline.persistenceLayer.repository;

import com.rafaelperez.tiendaonline.persistenceLayer.entity.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de base de datos con vendedores
 */
@Repository
public interface SellerRepository extends JpaRepository<SellerEntity, Long> {
    // - save(entity) - CREATE/UPDATE
    // - findById(id) - READ
    // - findAll() - READ ALL
    // - deleteById(id) - DELETE
}
