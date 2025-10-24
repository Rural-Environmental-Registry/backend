package br.car.registration.api.v1;

import br.car.registration.api.v1.request.UserReq;
import br.car.registration.api.v1.response.UserRes;
import br.car.registration.domain.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequestMapping("/v1/users")
public interface UserApi {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<UserRes> getAll();

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserRes getById(@PathVariable UUID id);

    @GetMapping(path = "keycloak/{idKeycloak}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserRes getByIdKeycloak(@PathVariable String idKeycloak);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    User createUser(@RequestBody UserReq userReq);

    @PutMapping(path = "keycloak/{idKeycloak}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    User updateUserKeycloak(@PathVariable String idKeycloak, @RequestBody UserReq userReq);

    @PutMapping(path = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    User updateUser(@PathVariable UUID id, @RequestBody UserReq userReq);

    @DeleteMapping(path = "/{id}")
    void deleteUser(@PathVariable UUID id);

}
