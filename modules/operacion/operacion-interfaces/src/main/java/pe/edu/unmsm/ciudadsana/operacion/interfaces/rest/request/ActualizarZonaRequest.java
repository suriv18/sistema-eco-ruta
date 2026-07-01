package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request;

import jakarta.validation.constraints.NotNull;

public record ActualizarZonaRequest(@NotNull Integer prioridad) {}
