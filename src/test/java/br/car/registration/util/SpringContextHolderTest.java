package br.car.registration.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

class SpringContextHolderTest {

    private SpringContextHolder springContextHolder;
    private ApplicationContext mockContext;

    @BeforeEach
    void setUp() {
        springContextHolder = new SpringContextHolder();
        mockContext = mock(ApplicationContext.class);
    }

    @Test
    void setApplicationContext_ShouldSetContext() {
        // When
        springContextHolder.setApplicationContext(mockContext);

        // Then - No exception should be thrown
        assertDoesNotThrow(() -> springContextHolder.setApplicationContext(mockContext));
    }

    @Test
    void getBean_WhenContextSet_ShouldReturnBean() {
        // Given
        String testBean = "testBean";
        when(mockContext.getBean(String.class)).thenReturn(testBean);
        springContextHolder.setApplicationContext(mockContext);

        // When
        String result = SpringContextHolder.getBean(String.class);

        // Then
        assertEquals(testBean, result);
        verify(mockContext).getBean(String.class);
    }
}