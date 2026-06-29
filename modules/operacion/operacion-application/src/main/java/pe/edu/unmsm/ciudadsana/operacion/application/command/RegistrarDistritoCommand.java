package pe.edu.unmsm.ciudadsana.operacion.application.command;
import java.util.UUID;
public record RegistrarDistritoCommand(UUID tenantId, String nombre, String ubigeo) {}
