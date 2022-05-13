package dev.vality.messages.domain.mapper;

import dev.vality.messages.domain.User;

public class UserMapper {

    public static User fromThrift(dev.vality.damsel.messages.User userThrift) {
        return User.builder()
                .id(userThrift.getUserId())
                .email(userThrift.getEmail())
                .fullName(userThrift.getFullname())
                .build();
    }

    public static dev.vality.damsel.messages.User toThrift(User user) {
        return new dev.vality.damsel.messages.User(
                user.getId(),
                user.getEmail(),
                user.getFullName()
        );
    }
}
