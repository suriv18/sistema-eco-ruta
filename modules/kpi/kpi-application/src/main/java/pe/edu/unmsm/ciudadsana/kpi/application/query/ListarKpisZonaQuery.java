package pe.edu.unmsm.ciudadsana.kpi.application.query;

import java.time.LocalDate;
import java.util.UUID;

public record ListarKpisZonaQuery(UUID tenantId, UUID zonaId, LocalDate fechaDesde, LocalDate fechaHasta, int page, int size) {}
