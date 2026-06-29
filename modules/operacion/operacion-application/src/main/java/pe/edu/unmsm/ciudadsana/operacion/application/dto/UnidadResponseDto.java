package pe.edu.unmsm.ciudadsana.operacion.application.dto;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
public record UnidadResponseDto(UUID id, UUID tenantId, String placa, String codigoInterno, String tipoUnidad, BigDecimal capacidadM3, BigDecimal capacidadKg, String estadoOperativo, Instant creadoEn) {}
