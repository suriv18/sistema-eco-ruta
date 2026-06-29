package pe.edu.unmsm.ciudadsana.operacion.application.command;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
public record CrearTurnoCommand(UUID tenantId, UUID unidadId, UUID choferId, UUID distritoId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String tipoTurno) {}
