package com.shepherd.shep_blog.common.response;

import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;


public record PaginationResponse<T>(
        List<T> content,
        int page,
        int size,
        int numberOfElements,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious,
        boolean first,
        boolean last
) {
    public static <S, T> PaginationResponse<T> map(Page<S> page, Function<S, T> mapper) {
        List<T> content = page.isEmpty()
                ? Collections.emptyList()
                : page.stream().map(mapper).toList();
        return new PaginationResponse<>(
                content,
                page.getNumber() + 1, // 1 based page number
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious(),
                page.isFirst(),
                page.isLast()
        );
    }

    public static <T> PaginationResponse<T> map(Page<T> page) {
        return new PaginationResponse<>(
                page.getContent(),
                page.getNumber() + 1, // 1 based page number
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious(),
                page.isFirst(),
                page.isLast()
        );
    }
}