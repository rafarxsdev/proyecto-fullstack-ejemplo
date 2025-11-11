package com.rafaelperez.tiendaonline.business;

import com.rafaelperez.tiendaonline.business.dto.ProductDTO;
import com.rafaelperez.tiendaonline.business.dto.SellerDTO;
import com.rafaelperez.tiendaonline.business.service.SellerService;
import com.rafaelperez.tiendaonline.business.service.impl.ProductServiceImpl;
import com.rafaelperez.tiendaonline.persistenceLayer.dao.ProductDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit Tests para ProductServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias
 * - Ejecución rápida
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService - Unit Tests")
public class ProductServiceTest {
    // DEPENDENCIAS MOCKEADAS
    @Mock
    private ProductDAO productDAO;

    @Mock
    private SellerService sellerService;

    // CLASE BAJO PRUEBA (System Under Test)
    @InjectMocks
    private ProductServiceImpl productService;

    // DATOS DE PRUEBA
    private ProductDTO validProductDTO;
    private SellerDTO validSellerDTO;
    private Long validSellerId;
    private Long validProductId;

    /**
     * Configuración ejecutada ANTES de cada test
     * Inicializa datos comunes reutilizables
     */
    @BeforeEach
    void setUp() {
        validSellerId = 1L;
        validProductId = 1L;

        // Seller válido para usar en tests
        validSellerDTO = new SellerDTO(
                validSellerId,
                "Test Seller",
                "test@seller.com",
                "123456789",
                "Test Address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Producto válido para usar en tests
        validProductDTO = new ProductDTO(
                null, // id null para CREATE
                "Laptop Test",
                "Descripción de prueba",
                BigDecimal.valueOf(1500.00),
                10,
                validSellerId,
                null,
                null
        );
    }

    // ==================== CREATE PRODUCT TESTS ====================

    @Test
    @DisplayName("CREATE - Producto válido debe retornar producto creado con ID")
    void createProduct_ValidData_ShouldReturnCreatedProduct() {
        // ARRANGE (Given) - Preparar el escenario
        ProductDTO expectedProduct = new ProductDTO(
                validProductId,
                validProductDTO.getName(),
                validProductDTO.getDescription(),
                validProductDTO.getPrice(),
                validProductDTO.getStock(),
                validSellerId,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Mock del comportamiento del sellerService
        when(sellerService.getSellerById(validSellerId))
                .thenReturn(validSellerDTO);

        // Mock del comportamiento del productDAO
        when(productDAO.save(any(ProductDTO.class)))
                .thenReturn(expectedProduct);

        // ACT (When) - Ejecutar el método bajo prueba
        ProductDTO result = productService.createProduct(validProductDTO);

        // ASSERT (Then) - Verificar los resultados
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validProductId);
        assertThat(result.getName()).isEqualTo("Laptop Test");
        assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(1500.00));
        assertThat(result.getStock()).isEqualTo(10);
        assertThat(result.getSellerId()).isEqualTo(validSellerId);

        // Verificar que se llamaron los métodos correctos
        verify(sellerService, times(1)).getSellerById(validSellerId);
        verify(productDAO, times(1)).save(any(ProductDTO.class));
    }

