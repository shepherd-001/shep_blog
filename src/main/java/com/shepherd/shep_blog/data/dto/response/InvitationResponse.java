package com.shepherd.shep_blog.data.dto.response;

import com.shepherd.shep_blog.data.model.enums.InvitationStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class InvitationResponse {
    private UUID invitationId;
    private InvitationStatus status;
}
