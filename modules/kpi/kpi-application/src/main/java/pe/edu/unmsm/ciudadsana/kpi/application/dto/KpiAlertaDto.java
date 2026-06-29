package pe.edu.unmsm.ciudadsana.kpi.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record KpiAlertaDto(
        UUID kpiAlertaId,
        UUID tenantId,
        UUID alertaIdExterno,
        UUID zonaIdExterno,
        Instant registradaEn,
        Instant atendidaEn,
        BigDecimal tiempoRespuestaMin,
        boolean fueCritica,
        boolean incluidaEnRuta,
        Instant creadoEn
) {}
