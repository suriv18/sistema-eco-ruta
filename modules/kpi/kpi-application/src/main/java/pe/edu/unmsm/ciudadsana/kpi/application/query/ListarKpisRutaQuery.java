package pe.edu.unmsm.ciudadsana.kpi.application.query;

import java.time.LocalDate;
import java.util.UUID;

public record ListarKpisRutaQuery(UUID tenantId, LocalDate fechaDesde, LocalDate fechaHasta, int page, int size) {}
