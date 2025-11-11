package com.rafaelperez.tiendaonline.business;

import com.rafaelperez.tiendaonline.business.dto.SellerDTO;
import com.rafaelperez.tiendaonline.business.service.impl.SellerServiceImpl;
import com.rafaelperez.tiendaonline.persistenceLayer.dao.SellerDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit Tests para SellerServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias
 * - Ejecución rápida
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SellerService - Unit Tests")
public class SellerServiceTest {

    @Mock
    private SellerDAO sellerDAO;

    @InjectMocks
    private SellerServiceImpl sellerService;

    private SellerDTO validSellerDTO;
    private Long validSellerId;

    @BeforeEach
    void setUp() {
        validSellerId = 1L;
        validSellerDTO = new SellerDTO();
        validSellerDTO.setId(validSellerId);
        validSellerDTO.setName("Carlos Perez");
        validSellerDTO.setEmail("carlos.perez@example.com");
        validSellerDTO.setPhone("3001112233");
        validSellerDTO.setAddress("Calle Falsa 123");
        validSellerDTO.setCreatedAt(LocalDateTime.now());
        validSellerDTO.setUpdatedAt(LocalDateTime.now());
    }

    // ---------- CREATE ----------

    @Test
    @DisplayName("CREATE - vendedor válido retorna vendedor creado")
    void createSeller_validData_returnsCreatedSeller() {
        // Arrange
        SellerDTO toCreate = new SellerDTO();
        toCreate.setName("Carlos Perez");
        toCreate.setEmail("carlos.perez@example.com");
        toCreate.setPhone("3001112233");

        SellerDTO persisted = new SellerDTO();
        persisted.setId(validSellerId);
        persisted.setName(toCreate.getName());
        persisted.setEmail(toCreate.getEmail());
        persisted.setPhone(toCreate.getPhone());

        when(sellerDAO.existsByEmail(toCreate.getEmail())).thenReturn(false);
        when(sellerDAO.save(any(SellerDTO.class))).thenReturn(persisted);

        // Act
        SellerDTO result = sellerService.createSeller(toCreate);

        // Assert - estado
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validSellerId);
        assertThat(result.getEmail()).isEqualTo(toCreate.getEmail());

