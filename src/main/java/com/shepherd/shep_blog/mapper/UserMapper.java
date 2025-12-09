package com.shepherd.shep_blog.mapper;

import com.shepherd.shep_blog.data.dto.request.RegisterReaderRequest;
import com.shepherd.shep_blog.data.dto.response.AuthResponse;
import com.shepherd.shep_blog.data.dto.response.RegisterUserResponse;
import com.shepherd.shep_blog.data.dto.response.VerifyEmailResponse;
import com.shepherd.shep_blog.data.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapConfig.class)
public interface UserMapper {

    VerifyEmailResponse mapToVerifyEmailResponse(User user, AuthResponse authResponse);

    @Mapping(target = "userName", source = "userName", qualifiedByName = "toLowerCaseTrim")
    @Mapping(target = "email", source = "email", qualifiedByName = "toLowerCaseTrim")
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "password", ignore = true)
    User mapToUser(RegisterReaderRequest request);

    RegisterUserResponse mapToRegisterReaderResponse(User user);

    @Named("toLowerCaseTrim")
    default String toLowerCaseTrim(String value) {
        return value != null ? value.toLowerCase().trim() : null;
    }
}