package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;

public record CambiarEstadoContenedorRequest(@NotBlank String nuevoEstado) {}
