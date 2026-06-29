package pe.edu.unmsm.ciudadsana.shared.result;

public record BusinessError(ErrorCode code, String message) {

    public static BusinessError of(ErrorCode code) {
        return new BusinessError(code, code.getMensajeDefecto());
    }

    public static BusinessError of(ErrorCode code, String message) {
        return new BusinessError(code, message);
    }
}
