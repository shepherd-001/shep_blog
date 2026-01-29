package com.shepherd.shep_blog.mapper;

import com.shepherd.shep_blog.data.dto.request.AddTeamMemberRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterAuthorRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterReaderRequest;
import com.shepherd.shep_blog.data.dto.response.*;
import com.shepherd.shep_blog.data.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapConfig.class)
public interface UserMapper {

    VerifyEmailResponse mapToVerifyEmailResponse(User user, AuthResponse authResponse);

    @Mapping(target = "username", source = "username", qualifiedByName = "toLowerCaseTrim")
    @Mapping(target = "email", source = "email", qualifiedByName = "toLowerCaseTrim")
    @Mapping(target = "password", ignore = true)
    User mapToUser(RegisterReaderRequest request);

    @Mapping(target = "firstName", source = "firstName", qualifiedByName = "trim")
    @Mapping(target = "lastName", source = "lastName", qualifiedByName = "trim")
    @Mapping(target = "username", source = "username", qualifiedByName = "toLowerCaseTrim")
    @Mapping(target = "email", source = "email", qualifiedByName = "toLowerCaseTrim")
    @Mapping(target = "password", ignore = true)
    User mapToUser(RegisterAuthorRequest request);

    @Mapping(target = "firstName", source = "firstName", qualifiedByName = "trim")
    @Mapping(target = "lastName", source = "lastName", qualifiedByName = "trim")
    @Mapping(target = "email", source = "email", qualifiedByName = "toLowerCaseTrim")
    @Mapping(target = "username", source = "username", qualifiedByName = "toLowerCaseTrim")
    @Mapping(target = "password", ignore = true)
    User mapToUser(AddTeamMemberRequest request);

    UserResponse mapToUserResponse(User user);

    RegisterUserResponse mapToRegisterReaderResponse(User user);

    @Named("toLowerCaseTrim")
    default String toLowerCaseTrim(String value) {
        return value != null ? value.toLowerCase().trim() : null;
    }

    @Named("trim")
    default String trim(String value) {
        return value != null ? value.trim() : null;
    }
}