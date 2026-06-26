package com.shepherd.shep_blog.data.dto.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;
import java.util.Set;

import static com.shepherd.shep_blog.utils.AppUtils.*;

@Slf4j
public record PaginationRequest(
        Integer page,
        Integer size,
        String sortBy,
        String sortDirection
) {
    private int resolvedPageNumber() {
        if (page == null || page < 1) {
            return 0; // Sprint data 0-based page
        }
        return page - 1; // Spring data 0-based
    }

    private int resolvedPageSize() {
        if (size == null || size < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(size, MAX_PAGE_SIZE);
    }

    private String resolvedSortField(Set<String> allowedSortFields) {
        if (allowedSortFields == null || allowedSortFields.isEmpty()) {
            throw new IllegalStateException("Allowed sort fields must be provided");
        }

        if (sortBy == null || sortBy.isBlank()) {
            return DEFAULT_SORT_FIELD;
        }
        String trimmed = sortBy.trim();
        return allowedSortFields.stream()
                .filter(field -> field.equalsIgnoreCase(trimmed))
                .findFirst()
                .orElseGet(() -> {
                    log.warn("==>> Invalid sortBy '{}' received. Falling back to '{}'", trimmed, DEFAULT_SORT_FIELD);
                    return DEFAULT_SORT_FIELD;
                });
    }

    private Sort.Direction resolvedSortDirection() {
        if (sortDirection == null || sortDirection.isBlank()) {
            return DEFAULT_SORT_DIRECTION;
        }

        try {
            return Sort.Direction.fromString(sortDirection.trim());
        } catch (IllegalArgumentException _) {
            return DEFAULT_SORT_DIRECTION;
        }
    }

    public Pageable toPageable(Set<String> allowedSortFields) {
        Objects.requireNonNull(allowedSortFields, "allowedSortFields must not be null");

        return PageRequest.of(
                resolvedPageNumber(),
                resolvedPageSize(),
                Sort.by(resolvedSortDirection(), resolvedSortField(allowedSortFields))
        );
    }

    public String toCacheKey(String prefix) {
        String sortField = (sortBy == null || sortBy.isBlank())
                ? DEFAULT_SORT_FIELD
                : sortBy.trim();
        return String.format(
                "%s:page:%d:size:%d:sort:%s:%s",
                prefix,
                resolvedPageNumber(),
                resolvedPageSize(),
                sortField,
                resolvedSortDirection().name()
        );
    }
}
