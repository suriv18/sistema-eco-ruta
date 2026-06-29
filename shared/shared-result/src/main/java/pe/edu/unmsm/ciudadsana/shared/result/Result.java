package pe.edu.unmsm.ciudadsana.shared.result;

import java.util.function.Function;
import java.util.function.Supplier;

public sealed interface Result<T> permits Result.Success, Result.Failure {

    record Success<T>(T value) implements Result<T> {}
    record Failure<T>(BusinessError error) implements Result<T> {}

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Result<T> failure(ErrorCode code) {
        return new Failure<>(BusinessError.of(code));
    }

    static <T> Result<T> failure(ErrorCode code, String message) {
        return new Failure<>(BusinessError.of(code, message));
    }

    static <T> Result<T> failure(BusinessError error) {
        return new Failure<>(error);
    }

    default boolean isSuccess() {
        return this instanceof Success<T>;
    }

    default boolean isFailure() {
        return this instanceof Failure<T>;
    }

    default T getValue() {
        if (this instanceof Success<T> s) return s.value();
        throw new IllegalStateException("Result es Failure: " + ((Failure<T>) this).error().message());
    }

    default BusinessError getError() {
        if (this instanceof Failure<T> f) return f.error();
        throw new IllegalStateException("Result es Success");
    }

    default <U> Result<U> map(Function<T, U> mapper) {
        return switch (this) {
            case Success<T> s -> Result.success(mapper.apply(s.value()));
            case Failure<T> f -> Result.failure(f.error());
        };
    }

    default <U> Result<U> flatMap(Function<T, Result<U>> mapper) {
        return switch (this) {
            case Success<T> s -> mapper.apply(s.value());
            case Failure<T> f -> Result.failure(f.error());
        };
    }

    default T getOrElse(T defaultValue) {
        return isSuccess() ? getValue() : defaultValue;
    }

    default T getOrElseGet(Supplier<T> supplier) {
        return isSuccess() ? getValue() : supplier.get();
    }

    default Result<T> onSuccess(java.util.function.Consumer<T> consumer) {
        if (this instanceof Success<T> s) consumer.accept(s.value());
        return this;
    }

    default Result<T> onFailure(java.util.function.Consumer<BusinessError> consumer) {
        if (this instanceof Failure<T> f) consumer.accept(f.error());
        return this;
    }
}
