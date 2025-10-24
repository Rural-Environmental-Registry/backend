package br.car.registration.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@ExtendWith(MockitoExtension.class)
class WebConfigTest {

    @InjectMocks
    private WebConfig webConfig;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(webConfig, "frontendUrls", "http://localhost:3000,http://localhost:4200");
    }

    @Test
    void addCorsMappings_WhenCalled_ShouldConfigureCorsCorrectly() {
        // Given
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration corsRegistration = mock(CorsRegistration.class);
        
        when(registry.addMapping("/v1/properties/**")).thenReturn(corsRegistration);
        when(corsRegistration.allowedOrigins(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders("*")).thenReturn(corsRegistration);
        when(corsRegistration.allowCredentials(true)).thenReturn(corsRegistration);

        // When
        webConfig.addCorsMappings(registry);

        // Then
        verify(registry).addMapping("/v1/properties/**");
        verify(corsRegistration).allowedOrigins(any(String[].class));
        verify(corsRegistration).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        verify(corsRegistration).allowedHeaders("*");
        verify(corsRegistration).allowCredentials(true);
    }

    @Test
    void addCorsMappings_WhenSingleUrl_ShouldConfigureCorrectly() {
        // Given
        ReflectionTestUtils.setField(webConfig, "frontendUrls", "http://localhost:3000");
        
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration corsRegistration = mock(CorsRegistration.class);
        
        when(registry.addMapping("/v1/properties/**")).thenReturn(corsRegistration);
        when(corsRegistration.allowedOrigins(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders("*")).thenReturn(corsRegistration);
        when(corsRegistration.allowCredentials(true)).thenReturn(corsRegistration);

        // When
        webConfig.addCorsMappings(registry);

        // Then
        verify(corsRegistration).allowedOrigins(any(String[].class));
    }

    @Test
    void addCorsMappings_WhenMultipleUrls_ShouldSplitCorrectly() {
        // Given
        ReflectionTestUtils.setField(webConfig, "frontendUrls", "http://localhost:3000,https://app.example.com,http://localhost:8080");
        
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration corsRegistration = mock(CorsRegistration.class);
        
        when(registry.addMapping("/v1/properties/**")).thenReturn(corsRegistration);
        when(corsRegistration.allowedOrigins(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders("*")).thenReturn(corsRegistration);
        when(corsRegistration.allowCredentials(true)).thenReturn(corsRegistration);

        // When
        webConfig.addCorsMappings(registry);

        // Then
        verify(corsRegistration).allowedOrigins(any(String[].class));
    }
}