package pe.edu.unmsm.ciudadsana.auth.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

public record NombresCompletos(String nombres, String apellidos) implements ValueObject {

    public NombresCompletos {
        if (nombres == null || nombres.isBlank()) {
            throw new IllegalArgumentException("Los nombres no pueden ser nulos o vacíos");
        }
        if (nombres.length() > 120) {
            throw new IllegalArgumentException("Los nombres no pueden superar 120 caracteres");
        }
        if (apellidos == null || apellidos.isBlank()) {
            throw new IllegalArgumentException("Los apellidos no pueden ser nulos o vacíos");
        }
        if (apellidos.length() > 120) {
            throw new IllegalArgumentException("Los apellidos no pueden superar 120 caracteres");
        }
    }

    public static NombresCompletos of(String nombres, String apellidos) {
        return new NombresCompletos(nombres, apellidos);
    }

    public String nombreCompleto() {
        return nombres + " " + apellidos;
    }
}
