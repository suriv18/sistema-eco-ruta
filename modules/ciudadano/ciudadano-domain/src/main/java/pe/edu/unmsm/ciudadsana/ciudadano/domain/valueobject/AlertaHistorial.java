package pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.time.Instant;
import java.util.UUID;

public record AlertaHistorial(
        UUID historialId,
        AlertaId alertaId,
        String estadoAnterior,
        String estadoNuevo,
        String comentario,
        UUID cambiadoPorUsuarioId,
        Instant cambiadoEn
) implements ValueObject {

    public AlertaHistorial {
        if (historialId == null) {
            throw new IllegalArgumentException("AlertaHistorial: historialId no puede ser nulo");
        }
        if (alertaId == null) {
            throw new IllegalArgumentException("AlertaHistorial: alertaId no puede ser nulo");
        }
        if (estadoNuevo == null || estadoNuevo.isBlank()) {
            throw new IllegalArgumentException("AlertaHistorial: estadoNuevo no puede ser nulo ni vacío");
        }
        if (cambiadoEn == null) {
            throw new IllegalArgumentException("AlertaHistorial: cambiadoEn no puede ser nulo");
        }
        // estadoAnterior, comentario, cambiadoPorUsuarioId are nullable
    }
}
