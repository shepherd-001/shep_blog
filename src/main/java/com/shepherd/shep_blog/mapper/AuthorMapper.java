package com.shepherd.shep_blog.mapper;

import com.shepherd.shep_blog.data.dto.request.RegisterAuthorRequest;
import com.shepherd.shep_blog.data.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapConfig.class)
public interface AuthorMapper {

//    @Mapping(target = "user.userName", source = "userName", qualifiedByName = "toLowerCaseTrim")
//    @Mapping(target = "user.email", source = "email", qualifiedByName = "toLowerCaseTrim")
//    @Mapping(target = "user.firstName", source = "firstName", qualifiedByName = "trim")
//    @Mapping(target = "user.lastName", source = "lastName", qualifiedByName = "trim")
//    @Mapping(target = "user.gender", ignore = true)
//    @Mapping(target = "user.password", ignore = true)
//    @Mapping(target = "organizationPhoneNumber", source = "organizationPhoneNumber", qualifiedByName = "trim")
//    @Mapping(target = "websiteAddress", source = "websiteAddress", qualifiedByName = "toLowerCaseTrim")
//    Author mapToAuthor(RegisterAuthorRequest request);

//    AuthorResponse mapToAuthorResponse(Author author);
}