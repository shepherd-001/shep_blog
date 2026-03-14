package com.shepherd.shep_blog.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PaginationResponse<T> {
    private List<T> content;
    private int page; // 1 based.
    private int size;
    private int numberOfElements;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    private boolean last;
}
