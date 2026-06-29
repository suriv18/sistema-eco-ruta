package pe.edu.unmsm.ciudadsana.shared.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import pe.edu.unmsm.ciudadsana.shared.result.ValidationError;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorInfo(
        String code,
        String message,
        List<ValidationError> errors
) {

    public static ErrorInfo of(String code, String message) {
        return new ErrorInfo(code, message, null);
    }

    public static ErrorInfo of(String code, String message, List<ValidationError> errors) {
        return new ErrorInfo(code, message, errors);
    }
}
