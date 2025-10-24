package br.car.registration.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleValidationExceptions_WhenFieldErrors_ShouldReturnErrorMap() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        FieldError fieldError1 = new FieldError("user", "name", "Nome é obrigatório");
        FieldError fieldError2 = new FieldError("user", "email", "Email inválido");
        
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));

        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        Map<String, String> errors = response.getBody();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Nome é obrigatório", errors.get("name"));
        assertEquals("Email inválido", errors.get("email"));
    }

    @Test
    void handleValidationExceptions_WhenObjectErrors_ShouldReturnErrorMap() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        ObjectError objectError = new ObjectError("user", "Erro de validação geral");
        
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(objectError));

        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        Map<String, String> errors = response.getBody();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("Erro de validação geral", errors.get("user"));
    }

    @Test
    void handleValidationExceptions_WhenMixedErrors_ShouldReturnErrorMap() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        FieldError fieldError = new FieldError("property", "area", "Área deve ser positiva");
        ObjectError objectError = new ObjectError("property", "Propriedade inválida");
        
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError, objectError));

        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        Map<String, String> errors = response.getBody();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Área deve ser positiva", errors.get("area"));
        assertEquals("Propriedade inválida", errors.get("property"));
    }

    @Test
    void handleValidationExceptions_WhenNoErrors_ShouldReturnEmptyMap() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of());

        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        Map<String, String> errors = response.getBody();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }
}