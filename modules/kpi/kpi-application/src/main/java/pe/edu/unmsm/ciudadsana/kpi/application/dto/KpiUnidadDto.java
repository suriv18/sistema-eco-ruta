package pe.edu.unmsm.ciudadsana.kpi.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record KpiUnidadDto(
        UUID kpiUnidadId,
        UUID tenantId,
        UUID unidadIdExterno,
        LocalDate fecha,
        BigDecimal kmRecorridos,
        BigDecimal horasOperacion,
        BigDecimal toneladasRecolectadas,
        BigDecimal consumoEstimadoLitros,
        Instant creadoEn
) {}
