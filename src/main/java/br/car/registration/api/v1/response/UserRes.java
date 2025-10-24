package br.car.registration.api.v1.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRes {
    private UUID id;
    private String idKeycloak;
    private String firstName;
    private String lastName;
    private String idNational;
    private String email;
}
