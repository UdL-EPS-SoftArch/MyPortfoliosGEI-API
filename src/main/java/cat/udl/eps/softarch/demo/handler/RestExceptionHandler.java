package cat.udl.eps.softarch.demo.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateResource() {
        return "Duplicate resource";
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        ConstraintViolationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException() {
        return "Validation error";
    }
}