    @Test
    @DisplayName("CREATE - Nombre null debe lanzar IllegalArgumentException")
    void createProduct_NullName_ShouldThrowException() {
        // ARRANGE
        validProductDTO.setName(null);

        // ACT & ASSERT - Usando assertThatThrownBy
        assertThatThrownBy(() -> productService.createProduct(validProductDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre del producto es obligatorio");

        // Verificar que NO se llamaron los métodos de persistencia
        verify(sellerService, never()).getSellerById(anyLong());
        verify(productDAO, never()).save(any(ProductDTO.class));
    }

    @Test
    @DisplayName("CREATE - Nombre vacío debe lanzar IllegalArgumentException")
    void createProduct_EmptyName_ShouldThrowException() {
        // ARRANGE
        validProductDTO.setName("");

        // ACT & ASSERT
        assertThatThrownBy(() -> productService.createProduct(validProductDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre del producto es obligatorio");
    }

    @Test
    @DisplayName("CREATE - Nombre muy largo debe lanzar IllegalArgumentException")
    void createProduct_NameTooLong_ShouldThrowException() {
        // ARRANGE
        String longName = "a".repeat(101); // 101 caracteres
        validProductDTO.setName(longName);

        // ACT & ASSERT
        assertThatThrownBy(() -> productService.createProduct(validProductDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre no puede exceder 100 caracteres");
    }

    @Test
    @DisplayName("CREATE - Precio null debe lanzar IllegalArgumentException")
    void createProduct_NullPrice_ShouldThrowException() {
        // ARRANGE
        validProductDTO.setPrice(null);

        // ACT & ASSERT
        assertThatThrownBy(() -> productService.createProduct(validProductDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio debe ser mayor a cero");
    }

    @Test
    @DisplayName("CREATE - Precio negativo debe lanzar IllegalArgumentException")
    void createProduct_NegativePrice_ShouldThrowException() {
        // ARRANGE
        validProductDTO.setPrice(BigDecimal.valueOf(-100));

        // ACT & ASSERT
        assertThatThrownBy(() -> productService.createProduct(validProductDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio debe ser mayor a cero");
    }

    @Test
    @DisplayName("CREATE - Precio cero debe lanzar IllegalArgumentException")
    void createProduct_ZeroPrice_ShouldThrowException() {
        // ARRANGE
        validProductDTO.setPrice(BigDecimal.ZERO);

        // ACT & ASSERT
        assertThatThrownBy(() -> productService.createProduct(validProductDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio debe ser mayor a cero");
    }

    @Test
    @DisplayName("CREATE - Stock negativo debe lanzar IllegalArgumentException")
    void createProduct_NegativeStock_ShouldThrowException() {
        // ARRANGE
        validProductDTO.setStock(-5);

        // ACT & ASSERT
        assertThatThrownBy(() -> productService.createProduct(validProductDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("stock no puede ser negativo");
    }

    @Test
    @DisplayName("CREATE - SellerId null debe lanzar IllegalArgumentException")
    void createProduct_NullSellerId_ShouldThrowException() {
        // ARRANGE
        validProductDTO.setSellerId(null);

        // ACT & ASSERT
        assertThatThrownBy(() -> productService.createProduct(validProductDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("vendedor es obligatorio");
    }

    @Test
    @DisplayName("CREATE - Vendedor inexistente debe lanzar RuntimeException")
    void createProduct_NonExistentSeller_ShouldThrowException() {
        // ARRANGE
        when(sellerService.getSellerById(validSellerId))
                .thenThrow(new RuntimeException("Vendedor no encontrado con ID: " + validSellerId));

        // ACT & ASSERT
        assertThatThrownBy(() -> productService.createProduct(validProductDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Vendedor no encontrado");

        // Verificar que NO se llamó al DAO
        verify(productDAO, never()).save(any(ProductDTO.class));
    }

    // ==================== READ PRODUCT TESTS ====================

    @Test
    @DisplayName("READ - Producto existente debe retornar producto")
    void getProductById_ExistingId_ShouldReturnProduct() {
        // ARRANGE
        ProductDTO existingProduct = new ProductDTO(
                validProductId,
                "Laptop Test",
                "Descripción",
                BigDecimal.valueOf(1500),
                10,
                validSellerId,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(productDAO.findById(validProductId))
                .thenReturn(Optional.of(existingProduct));

        // ACT
        ProductDTO result = productService.getProductById(validProductId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validProductId);
        assertThat(result.getName()).isEqualTo("Laptop Test");

        verify(productDAO, times(1)).findById(validProductId);
    }

    @Test
    @DisplayName("READ - Producto inexistente debe lanzar RuntimeException")
    void getProductById_NonExistentId_ShouldThrowException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(productDAO.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> productService.getProductById(nonExistentId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Producto no encontrado con ID: " + nonExistentId);
    }

    @Test
    @DisplayName("READ ALL - Debe retornar lista de productos")
    void getAllProducts_ShouldReturnProductList() {
        // ARRANGE
        List<ProductDTO> products = Arrays.asList(
                new ProductDTO(1L, "Product 1", "Desc 1", BigDecimal.valueOf(100), 5, 1L, null, null),
                new ProductDTO(2L, "Product 2", "Desc 2", BigDecimal.valueOf(200), 10, 1L, null, null)
        );

        when(productDAO.findAll()).thenReturn(products);

        // ACT
        List<ProductDTO> result = productService.getAllProducts();

        // ASSERT
        assertThat(result).hasSize(2);
        assertThat(result).extracting("name")
                .containsExactly("Product 1", "Product 2");

        verify(productDAO, times(1)).findAll();
    }

    // ==================== UPDATE PRODUCT TESTS ====================

    @Test
    @DisplayName("UPDATE - Datos válidos debe retornar producto actualizado")
    void updateProduct_ValidData_ShouldReturnUpdatedProduct() {
        // ARRANGE
        ProductDTO existingProduct = new ProductDTO(
                validProductId, "Old Name", "Old Desc",
                BigDecimal.valueOf(1000), 5, validSellerId, null, null
        );

        ProductDTO updateData = new ProductDTO(
                validProductId, "New Name", null,
                BigDecimal.valueOf(1500), null, null, null, null
        );

        ProductDTO updatedProduct = new ProductDTO(
                validProductId, "New Name", "Old Desc",
                BigDecimal.valueOf(1500), 5, validSellerId, null, null
        );

        when(productDAO.findById(validProductId))
                .thenReturn(Optional.of(existingProduct));
        when(productDAO.update(eq(validProductId), any(ProductDTO.class)))
                .thenReturn(Optional.of(updatedProduct));

        // ACT
        ProductDTO result = productService.updateProduct(validProductId, updateData);

        // ASSERT
        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(1500));
        assertThat(result.getStock()).isEqualTo(5); // No cambió

        verify(productDAO, times(1)).update(eq(validProductId), any(ProductDTO.class));
    }

    @Test
    @DisplayName("UPDATE - Producto inexistente debe lanzar RuntimeException")
    void updateProduct_NonExistentId_ShouldThrowException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(productDAO.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> productService.updateProduct(nonExistentId, validProductDTO))
                .isInstanceOf(RuntimeException.class);
    }

    // ==================== DELETE PRODUCT TESTS ====================

    @Test
    @DisplayName("DELETE - Producto existente debe completar sin error")
    void deleteProduct_ExistingId_ShouldComplete() {
        // ARRANGE
        when(productDAO.deleteById(validProductId)).thenReturn(true);

        // ACT & ASSERT - No debe lanzar excepción
        assertThatCode(() -> productService.deleteProduct(validProductId))
                .doesNotThrowAnyException();

        verify(productDAO, times(1)).deleteById(validProductId);
    }

    @Test
    @DisplayName("DELETE - Producto inexistente debe lanzar RuntimeException")
    void deleteProduct_NonExistentId_ShouldThrowException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(productDAO.deleteById(nonExistentId)).thenReturn(false);

        // ACT & ASSERT
        assertThatThrownBy(() -> productService.deleteProduct(nonExistentId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Producto no encontrado con ID: " + nonExistentId);
    }
}
