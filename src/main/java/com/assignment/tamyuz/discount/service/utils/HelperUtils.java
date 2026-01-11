package com.assignment.tamyuz.discount.service.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

@UtilityClass
public class HelperUtils {

    public static <T> PagedResponse<T> toPagedResponse(Page<T> page) {
        return PagedResponse.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

}