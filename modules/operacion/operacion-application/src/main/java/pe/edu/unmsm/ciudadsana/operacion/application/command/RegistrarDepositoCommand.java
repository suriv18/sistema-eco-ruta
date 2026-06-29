package pe.edu.unmsm.ciudadsana.operacion.application.command;
import java.util.UUID;
public record RegistrarDepositoCommand(UUID tenantId, UUID distritoId, String nombre, String tipo) {}
