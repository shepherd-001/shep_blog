package com.shepherd.shep_blog.mapper;

import com.shepherd.shep_blog.data.dto.response.TeamMemberResponse;
import com.shepherd.shep_blog.data.model.TeamMember;
import com.shepherd.shep_blog.data.model.User;
import com.shepherd.shep_blog.data.model.enums.Gender;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-23T17:51:34+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class AuthorMapperImpl implements AuthorMapper {

    @Override
    public TeamMemberResponse mapToTeamMemberResponse(TeamMember teamMember) {
        if ( teamMember == null ) {
            return null;
        }

        TeamMemberResponse.TeamMemberResponseBuilder teamMemberResponse = TeamMemberResponse.builder();

        teamMemberResponse.firstName( teamMemberUserFirstName( teamMember ) );
        teamMemberResponse.lastName( teamMemberUserLastName( teamMember ) );
        teamMemberResponse.username( teamMemberUserUsername( teamMember ) );
        teamMemberResponse.email( teamMemberUserEmail( teamMember ) );
        teamMemberResponse.gender( teamMemberUserGender( teamMember ) );
        teamMemberResponse.enabled( teamMemberUserEnabled( teamMember ) );
        teamMemberResponse.emailVerified( teamMemberUserEmailVerified( teamMember ) );
        teamMemberResponse.status( teamMember.getStatus() );
        teamMemberResponse.role( teamMember.getRole() );

        return teamMemberResponse.build();
    }

    private String teamMemberUserFirstName(TeamMember teamMember) {
        User user = teamMember.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getFirstName();
    }

    private String teamMemberUserLastName(TeamMember teamMember) {
        User user = teamMember.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getLastName();
    }

    private String teamMemberUserUsername(TeamMember teamMember) {
        User user = teamMember.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getUsername();
    }

    private String teamMemberUserEmail(TeamMember teamMember) {
        User user = teamMember.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getEmail();
    }

    private Gender teamMemberUserGender(TeamMember teamMember) {
        User user = teamMember.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getGender();
    }

    private boolean teamMemberUserEnabled(TeamMember teamMember) {
        User user = teamMember.getUser();
        if ( user == null ) {
            return false;
        }
        return user.isEnabled();
    }

    private boolean teamMemberUserEmailVerified(TeamMember teamMember) {
        User user = teamMember.getUser();
        if ( user == null ) {
            return false;
        }
        return user.isEmailVerified();
    }
}