        // Assert - comportamiento
        verify(sellerDAO, times(1)).existsByEmail(toCreate.getEmail());
        ArgumentCaptor<SellerDTO> captor = ArgumentCaptor.forClass(SellerDTO.class);
        verify(sellerDAO, times(1)).save(captor.capture());
        SellerDTO passed = captor.getValue();
        assertThat(passed.getId()).isNull();
        assertThat(passed.getEmail()).isEqualTo(toCreate.getEmail());
    }

    @Test
    @DisplayName("CREATE - email duplicado lanza IllegalArgumentException")
    void createSeller_emailExists_throws() {
        // Arrange
        SellerDTO toCreate = new SellerDTO();
        toCreate.setName("Carlos");
        toCreate.setEmail(validSellerDTO.getEmail());

        when(sellerDAO.existsByEmail(toCreate.getEmail())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> sellerService.createSeller(toCreate))
                .isInstanceOf(IllegalArgumentException.class);

        verify(sellerDAO, times(1)).existsByEmail(toCreate.getEmail());
        verify(sellerDAO, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - formato de email inválido lanza IllegalArgumentException")
    void createSeller_invalidEmailFormat_throws() {
        // Arrange
        SellerDTO toCreate = new SellerDTO();
        toCreate.setName("Carlos");
        toCreate.setEmail("bad-email-format");

        // Act & Assert (validación previa; no debe llamar al DAO)
        assertThatThrownBy(() -> sellerService.createSeller(toCreate))
                .isInstanceOf(IllegalArgumentException.class);

        verify(sellerDAO, never()).existsByEmail(anyString());
        verify(sellerDAO, never()).save(any());
    }

    // ---------- READ ----------

    @Test
    @DisplayName("GET by id - vendedor existente retorna DTO")
    void getSellerById_existing_returnsSeller() {
        // Arrange
        when(sellerDAO.findById(validSellerId)).thenReturn(Optional.of(validSellerDTO));

        // Act
        SellerDTO result = sellerService.getSellerById(validSellerId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validSellerId);
        assertThat(result.getEmail()).isEqualTo(validSellerDTO.getEmail());
        verify(sellerDAO, times(1)).findById(validSellerId);
    }

    @Test
    @DisplayName("GET by id - no existente lanza RuntimeException")
    void getSellerById_notFound_throws() {
        // Arrange
        when(sellerDAO.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> sellerService.getSellerById(999L))
                .isInstanceOf(RuntimeException.class);

        verify(sellerDAO, times(1)).findById(999L);
    }

    @Test
    @DisplayName("GET all - retorna lista (no vacío)")
    void getAllSellers_nonEmpty() {
        // Arrange
        when(sellerDAO.findAll()).thenReturn(List.of(validSellerDTO));

        // Act
        List<SellerDTO> results = sellerService.getAllSellers();

        // Assert
        assertThat(results).isNotEmpty();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getEmail()).isEqualTo(validSellerDTO.getEmail());
        verify(sellerDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("GET all - retorna lista vacía correctamente")
    void getAllSellers_empty() {
        // Arrange
        when(sellerDAO.findAll()).thenReturn(List.of());

        // Act
        List<SellerDTO> results = sellerService.getAllSellers();

        // Assert
        assertThat(results).isEmpty();
        verify(sellerDAO, times(1)).findAll();
    }

    // ---------- UPDATE ----------

    @Test
    @DisplayName("UPDATE - actualiza campos no nulos y preserva email")
    void updateSeller_valid_updatesAndKeepEmail() {
        // Arrange
        SellerDTO existing = new SellerDTO();
        existing.setId(validSellerId);
        existing.setName("Carlos Old");
        existing.setEmail(validSellerDTO.getEmail());
        existing.setPhone("3001112233");
        existing.setAddress("Old address");

        SellerDTO update = new SellerDTO();
        update.setName("Carlos New");
        update.setEmail("attempt.change@example.com"); // intento de cambio, debe preservarse

        SellerDTO updated = new SellerDTO();
        updated.setId(validSellerId);
        updated.setName("Carlos New");
        updated.setEmail(existing.getEmail());
        updated.setPhone(existing.getPhone());
        updated.setAddress(existing.getAddress());

        when(sellerDAO.findById(validSellerId)).thenReturn(Optional.of(existing));
        //when(sellerDAO.update(eq(validSellerId), any(SellerDTO.class))).thenReturn(Optional.of(updated));
        when(sellerDAO.update(eq(validSellerId), any(SellerDTO.class))).thenAnswer(invocation -> {
            SellerDTO passed = invocation.getArgument(1);

            // Simulamos que el DAO preserva el email original
            passed.setEmail(existing.getEmail());

            return Optional.of(passed);
        });

        // Act
        SellerDTO result = sellerService.updateSeller(validSellerId, update);

        // Assert - estado
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Carlos New");
        assertThat(result.getEmail()).isEqualTo(existing.getEmail());

        // Assert - comportamiento
        ArgumentCaptor<SellerDTO> captor = ArgumentCaptor.forClass(SellerDTO.class);
        verify(sellerDAO, times(1)).update(eq(validSellerId), captor.capture());
        SellerDTO passed = captor.getValue();
        assertThat(passed.getEmail()).isEqualTo(existing.getEmail());
    }

    @Test
    @DisplayName("UPDATE - vendedor no existente lanza RuntimeException")
    void updateSeller_notFound_throws() {
        // Arrange
        when(sellerDAO.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> sellerService.updateSeller(999L, validSellerDTO))
                .isInstanceOf(RuntimeException.class);

        verify(sellerDAO, times(1)).findById(999L);
        verify(sellerDAO, never()).update(anyLong(), any());
    }

    // ---------- DELETE ----------

    @Test
    @DisplayName("DELETE - vendedor existente se elimina correctamente")
    void deleteSeller_existing_deletesSuccessfully() {
        // Arrange
        // Important: SellerServiceImpl first calls getSellerById(id) -> debemos stubear findById
        when(sellerDAO.findById(validSellerId)).thenReturn(Optional.of(validSellerDTO));
        when(sellerDAO.deleteById(validSellerId)).thenReturn(true);

        // Act & Assert (no debe lanzar excepción)
        assertThatCode(() -> sellerService.deleteSeller(validSellerId))
                .doesNotThrowAnyException();

        // Verificaciones
        verify(sellerDAO, times(1)).findById(validSellerId);
        verify(sellerDAO, times(1)).deleteById(validSellerId);
    }

    @Test
    @DisplayName("DELETE - vendedor no existente lanza RuntimeException")
    void deleteSeller_notFound_throws() {
        // Arrange
        when(sellerDAO.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> sellerService.deleteSeller(999L))
                .isInstanceOf(RuntimeException.class);

        verify(sellerDAO, times(1)).findById(999L);
        verify(sellerDAO, never()).deleteById(anyLong());
    }
}
