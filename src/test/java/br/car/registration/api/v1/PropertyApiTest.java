package br.car.registration.api.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

class PropertyApiTest {

    @Test
    void interface_WhenLoaded_ShouldHaveRequestMapping() {
        // When
        RequestMapping annotation = PropertyApi.class.getAnnotation(RequestMapping.class);

        // Then
        assertNotNull(annotation);
        assertEquals("/v1/properties", annotation.value()[0]);
    }

    @Test
    void getProperties_WhenMethodExists_ShouldHaveGetMapping() throws NoSuchMethodException {
        // When
        Method method = PropertyApi.class.getMethod("getProperties", 
                br.car.registration.api.v1.request.PropertyFilter.class,
                org.springframework.data.domain.Pageable.class);
        GetMapping annotation = method.getAnnotation(GetMapping.class);

        // Then
        assertNotNull(method);
        assertNotNull(annotation);
    }

    @Test
    void getProperty_WhenMethodExists_ShouldHaveGetMapping() throws NoSuchMethodException {
        // When
        Method method = PropertyApi.class.getMethod("getProperty", java.util.UUID.class);
        GetMapping annotation = method.getAnnotation(GetMapping.class);

        // Then
        assertNotNull(method);
        assertNotNull(annotation);
        assertEquals("/{id}", annotation.path()[0]);
    }

    @Test
    void addProperty_WhenMethodExists_ShouldHavePostMapping() throws NoSuchMethodException {
        // When
        Method method = PropertyApi.class.getMethod("addProperty", 
                br.car.registration.api.v1.request.PropertyReq.class,
                org.springframework.web.multipart.MultipartFile.class);
        PostMapping annotation = method.getAnnotation(PostMapping.class);

        // Then
        assertNotNull(method);
        assertNotNull(annotation);
    }

    @Test
    void updateProperty_WhenMethodExists_ShouldHavePutMapping() throws NoSuchMethodException {
        // When
        Method method = PropertyApi.class.getMethod("updateProperty", 
                br.car.registration.api.v1.request.PropertyReq.class,
                org.springframework.web.multipart.MultipartFile.class,
                java.util.UUID.class);
        PutMapping annotation = method.getAnnotation(PutMapping.class);

        // Then
        assertNotNull(method);
        assertNotNull(annotation);
        assertEquals("/{id}", annotation.path()[0]);
    }

    @Test
    void interface_WhenLoaded_ShouldBeInterface() {
        // When & Then
        assertTrue(PropertyApi.class.isInterface());
    }

    @Test
    void interface_WhenLoaded_ShouldHaveCorrectMethodCount() {
        // When
        Method[] methods = PropertyApi.class.getDeclaredMethods();

        // Then
        assertEquals(6, methods.length);
    }
}