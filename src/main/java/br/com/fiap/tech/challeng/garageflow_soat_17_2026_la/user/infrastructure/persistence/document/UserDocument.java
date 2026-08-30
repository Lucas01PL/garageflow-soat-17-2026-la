package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.infrastructure.persistence.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.UserRole;

@ToString
@NoArgsConstructor
@Data
@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;

    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Status cannot be blank")
    private String status;

    private UserRole role;

}

