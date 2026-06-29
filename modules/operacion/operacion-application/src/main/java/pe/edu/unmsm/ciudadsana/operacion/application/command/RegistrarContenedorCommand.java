package pe.edu.unmsm.ciudadsana.operacion.application.command;
import java.math.BigDecimal;
import java.util.UUID;
public record RegistrarContenedorCommand(UUID tenantId, UUID zonaId, String codigo, BigDecimal capacidadM3) {}
