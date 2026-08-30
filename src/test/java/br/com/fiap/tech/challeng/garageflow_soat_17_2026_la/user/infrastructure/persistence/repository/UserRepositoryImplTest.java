package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.infrastructure.persistence.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.UserRole;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.infrastructure.persistence.document.UserDocument;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.infrastructure.persistence.mongo.UserMongoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private UserMongoRepository mongoRepository;

    @InjectMocks
    private UserRepositoryImpl repository;

    @Test
    void shouldSaveAndReturnDomain() {
        User domain = new User();
        domain.setId(null);
        domain.setFullName("Test User");
        domain.setEmail("t@test.com");
        domain.setPassword("pwd");
        domain.setStatus("ACTIVE");
        domain.setRole(UserRole.OPERATOR);

        UserDocument saved = new UserDocument();
        saved.setId("10");
        saved.setFullName(domain.getFullName());
        saved.setEmail(domain.getEmail());
        saved.setPassword(domain.getPassword());
        saved.setStatus(domain.getStatus());
        saved.setRole(domain.getRole());

        when(mongoRepository.save(any(UserDocument.class))).thenReturn(saved);

        User result = repository.save(domain);

        assertNotNull(result);
        assertEquals("10", result.getId());
        assertEquals("Test User", result.getFullName());
        assertEquals("t@test.com", result.getEmail());
        assertEquals(UserRole.OPERATOR, result.getRole());

        ArgumentCaptor<UserDocument> captor = ArgumentCaptor.forClass(UserDocument.class);
        verify(mongoRepository).save(captor.capture());
        assertEquals(UserRole.OPERATOR, captor.getValue().getRole());
    }

    @Test
    void shouldDelegateExistsByRole() {
        when(mongoRepository.existsByRole(UserRole.ADMIN)).thenReturn(true);
        assertTrue(repository.existsByRole(UserRole.ADMIN));

        when(mongoRepository.existsByRole(UserRole.CUSTOMER)).thenReturn(false);
        assertFalse(repository.existsByRole(UserRole.CUSTOMER));
    }

    @Test
    void shouldThrowWhenSaveNull() {
        assertThrows(NullPointerException.class, () -> repository.save(null));
    }

    @Test
    void shouldDelegateExistsById() {
        when(mongoRepository.existsById("1")).thenReturn(true);
        assertTrue(repository.existsById("1"));
    }

    @Test
    void shouldDeleteById() {
        doNothing().when(mongoRepository).deleteById("1");
        repository.deleteById("1");
        verify(mongoRepository).deleteById("1");
    }

    @Test
    void shouldFindById() {
        UserDocument doc = new UserDocument();
        doc.setId("2");
        doc.setFullName("F2");
        when(mongoRepository.findById("2")).thenReturn(Optional.of(doc));

        Optional<User> result = repository.findById("2");

        assertTrue(result.isPresent());
        assertEquals("2", result.get().getId());
        assertEquals("F2", result.get().getFullName());
    }

    @Test
    void shouldReturnEmptyWhenFindByIdNotFound() {
        when(mongoRepository.findById("x")).thenReturn(Optional.empty());
        assertTrue(repository.findById("x").isEmpty());
    }

    @Test
    void shouldFindAll() {
        UserDocument a = new UserDocument(); a.setId("1"); a.setFullName("A");
        UserDocument b = new UserDocument(); b.setId("2"); b.setFullName("B");
        when(mongoRepository.findAll()).thenReturn(List.of(a, b));

        var result = repository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void shouldFindByEmail() {
        UserDocument doc = new UserDocument();
        doc.setId("5");
        doc.setEmail("e@e.com");
        when(mongoRepository.findByEmail("e@e.com")).thenReturn(Optional.of(doc));

        Optional<User> result = repository.findByEmail("e@e.com");

        assertTrue(result.isPresent());
        assertEquals("e@e.com", result.get().getEmail());
    }

    @Test
    void shouldFindByFullNameContainingIgnoreCase() {
        UserDocument doc = new UserDocument(); doc.setId("7"); doc.setFullName("Charlie");
        when(mongoRepository.findByFullNameContainingIgnoreCase("char")).thenReturn(List.of(doc));

        var result = repository.findByFullNameContainingIgnoreCase("char");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Charlie", result.get(0).getFullName());
    }
}

