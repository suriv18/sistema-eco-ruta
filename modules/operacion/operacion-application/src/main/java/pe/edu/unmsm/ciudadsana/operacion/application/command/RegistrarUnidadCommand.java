package pe.edu.unmsm.ciudadsana.operacion.application.command;
import java.math.BigDecimal;
import java.util.UUID;
public record RegistrarUnidadCommand(UUID tenantId, String placa, String codigoInterno, String tipoUnidad, BigDecimal capacidadM3, BigDecimal capacidadKg) {}
