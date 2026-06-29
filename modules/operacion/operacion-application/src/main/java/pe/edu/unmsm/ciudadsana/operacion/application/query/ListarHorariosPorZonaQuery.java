package pe.edu.unmsm.ciudadsana.operacion.application.query;

import java.util.UUID;

public record ListarHorariosPorZonaQuery(UUID tenantId, UUID zonaId, int page, int size) {}
