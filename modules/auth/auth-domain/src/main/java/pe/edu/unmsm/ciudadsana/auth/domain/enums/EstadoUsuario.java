package pe.edu.unmsm.ciudadsana.auth.domain.enums;

public enum EstadoUsuario {
    ACTIVO,
    BLOQUEADO,
    INACTIVO;

    public boolean puedeIniciarSesion() {
        return this == ACTIVO;
    }
}
