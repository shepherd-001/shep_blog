package com.shepherd.shep_blog.utils.pagination_utils;

import com.shepherd.shep_blog.data.dto.response.PageResponse;
import com.shepherd.shep_blog.utils.ErrorMessage;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public final class PageMapper {

    public static <S, T> PageResponse<T> map(Page<S> page, Function<S, T> mapper) {
        List<T> content = page.isEmpty()
                ? Collections.emptyList()
                : page.stream().map(mapper).toList();
        return PageResponse.<T>builder()
                .content(content)
                .pageNumber(page.getNumber() + 1) // 1 based page number
                .pageSize(page.getSize())
                .numberOfElements(page.getNumberOfElements())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .isLast(page.isLast())
                .build();
    }

    public static <T> PageResponse<T> map(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber() + 1) // 1 based page number
                .pageSize(page.getSize())
                .numberOfElements(page.getNumberOfElements())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .isLast(page.isLast())
                .build();
    }

    private PageMapper() {
        throw new UnsupportedOperationException(ErrorMessage.NON_INSTANTIABLE_UTILITY_CLASS);
    }
}