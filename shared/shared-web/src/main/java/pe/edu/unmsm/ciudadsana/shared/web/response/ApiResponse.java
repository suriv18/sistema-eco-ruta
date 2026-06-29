package pe.edu.unmsm.ciudadsana.shared.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        T data,
        String message,
        ErrorInfo error
) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(true, data, message, null);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(true, data, "Recurso creado exitosamente", null);
    }

    public static <T> ApiResponse<T> noContent() {
        return new ApiResponse<>(true, null, "Operación realizada correctamente", null);
    }

    public static <T> ApiResponse<T> error(ErrorInfo error) {
        return new ApiResponse<>(false, null, null, error);
    }
}
