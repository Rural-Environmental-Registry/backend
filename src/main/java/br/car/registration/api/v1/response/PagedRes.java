package br.car.registration.api.v1.response;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class PagedRes<T> {
    private List<T> properties;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private Boolean hasnext;
}
