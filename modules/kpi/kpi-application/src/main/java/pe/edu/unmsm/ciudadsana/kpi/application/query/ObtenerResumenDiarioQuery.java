package pe.edu.unmsm.ciudadsana.kpi.application.query;

import java.time.LocalDate;
import java.util.UUID;

public record ObtenerResumenDiarioQuery(UUID tenantId, UUID distritoId, LocalDate fecha) {}
