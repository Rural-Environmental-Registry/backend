package br.car.registration.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import br.car.registration.api.v1.request.PropertyFilter;
import br.car.registration.domain.Property;

class PropertySpecificationSimpleTest {

    @Test
    void withFilters_ShouldCreateSpecificationWithoutErrors() {
        // Given
        PropertyFilter filter = new PropertyFilter();
        filter.setPropertyName("Test Property");
        filter.setStateDistrict("Test District");
        filter.setMunicipality("Test Municipality");
        filter.setCode(Arrays.asList("TEST-001"));

        // When
        Specification<Property> spec = PropertySpecification.withFilters(filter, "12345678901");

        // Then
        assertNotNull(spec);
    }

    @Test
    void withFilters_WithEmptyFilter_ShouldCreateSpecification() {
        // Given
        PropertyFilter filter = new PropertyFilter();

        // When
        Specification<Property> spec = PropertySpecification.withFilters(filter, "12345678901");

        // Then
        assertNotNull(spec);
    }

    @Test
    void withFilters_WithBlankIdentifier_ShouldCreateSpecification() {
        // Given
        PropertyFilter filter = new PropertyFilter();

        // When
        Specification<Property> spec = PropertySpecification.withFilters(filter, "");

        // Then
        assertNotNull(spec);
    }

    @Test
    void withFilters_WithNullIdentifier_ShouldCreateSpecification() {
        // Given
        PropertyFilter filter = new PropertyFilter();

        // When
        Specification<Property> spec = PropertySpecification.withFilters(filter, null);

        // Then
        assertNotNull(spec);
    }

    @Test
    void withFilters_WithAllFilters_ShouldCreateSpecification() {
        // Given
        PropertyFilter filter = new PropertyFilter();
        filter.setPropertyName("Test");
        filter.setStateDistrict("District");
        filter.setMunicipality("Municipality");
        filter.setCode(Arrays.asList("001", "002"));

        // When
        Specification<Property> spec = PropertySpecification.withFilters(filter, "12345678901");

        // Then
        assertNotNull(spec);
    }
}