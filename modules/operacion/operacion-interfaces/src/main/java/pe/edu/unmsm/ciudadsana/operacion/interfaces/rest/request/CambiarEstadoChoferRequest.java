package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request;

import jakarta.validation.constraints.NotNull;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoChofer;

public record CambiarEstadoChoferRequest(@NotNull EstadoChofer estado) {}
