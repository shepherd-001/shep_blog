package com.shepherd.shep_blog.data.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PaginationResponse<T> {
    private final List<T> content;
    private final int pageNumber; // 1 based.
    private final int pageSize;
    private int numberOfElements;
    private long totalElements;
    private int totalPages;
    private final boolean hasNext;
    private final boolean hasPrevious;
    private final boolean isLast;
}
