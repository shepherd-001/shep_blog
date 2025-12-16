package com.shepherd.shep_blog.data.dto.request;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaginationRequest {
    private Integer pageNumber;
    private Integer pageSize;
    private String sortBy;
    private String sortDirection;
    private Set<String> allowedSortFields;
}