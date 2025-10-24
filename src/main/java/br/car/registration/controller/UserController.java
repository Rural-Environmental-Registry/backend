package br.car.registration.controller;

import br.car.registration.api.v1.UserApi;
import br.car.registration.api.v1.request.UserReq;
import br.car.registration.api.v1.response.UserRes;
import br.car.registration.domain.User;
import br.car.registration.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Tag(name = "Users", description = "Manage users, including creation, retrieval, update, and deletion.")
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public List<UserRes> getAll() {
        return userService.findAll();
    }

    @Override
    public UserRes getById(UUID id) {
        return userService.findById(id);
    }

    @Override
    public UserRes getByIdKeycloak(String idKeycloak) {
        return userService.findByIdKeycloak(idKeycloak);
    }

    @Override
    public User createUser(UserReq userReq) {
        return userService.createUser(userReq);
    }

    @Override
    public User updateUserKeycloak(String idKeycloak, UserReq userReq) {
        return userService.updateUserKeycloak(idKeycloak,userReq);
    }

    @Override
    public User updateUser(UUID id, UserReq userReq) {
        return userService.updateUser(id,userReq);
    }

    @Override
    public void deleteUser(UUID id) {
        userService.deleteUser(id);
    }
}
