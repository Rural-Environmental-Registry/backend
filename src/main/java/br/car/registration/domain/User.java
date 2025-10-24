package br.car.registration.domain;

import br.car.registration.domain.attributes.AttributableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends AttributableEntity {
    private String idKeycloak;
    private String firstName;
    private String lastName;
    private String idNational;
    private String email;
}
