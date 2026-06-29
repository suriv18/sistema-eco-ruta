package pe.edu.unmsm.ciudadsana.kpi.application.command;

import java.time.LocalDate;
import java.util.UUID;

public record CalcularResumenDiarioCommand(UUID tenantId, UUID distritoId, LocalDate fecha) {}
