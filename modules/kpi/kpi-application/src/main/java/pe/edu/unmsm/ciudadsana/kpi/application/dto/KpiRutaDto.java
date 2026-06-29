package pe.edu.unmsm.ciudadsana.kpi.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record KpiRutaDto(
        UUID kpiRutaId,
        UUID tenantId,
        UUID rutaIdExterno,
        LocalDate fecha,
        BigDecimal distanciaPlanificadaM,
        BigDecimal distanciaRealM,
        int duracionPlanificadaS,
        int duracionRealS,
        int zonasProgramadas,
        int zonasAtendidas,
        BigDecimal cumplimientoPorcentaje,
        BigDecimal kmPorTonelada,
        Instant creadoEn
) {}
