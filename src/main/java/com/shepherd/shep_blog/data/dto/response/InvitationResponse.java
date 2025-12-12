package com.shepherd.shep_blog.data.dto.response;

import com.shepherd.shep_blog.data.model.InvitationStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class InvitationResponse {
    private InvitationStatus status;
}
