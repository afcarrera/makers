package ec.com.carrera.makers.loan.util;

import ec.com.carrera.makers.loan.exception.GlobalResponseStatusException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExceptionUtilTest {
    @Mock
    MessageSource messageSource;

    @Test
    void testGetNotFoundException(){
        when(messageSource.getMessage(anyString(), any(Object[].class), any(Locale.class))).thenReturn("message");
        GlobalResponseStatusException exception = ExceptionUtil.getNotFoundException(messageSource);
        assertNotNull(exception);
    }
}
