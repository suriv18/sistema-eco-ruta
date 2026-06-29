package pe.edu.unmsm.ciudadsana.operacion.application.dto;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
public record ContenedorResponseDto(UUID id, UUID tenantId, UUID zonaId, String codigo, BigDecimal capacidadM3, String estadoContenedor, Instant creadoEn) {}
