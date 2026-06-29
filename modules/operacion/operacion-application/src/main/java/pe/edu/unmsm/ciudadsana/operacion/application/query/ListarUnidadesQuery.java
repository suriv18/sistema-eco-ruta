package pe.edu.unmsm.ciudadsana.operacion.application.query;
import java.util.UUID;
public record ListarUnidadesQuery(UUID tenantId, int page, int size) {}
