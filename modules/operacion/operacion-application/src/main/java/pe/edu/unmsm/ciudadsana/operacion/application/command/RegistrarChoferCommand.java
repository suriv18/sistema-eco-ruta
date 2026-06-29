package pe.edu.unmsm.ciudadsana.operacion.application.command;
import java.util.UUID;
public record RegistrarChoferCommand(UUID tenantId, String nombres, String apellidos, String dni, String licencia, String telefono) {}
