package com.shepherd.shep_blog.data.dto.request;

import com.shepherd.shep_blog.utils.pagination_utils.PageRequestFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaginationRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;

    public int resolvedPageNumber() {
        if(page == null || page < 1) {
            return 0; // Sprint data 0-based page
        }
//        if(pageNumber > PageRequestFactory.MAX_PAGE_NUMBER) {
//            throw new IllegalArgumentException("Page number exceeds maximum allowed");
//        }
        return page - 1; // Spring data 0-based
    }

    public int resolvedPageSize() {
        if(size == null || size < 1) {
            return PageRequestFactory.DEFAULT_PAGE_SIZE;
        }
        return Math.min(size, PageRequestFactory.MAX_PAGE_SIZE);
    }

    public String resolvedSortField(Set<String> allowedSortFields) {
        if (allowedSortFields == null || allowedSortFields.isEmpty()) {
            throw new IllegalStateException("Allowed sort fields must be provided");
        }

        if(sortBy == null || sortBy.isBlank()) {
            return PageRequestFactory.DEFAULT_SORT_FIELD;
        }
        String trimmed = sortBy.trim();
        return allowedSortFields.contains(trimmed)
                ? trimmed
                : PageRequestFactory.DEFAULT_SORT_FIELD;
    }

    public Sort.Direction resolvedSortDirection() {
        if(sortDirection == null || sortDirection.isBlank()) {
            return PageRequestFactory.DEFAULT_SORT_DIRECTION;
        }

        try{
            return Sort.Direction.fromString(sortDirection.trim());
        }
        catch (IllegalArgumentException _) {
            return PageRequestFactory.DEFAULT_SORT_DIRECTION;
        }
    }

    public String toCacheKey(String prefix) {
        String sortField = (sortBy == null || sortBy.isBlank())
                ? PageRequestFactory.DEFAULT_SORT_FIELD
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