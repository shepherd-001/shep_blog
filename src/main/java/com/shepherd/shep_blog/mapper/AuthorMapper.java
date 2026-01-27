package com.shepherd.shep_blog.mapper;

import com.shepherd.shep_blog.data.dto.response.TeamMemberResponse;
import com.shepherd.shep_blog.data.model.TeamMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapConfig.class)
public interface AuthorMapper {
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "gender", source = "user.gender")
//    @Mapping(target = "roles", source = "user.roles")
    @Mapping(target = "enabled", source = "user.enabled")
    @Mapping(target = "emailVerified", source = "user.emailVerified")
    TeamMemberResponse mapToTeamMemberResponse(TeamMember teamMember);
}