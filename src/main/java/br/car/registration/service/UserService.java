package br.car.registration.service;

import br.car.registration.api.v1.request.UserReq;
import br.car.registration.api.v1.response.UserRes;
import br.car.registration.domain.User;
import br.car.registration.mappers.UserMapper;
import br.car.registration.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserRes> findAll() {
        return userRepository.findAll().stream().map(userMapper::toDTO).toList();
    }

    public UserRes findById(UUID id) {
        return userRepository.findById(id).map(userMapper::toDTO).orElseThrow(EntityNotFoundException::new);
    }

    public User createUser(UserReq userReq) {
        var userOpt = userRepository.findByIdKeycloak(userReq.getIdKeycloak());
        if(userOpt.isPresent()){
            return updateUser(userOpt.get().getId(), userReq);
        }else {
            User user = userMapper.toEntity(userReq);
            return userRepository.save(user);
        }
    }

    public User updateUserKeycloak(String idKeycloak, UserReq userReq){
        var userOpt = userRepository.findByIdKeycloak(idKeycloak);
        if(userOpt.isPresent()){
            var user = userOpt.get();
            user.setFirstName(userReq.getFirstName());
            user.setLastName(userReq.getLastName());
            user.setEmail(userReq.getEmail());
            user.setIdNational(userReq.getIdNational());
            return userRepository.save(user);
        }else{
            throw new EntityNotFoundException();
        }
    }

    public User updateUser(UUID id, UserReq userReq){
        var userOpt = userRepository.findById(id);
        if(userOpt.isPresent()){
            var user = userOpt.get();
            user.setFirstName(userReq.getFirstName());
            user.setLastName(userReq.getLastName());
            user.setEmail(userReq.getEmail());
            user.setIdNational(userReq.getIdNational());
            return userRepository.save(user);
        }else{
            throw new EntityNotFoundException();
        }
    }
    public void deleteUser(UUID id){
        userRepository.deleteById(id);
    }

    public UserRes findByIdKeycloak(String idKeycloak) {
        return userRepository.findByIdKeycloak(idKeycloak).map(userMapper::toDTO).orElseThrow(EntityNotFoundException::new);
    }
}
