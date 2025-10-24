package br.car.registration.api.v1.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User request")
public class UserReq {
    private String idKeycloak;
    private String firstName;
    private String lastName;
    private String idNational;
    private String email;
}
