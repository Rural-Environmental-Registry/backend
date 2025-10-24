package br.car.registration.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

class SpringContextHolderAdvancedTest {

    private SpringContextHolder springContextHolder;
    private ApplicationContext mockContext;

    @BeforeEach
    void setUp() {
        springContextHolder = new SpringContextHolder();
        mockContext = mock(ApplicationContext.class);
        // Clear static context before each test
        ReflectionTestUtils.setField(SpringContextHolder.class, "context", null);
    }

    @Test
    void getBean_WhenContextNotSet_ShouldThrowNullPointerException() {
        // When & Then
        assertThrows(NullPointerException.class, 
            () -> SpringContextHolder.getBean(String.class));
    }

    @Test
    void getBean_WithMultipleBeanTypes_ShouldReturnCorrectBeans() {
        // Given
        String stringBean = "testString";
        Integer integerBean = 42;
        
        when(mockContext.getBean(String.class)).thenReturn(stringBean);
        when(mockContext.getBean(Integer.class)).thenReturn(integerBean);
        
        springContextHolder.setApplicationContext(mockContext);

        // When
        String resultString = SpringContextHolder.getBean(String.class);
        Integer resultInteger = SpringContextHolder.getBean(Integer.class);

        // Then
        assertEquals(stringBean, resultString);
        assertEquals(integerBean, resultInteger);
        verify(mockContext).getBean(String.class);
        verify(mockContext).getBean(Integer.class);
    }

    @Test
    void getBean_WhenBeanNotFound_ShouldPropagateException() {
        // Given
        when(mockContext.getBean(String.class)).thenThrow(new RuntimeException("Bean not found"));
        springContextHolder.setApplicationContext(mockContext);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> SpringContextHolder.getBean(String.class));
        assertEquals("Bean not found", exception.getMessage());
    }

    @Test
    void setApplicationContext_WithNullContext_ShouldAcceptNull() {
        // When
        springContextHolder.setApplicationContext(null);

        // Then
        assertThrows(NullPointerException.class,
            () -> SpringContextHolder.getBean(String.class));
    }

    @Test
    void setApplicationContext_CalledMultipleTimes_ShouldUpdateContext() {
        // Given
        ApplicationContext firstContext = mock(ApplicationContext.class);
        ApplicationContext secondContext = mock(ApplicationContext.class);
        
        String firstBean = "first";
        String secondBean = "second";
        
        when(firstContext.getBean(String.class)).thenReturn(firstBean);
        when(secondContext.getBean(String.class)).thenReturn(secondBean);

        // When
        springContextHolder.setApplicationContext(firstContext);
        String firstResult = SpringContextHolder.getBean(String.class);
        
        springContextHolder.setApplicationContext(secondContext);
        String secondResult = SpringContextHolder.getBean(String.class);

        // Then
        assertEquals(firstBean, firstResult);
        assertEquals(secondBean, secondResult);
        verify(firstContext).getBean(String.class);
        verify(secondContext).getBean(String.class);
    }

    @Test
    void getBean_WithCustomClass_ShouldWork() {
        // Given
        CustomTestBean customBean = new CustomTestBean("test");
        when(mockContext.getBean(CustomTestBean.class)).thenReturn(customBean);
        springContextHolder.setApplicationContext(mockContext);

        // When
        CustomTestBean result = SpringContextHolder.getBean(CustomTestBean.class);

        // Then
        assertEquals(customBean, result);
        assertEquals("test", result.getValue());
        verify(mockContext).getBean(CustomTestBean.class);
    }

    // Helper class for testing
    private static class CustomTestBean {
        private final String value;

        public CustomTestBean(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}