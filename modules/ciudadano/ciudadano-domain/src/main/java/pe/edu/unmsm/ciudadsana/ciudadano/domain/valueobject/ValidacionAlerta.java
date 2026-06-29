package pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.time.Instant;

public record ValidacionAlerta(
        ValidacionId id,
        AlertaId alertaId,
        boolean esDuplicada,
        AlertaId alertaOriginalId,
        boolean dentroGeocerca,
        double scoreSpam,
        String resultado,
        Instant validadaEn
) implements ValueObject {

    public ValidacionAlerta {
        if (id == null) {
            throw new IllegalArgumentException("ValidacionAlerta: id no puede ser nulo");
        }
        if (alertaId == null) {
            throw new IllegalArgumentException("ValidacionAlerta: alertaId no puede ser nulo");
        }
        if (scoreSpam < 0.0 || scoreSpam > 999.99) {
            throw new IllegalArgumentException(
                    "ValidacionAlerta: scoreSpam debe ser un valor no negativo, valor recibido: " + scoreSpam);
        }
        if (resultado == null || resultado.isBlank()) {
            throw new IllegalArgumentException("ValidacionAlerta: resultado no puede ser nulo ni vacío");
        }
        if (validadaEn == null) {
            throw new IllegalArgumentException("ValidacionAlerta: validadaEn no puede ser nulo");
        }
        // alertaOriginalId is nullable (only set when esDuplicada == true)
    }
}
