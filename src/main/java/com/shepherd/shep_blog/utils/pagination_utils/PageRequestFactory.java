package com.shepherd.shep_blog.utils.pagination_utils;

import com.shepherd.shep_blog.data.dto.request.PaginationRequest;
import com.shepherd.shep_blog.utils.ErrorMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

public final class PageRequestFactory {
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int MAX_PAGE_NUMBER = 500;
    public static final String DEFAULT_SORT_FIELD = "createdAt";
    public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;


    public static Pageable create(PaginationRequest request, Set<String> allowedSortFields) {
        if (request == null) {
            throw new IllegalArgumentException("Pagination request must not be null");
        }

        String sortField = request.resolvedSortField(allowedSortFields);
        Sort.Direction direction = request.resolvedSortDirection();
        return PageRequest.of(request.resolvedPageNumber(), request.resolvedPageSize(), Sort.by(direction, sortField));
    }

    private PageRequestFactory() {
        throw new UnsupportedOperationException(ErrorMessage.NON_INSTANTIABLE_UTILITY_CLASS);
    }
}