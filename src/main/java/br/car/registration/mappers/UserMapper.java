package br.car.registration.mappers;

import br.car.registration.api.v1.request.UserReq;
import br.car.registration.api.v1.response.UserRes;
import br.car.registration.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserReq userReq);

    UserRes toDTO(User user);
}
