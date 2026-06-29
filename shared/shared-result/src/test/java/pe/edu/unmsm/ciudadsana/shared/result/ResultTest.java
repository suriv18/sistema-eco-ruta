package pe.edu.unmsm.ciudadsana.shared.result;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ResultTest {

    @Test
    void success_wrapsValue() {
        Result<String> result = Result.success("valor");

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.isFailure()).isFalse();
        assertThat(result.getValue()).isEqualTo("valor");
    }

    @Test
    void failure_wrapsError() {
        Result<String> result = Result.failure(ErrorCode.USUARIO_NO_ENCONTRADO);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.USUARIO_NO_ENCONTRADO);
    }

    @Test
    void map_transformsSuccessValue() {
        Result<String> result = Result.success("hola");

        Result<Integer> mapped = result.map(String::length);

        assertThat(mapped.isSuccess()).isTrue();
        assertThat(mapped.getValue()).isEqualTo(4);
    }

    @Test
    void map_propagatesFailure() {
        Result<String> result = Result.failure(ErrorCode.USUARIO_NO_ENCONTRADO);

        Result<Integer> mapped = result.map(String::length);

        assertThat(mapped.isFailure()).isTrue();
        assertThat(mapped.getError().code()).isEqualTo(ErrorCode.USUARIO_NO_ENCONTRADO);
    }

    @Test
    void flatMap_chainsSuccessResults() {
        Result<String> result = Result.success("42");

        Result<Integer> chained = result.flatMap(s -> {
            try {
                return Result.success(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                return Result.failure(ErrorCode.VALIDACION_ERROR);
            }
        });

        assertThat(chained.isSuccess()).isTrue();
        assertThat(chained.getValue()).isEqualTo(42);
    }

    @Test
    void flatMap_propagatesFirstFailure() {
        Result<String> result = Result.failure(ErrorCode.CREDENCIALES_INVALIDAS);

        Result<Integer> chained = result.flatMap(s -> Result.success(Integer.parseInt(s)));

        assertThat(chained.isFailure()).isTrue();
        assertThat(chained.getError().code()).isEqualTo(ErrorCode.CREDENCIALES_INVALIDAS);
    }

    @Test
    void getValue_throwsOnFailure() {
        Result<String> result = Result.failure(ErrorCode.TENANT_INVALIDO);

        assertThatThrownBy(result::getValue)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void getError_throwsOnSuccess() {
        Result<String> result = Result.success("ok");

        assertThatThrownBy(result::getError)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void failure_withCustomMessage() {
        Result<Void> result = Result.failure(ErrorCode.GEOMETRIA_INVALIDA, "Polígono no cerrado");

        assertThat(result.getError().message()).isEqualTo("Polígono no cerrado");
    }
}
