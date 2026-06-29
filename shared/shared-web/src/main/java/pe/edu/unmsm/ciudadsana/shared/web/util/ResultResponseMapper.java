package pe.edu.unmsm.ciudadsana.shared.web.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.response.ErrorInfo;

import java.util.function.Function;

public final class ResultResponseMapper {

    private ResultResponseMapper() {}

    public static <T> ResponseEntity<ApiResponse<T>> toOk(Result<T> result) {
        return toResponse(result, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResponse<T>> toCreated(Result<T> result) {
        return toResponse(result, HttpStatus.CREATED);
    }

    public static <T, R> ResponseEntity<ApiResponse<R>> toOk(Result<T> result, Function<T, R> mapper) {
        return switch (result) {
            case Result.Success<T> s -> ResponseEntity.ok(ApiResponse.ok(mapper.apply(s.value())));
            case Result.Failure<T> f -> toErrorResponse(f);
        };
    }

    public static <T> ResponseEntity<ApiResponse<Void>> toNoContent(Result<T> result) {
        return switch (result) {
            case Result.Success<T> ignored -> ResponseEntity.ok(ApiResponse.noContent());
            case Result.Failure<T> f      -> toErrorResponse(f);
        };
    }

    private static <T> ResponseEntity<ApiResponse<T>> toResponse(Result<T> result, HttpStatus successStatus) {
        return switch (result) {
            case Result.Success<T> s -> ResponseEntity.status(successStatus).body(ApiResponse.ok(s.value()));
            case Result.Failure<T> f -> toErrorResponse(f);
        };
    }

    @SuppressWarnings("unchecked")
    private static <T, R> ResponseEntity<ApiResponse<R>> toErrorResponse(Result.Failure<T> failure) {
        HttpStatus status = resolveHttpStatus(failure.error().code().name());
        ErrorInfo errorInfo = ErrorInfo.of(failure.error().code().name(), failure.error().message());
        return ResponseEntity.status(status).body((ApiResponse<R>) ApiResponse.error(errorInfo));
    }

    private static HttpStatus resolveHttpStatus(String errorCode) {
        return switch (errorCode) {
            case "CREDENCIALES_INVALIDAS", "TOKEN_INVALIDO", "REFRESH_TOKEN_INVALIDO" -> HttpStatus.UNAUTHORIZED;
            case "USUARIO_SIN_PERMISO" -> HttpStatus.FORBIDDEN;
            case "USUARIO_NO_ENCONTRADO", "ZONA_NO_ENCONTRADA", "UNIDAD_NO_ENCONTRADA",
                 "CHOFER_NO_ENCONTRADO", "RUTA_NO_ENCONTRADA", "ALERTA_NO_ENCONTRADA",
                 "DISTRITO_NO_ENCONTRADO", "DEPOSITO_NO_ENCONTRADO", "CONTENEDOR_NO_ENCONTRADO",
                 "TURNO_NO_ENCONTRADO", "ROL_NO_ENCONTRADO", "RECURSO_NO_ENCONTRADO" -> HttpStatus.NOT_FOUND;
            case "USUARIO_YA_EXISTE", "ALERTA_DUPLICADA", "TURNO_SUPERPUESTO" -> HttpStatus.CONFLICT;
            case "VALIDACION_ERROR", "GEOMETRIA_INVALIDA", "VENTANA_HORARIA_INVALIDA",
                 "ARGUMENTO_INVALIDO" -> HttpStatus.BAD_REQUEST;
            case "OPERACION_NO_PERMITIDA", "RUTA_FINALIZADA_NO_MODIFICABLE",
                 "RUTA_ESTADO_INVALIDO" -> HttpStatus.UNPROCESSABLE_ENTITY;
            case "OPTIMIZADOR_NO_DISPONIBLE", "OSRM_NO_DISPONIBLE" -> HttpStatus.SERVICE_UNAVAILABLE;
            default -> HttpStatus.UNPROCESSABLE_ENTITY;
        };
    }
}
