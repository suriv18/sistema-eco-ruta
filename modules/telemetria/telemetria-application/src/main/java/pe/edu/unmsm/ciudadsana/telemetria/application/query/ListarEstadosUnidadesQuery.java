package pe.edu.unmsm.ciudadsana.telemetria.application.query;

import java.util.UUID;

public record ListarEstadosUnidadesQuery(UUID tenantId, int page, int size) {}
