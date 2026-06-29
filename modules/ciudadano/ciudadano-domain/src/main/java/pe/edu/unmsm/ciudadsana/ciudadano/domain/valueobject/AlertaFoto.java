package pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

public record AlertaFoto(
        AlertaFotoId id,
        AlertaId alertaId,
        String urlArchivo,
        String tipoMime,
        Long tamanioBytes
) implements ValueObject {

    public AlertaFoto {
        if (id == null) {
            throw new IllegalArgumentException("AlertaFoto: id no puede ser nulo");
        }
        if (alertaId == null) {
            throw new IllegalArgumentException("AlertaFoto: alertaId no puede ser nulo");
        }
        if (urlArchivo == null || urlArchivo.isBlank()) {
            throw new IllegalArgumentException("AlertaFoto: urlArchivo no puede ser nulo ni vacío");
        }
        if (tipoMime == null || tipoMime.isBlank()) {
            throw new IllegalArgumentException("AlertaFoto: tipoMime no puede ser nulo ni vacío");
        }
        // tamanioBytes is nullable — no validation needed
    }
}
