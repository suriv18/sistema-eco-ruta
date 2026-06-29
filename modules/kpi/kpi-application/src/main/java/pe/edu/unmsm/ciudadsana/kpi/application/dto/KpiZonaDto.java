package pe.edu.unmsm.ciudadsana.kpi.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record KpiZonaDto(
        UUID kpiZonaId,
        UUID tenantId,
        UUID zonaIdExterno,
        LocalDate fecha,
        int vecesProgramada,
        int vecesAtendida,
        BigDecimal kgRecolectados,
        BigDecimal coberturaPorcentaje,
        Instant creadoEn
) {}
