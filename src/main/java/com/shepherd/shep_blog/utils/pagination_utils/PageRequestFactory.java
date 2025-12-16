package com.shepherd.shep_blog.utils.pagination_utils;

import com.shepherd.shep_blog.data.dto.request.PaginationRequest;
import com.shepherd.shep_blog.utils.ErrorMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

public final class PageRequestFactory {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;
    private static final int MAX_PAGE_NUMBER = 500;
    public static final String DEFAULT_SORT_FIELD = "createdAt";
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;


    public static Pageable create(PaginationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("PaginationRequest must not be null");
        }
        int resolvedPageNumber = resolvePageNumber(request.getPageNumber());
        int resolvedPageSize = resolvePageSize(request.getPageSize());

        Sort sort = resolveSort(request.getSortBy(),request.getAllowedSortFields(), request.getSortDirection());

        return PageRequest.of(resolvedPageNumber, resolvedPageSize, sort);
    }

    private static int resolvePageNumber(Integer pageNumber) {
        if(pageNumber == null || pageNumber < 1) {
            return 0; // default to first page
        }
        if(pageNumber > MAX_PAGE_NUMBER) {
            throw new IllegalArgumentException("Page number exceeds maximum allowed");
        }
        return pageNumber - 1; // Spring data 0-based
    }

    private static int resolvePageSize(Integer pageSize) {
        if(pageSize == null || pageSize < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }

    private static Sort resolveSort(String sortBy, Set<String> allowedSortFields, String sortDirection) {
        if (allowedSortFields == null || allowedSortFields.isEmpty()) {
            throw new IllegalStateException("Allowed sort fields must be provided");
        }

        String resolvedSortBy = resolveSortField(sortBy, allowedSortFields);
        Sort.Direction resolvedDirection = resolveSortDirection(sortDirection);

        return Sort.by(resolvedDirection, resolvedSortBy);
    }

    private static String resolveSortField(String sortBy, Set<String> allowedSortFields) {
        if(sortBy == null || sortBy.isBlank()) {
            return DEFAULT_SORT_FIELD;
        }
        sortBy = sortBy.trim();

        if(!allowedSortFields.contains(sortBy)) {
            return DEFAULT_SORT_FIELD;
        }
        return sortBy;
    }

    private static Sort.Direction resolveSortDirection(String sortDirection) {
        if(sortDirection == null || sortDirection.isBlank()) {
            return DEFAULT_SORT_DIRECTION;
        }

        try{
            return Sort.Direction.fromString(sortDirection.trim());
        }
        catch (IllegalArgumentException ex) {
            return DEFAULT_SORT_DIRECTION;
        }
    }

    private PageRequestFactory() {
        throw new UnsupportedOperationException(ErrorMessage.NON_INSTANTIABLE_UTILITY_CLASS);
    }
}