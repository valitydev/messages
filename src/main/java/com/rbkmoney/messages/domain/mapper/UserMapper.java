package com.rbkmoney.messages.domain.mapper;

import com.rbkmoney.messages.domain.User;

public class UserMapper {

    public static User fromThrift(com.rbkmoney.damsel.messages.User userThrift) {
        return User.builder()
                .id(userThrift.getUserId())
                .email(userThrift.getEmail())
                .fullName(userThrift.getFullname())
                .build();
    }

    public static com.rbkmoney.damsel.messages.User toThrift(User user) {
        return new com.rbkmoney.damsel.messages.User(
                user.getId(),
                user.getEmail(),
                user.getFullName()
        );
    }
}
