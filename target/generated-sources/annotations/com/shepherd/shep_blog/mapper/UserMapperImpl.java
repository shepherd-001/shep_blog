package com.shepherd.shep_blog.mapper;

import com.shepherd.shep_blog.data.dto.request.CreateTeamMemberRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterAuthorRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterReaderRequest;
import com.shepherd.shep_blog.data.dto.response.AuthResponse;
import com.shepherd.shep_blog.data.dto.response.RegisterUserResponse;
import com.shepherd.shep_blog.data.dto.response.UserResponse;
import com.shepherd.shep_blog.data.dto.response.VerifyEmailResponse;
import com.shepherd.shep_blog.data.model.User;
import com.shepherd.shep_blog.data.model.UserRole;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-22T14:46:07+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public VerifyEmailResponse mapToVerifyEmailResponse(User user, AuthResponse authResponse) {
        if ( user == null && authResponse == null ) {
            return null;
        }

        VerifyEmailResponse.VerifyEmailResponseBuilder verifyEmailResponse = VerifyEmailResponse.builder();

        if ( user != null ) {
            verifyEmailResponse.firstName( user.getFirstName() );
            verifyEmailResponse.lastName( user.getLastName() );
            verifyEmailResponse.username( user.getUsername() );
            verifyEmailResponse.email( user.getEmail() );
            verifyEmailResponse.enabled( user.isEnabled() );
            verifyEmailResponse.emailVerified( user.isEmailVerified() );
        }
        if ( authResponse != null ) {
            verifyEmailResponse.accessToken( authResponse.getAccessToken() );
            verifyEmailResponse.refreshToken( authResponse.getRefreshToken() );
        }

        return verifyEmailResponse.build();
    }

    @Override
    public User mapToUser(RegisterReaderRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( toLowerCaseTrim( request.getUsername() ) );
        user.email( toLowerCaseTrim( request.getEmail() ) );
        user.gender( request.getGender() );

        return user.build();
    }

    @Override
    public User mapToUser(RegisterAuthorRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.firstName( trim( request.getFirstName() ) );
        user.lastName( trim( request.getLastName() ) );
        user.username( toLowerCaseTrim( request.getUsername() ) );
        user.email( toLowerCaseTrim( request.getEmail() ) );
        user.gender( request.getGender() );

        return user.build();
    }

    @Override
    public User mapToUser(CreateTeamMemberRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.firstName( trim( request.getFirstName() ) );
        user.lastName( trim( request.getLastName() ) );
        user.email( toLowerCaseTrim( request.getEmail() ) );
        user.username( toLowerCaseTrim( request.getUsername() ) );
        user.gender( request.getGender() );

        return user.build();
    }

    @Override
    public UserResponse mapToUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.firstName( user.getFirstName() );
        userResponse.lastName( user.getLastName() );
        userResponse.username( user.getUsername() );
        userResponse.email( user.getEmail() );
        userResponse.gender( user.getGender() );
        Set<UserRole> set = user.getRoles();
        if ( set != null ) {
            userResponse.roles( new LinkedHashSet<UserRole>( set ) );
        }
        userResponse.enabled( user.isEnabled() );
        userResponse.emailVerified( user.isEmailVerified() );

        return userResponse.build();
    }

    @Override
    public RegisterUserResponse mapToRegisterReaderResponse(User user) {
        if ( user == null ) {
            return null;
        }

        RegisterUserResponse.RegisterUserResponseBuilder registerUserResponse = RegisterUserResponse.builder();

        registerUserResponse.username( user.getUsername() );
        registerUserResponse.firstName( user.getFirstName() );
        registerUserResponse.lastName( user.getLastName() );
        registerUserResponse.email( user.getEmail() );
        registerUserResponse.gender( user.getGender() );
        registerUserResponse.enabled( user.isEnabled() );
        registerUserResponse.emailVerified( user.isEmailVerified() );

        return registerUserResponse.build();
    }
}
