package pe.edu.unmsm.ciudadsana.shared.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.edu.unmsm.ciudadsana.shared.result.ValidationError;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.response.ErrorInfo;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        List<ValidationError> errors = ex.getBindingResult().getAllErrors().stream()
                .filter(e -> e instanceof FieldError)
                .map(e -> (FieldError) e)
                .map(fe -> ValidationError.of(fe.getField(), fe.getDefaultMessage()))
                .toList();

        ErrorInfo errorInfo = ErrorInfo.of("VALIDACION_ERROR", "Error de validación en los datos de entrada", errors);
        return ResponseEntity.badRequest().body(ApiResponse.error(errorInfo));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorInfo errorInfo = ErrorInfo.of("ARGUMENTO_INVALIDO", ex.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error(errorInfo));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        log.error("Error inesperado", ex);
        ErrorInfo errorInfo = ErrorInfo.of("ERROR_INTERNO", "Ha ocurrido un error interno. Contacte al administrador.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(errorInfo));
    }
}
