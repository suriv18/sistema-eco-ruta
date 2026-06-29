package pe.edu.unmsm.ciudadsana.rutas.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record ActualizarParadaRequest(
        @NotBlank String nuevoEstado,
        Instant horaLlegada,
        Instant horaSalida
) {}
