package pe.edu.unmsm.ciudadsana.rutas.application.command;

import java.time.Instant;
import java.util.UUID;

public record ActualizarParadaCommand(
        UUID rutaId,
        UUID paradaId,
        UUID tenantId,
        String nuevoEstado,
        Instant horaLlegada,
        Instant horaSalida
) {}
