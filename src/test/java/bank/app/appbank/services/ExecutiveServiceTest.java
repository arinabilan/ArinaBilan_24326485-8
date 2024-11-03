package bank.app.appbank.services;
import bank.app.appbank.packages.entities.ExecutiveEntity;
import bank.app.appbank.packages.repositories.ExecutiveRepository;
import bank.app.appbank.packages.services.ExecutiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExecutiveServiceTest {
    @InjectMocks
    private ExecutiveService executiveService;

    @Mock
    private ExecutiveRepository executiveRepository;

    private ExecutiveEntity mockExecutive;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockExecutive = new ExecutiveEntity();
        mockExecutive.setId(1L);
        mockExecutive.setName("John");
        mockExecutive.setSurname("Doe");
        mockExecutive.setEmail("john.doe@example.com");
        mockExecutive.setPhone("123456789");
        mockExecutive.setPassword("password123");
    }

    @Test
    void testGetExecutive() {
        when(executiveRepository.findById(1L)).thenReturn(Optional.of(mockExecutive));

        ExecutiveEntity result = executiveService.getExecutive(1L);

        assertNotNull(result);
        assertEquals(mockExecutive.getId(), result.getId());
        verify(executiveRepository, times(1)).findById(1L);
    }

    @Test
    void testGetExecutiveNotFound() {
        when(executiveRepository.findById(1L)).thenReturn(Optional.empty());

        ExecutiveEntity result = executiveService.getExecutive(1L);

        assertNull(result);
        verify(executiveRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByEmailAndPasswordSuccess() {
        when(executiveRepository.findByEmail(mockExecutive.getEmail())).thenReturn(Optional.of(mockExecutive));

        ExecutiveEntity result = executiveService.getByEmailAndPassword(mockExecutive.getEmail(), mockExecutive.getPassword());

        assertNotNull(result);
        assertEquals(mockExecutive.getId(), result.getId());
        verify(executiveRepository, times(1)).findByEmail(mockExecutive.getEmail());
    }

    @Test
    void testGetByEmailAndPasswordWrongPassword() {
        when(executiveRepository.findByEmail(mockExecutive.getEmail())).thenReturn(Optional.of(mockExecutive));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                executiveService.getByEmailAndPassword(mockExecutive.getEmail(), "wrongPassword"));

        assertEquals("Constraseña incorrecta", exception.getMessage());
        verify(executiveRepository, times(1)).findByEmail(mockExecutive.getEmail());
    }

    @Test
    void testGetByEmailAndPasswordEmailNotFound() {
        when(executiveRepository.findByEmail(mockExecutive.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                executiveService.getByEmailAndPassword(mockExecutive.getEmail(), mockExecutive.getPassword()));

        assertEquals("Email no encontrado", exception.getMessage());
        verify(executiveRepository, times(1)).findByEmail(mockExecutive.getEmail());
    }

    @Test
    void testAddExecutive() {
        when(executiveRepository.save(any(ExecutiveEntity.class))).thenReturn(mockExecutive);

        ExecutiveEntity result = executiveService.addExecutive(mockExecutive);

        assertNotNull(result);
        assertEquals(mockExecutive.getId(), result.getId());
        verify(executiveRepository, times(1)).save(mockExecutive);
    }

    @Test
    void testUpdateExecutive() {
        when(executiveRepository.save(any(ExecutiveEntity.class))).thenReturn(mockExecutive);

        ExecutiveEntity result = executiveService.updateExecutive(mockExecutive);

        assertNotNull(result);
        assertEquals(mockExecutive.getId(), result.getId());
        verify(executiveRepository, times(1)).save(mockExecutive);
    }

    @Test
    void testDeleteExecutive() throws Exception {
        doNothing().when(executiveRepository).deleteById(1L);

        Boolean result = executiveService.deleteExecutive(1L);

        assertTrue(result);
        verify(executiveRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteExecutiveThrowsException() {
        doThrow(new RuntimeException("Error deleting executive")).when(executiveRepository).deleteById(1L);

        Exception exception = assertThrows(Exception.class, () -> {
            executiveService.deleteExecutive(1L);
        });

        assertEquals("Error deleting executive", exception.getMessage());
        verify(executiveRepository, times(1)).deleteById(1L);
    }
}
