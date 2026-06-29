package pe.edu.unmsm.ciudadsana.operacion.application.dto;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
public record TurnoResponseDto(UUID id, UUID tenantId, UUID unidadId, UUID choferId, UUID distritoId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String tipoTurno, String estado, Instant creadoEn) {}
