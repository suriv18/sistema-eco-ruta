package pe.edu.unmsm.ciudadsana.rutas.application.command;

import java.time.LocalDate;
import java.util.UUID;

public record CrearRutaCommand(
        UUID tenantId,
        UUID turnoId,
        UUID distritoId,
        UUID depositoOrigenId,
        UUID depositoDestinoId,
        LocalDate fecha,
        String tipoRuta
) {}
