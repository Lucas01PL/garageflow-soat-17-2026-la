package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.config.security;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.UserRole;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserSeederTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldCreateAdminWhenNoneExists() {
        AdminUserSeeder seeder = new AdminUserSeeder(userRepository, passwordEncoder, "admin@garageflow.com", "admin123");
        when(userRepository.existsByRole(UserRole.ADMIN)).thenReturn(false);
        when(passwordEncoder.encode("admin123")).thenReturn("encoded-password");

        seeder.run();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("admin@garageflow.com", captor.getValue().getEmail());
        assertEquals("encoded-password", captor.getValue().getPassword());
        assertEquals(UserRole.ADMIN, captor.getValue().getRole());
    }

    @Test
    void shouldNotCreateAdminWhenOneAlreadyExists() {
        AdminUserSeeder seeder = new AdminUserSeeder(userRepository, passwordEncoder, "admin@garageflow.com", "admin123");
        when(userRepository.existsByRole(UserRole.ADMIN)).thenReturn(true);

        seeder.run();

        verify(userRepository, never()).save(any(User.class));
    }
}
