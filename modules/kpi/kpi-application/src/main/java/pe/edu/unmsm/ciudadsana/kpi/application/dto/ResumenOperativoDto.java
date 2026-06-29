package pe.edu.unmsm.ciudadsana.kpi.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ResumenOperativoDto(
        UUID resumenId,
        UUID tenantId,
        UUID distritoIdExterno,
        LocalDate fecha,
        BigDecimal kmProgramados,
        BigDecimal kmRecorridos,
        BigDecimal toneladasRecolectadas,
        BigDecimal coberturaPorcentaje,
        int alertasRegistradas,
        int alertasAtendidas,
        BigDecimal tiempoRespuestaPromedioMin,
        Instant creadoEn
) {}
