package pe.edu.unmsm.ciudadsana.operacion.application.query;
import java.util.UUID;
public record ListarDistritosQuery(UUID tenantId, int page, int size) {}
