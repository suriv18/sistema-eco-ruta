package pe.edu.unmsm.ciudadsana.ciudadano.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CambiarEstadoAlertaRequest(
        @NotBlank String nuevoEstado,
        String comentario,
        UUID cambiadoPorUsuarioId
) {}
