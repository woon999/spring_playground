package com.example.admin.service;

import com.example.admin.model.entity.User;
import com.example.admin.model.enumclass.UserStatus;
import com.example.admin.model.network.Header;
import com.example.admin.model.network.request.UserApiRequest;
import com.example.admin.model.network.response.UserApiResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserApiLogicService extends BaseService<UserApiRequest, UserApiResponse,User> {



    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> request) {

        UserApiRequest body = request.getData();

        User user = User.builder()
                .account(body.getAccount())
                .password(body.getPassword())
                .status(UserStatus.REGISTERED)
                .phoneNumber(body.getPhoneNumber())
                .email(body.getEmail())
                .registeredAt(LocalDateTime.now())
                .build();

        User newUser = baseRepository.save(user);

        // 3. 생성된 데이터 -> userApiResponse return
        return response(newUser);
    }

    @Override
    public Header<UserApiResponse> read(Long id) {

        return baseRepository.findById(id)
                .map(user -> response(user))
                .orElseGet(
                        () -> Header.ERROR("데이터 없음")
                );
    }

    @Override
    public Header<UserApiResponse> update(Header<UserApiRequest> request) {
        // 1. data
        UserApiRequest body = request.getData();

        // 2. id -> user 데이터를 찾고
        Optional<User> optional = baseRepository.findById(body.getId());

        return optional.map(user -> {
            // 3. data -> update
            // id
            user.setAccount(body.getAccount())
                    .setPassword(body.getPassword())
                    .setStatus(body.getStatus())
                    .setPhoneNumber(body.getPhoneNumber())
                    .setEmail(body.getEmail())
                    .setRegisteredAt(body.getRegisteredAt())
                    .setUnregisteredAt(body.getUnregisteredAt());
            return user;

        })
                .map(user -> baseRepository.save(user)) // update -> newUser
                .map(updateUser -> response(updateUser)) // userApiResponse
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {
        // 1. id -> repository -> user
        Optional<User> optional = baseRepository.findById(id);

        // 2. repository -> delete
        return optional.map(user -> {
            baseRepository.delete(user);
            return Header.OK();
        })
                .orElseGet(() -> Header.ERROR("데이터 없음"));

    }

    private Header<UserApiResponse> response(User user) {
        //user -> userApiResponse
        UserApiResponse userApiResponse = UserApiResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .registeredAt(user.getRegisteredAt())
                .unregisteredAt(user.getUnregisteredAt())
                .build();

        // Header + data return
        return Header.OK(userApiResponse);
    }
}
