package com.personal.store.users;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDto(User user);
    User toEntity(RegisterUserRequest request);
    void updateUserDto(UpdateUserRequest request, @MappingTarget User user);
}
