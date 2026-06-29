package pe.edu.unmsm.ciudadsana.rutas.application.query;

import java.time.LocalDate;
import java.util.UUID;

public record ListarRutasQuery(
        UUID tenantId,
        UUID distritoId,
        LocalDate fecha,
        String estado,
        int page,
        int size
) {}
