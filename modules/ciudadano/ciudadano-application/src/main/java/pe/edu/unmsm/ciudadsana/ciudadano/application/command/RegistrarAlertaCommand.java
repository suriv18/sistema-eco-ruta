package pe.edu.unmsm.ciudadsana.ciudadano.application.command;

import java.util.UUID;

public record RegistrarAlertaCommand(
    UUID tenantId,
    UUID ciudadanoId,
    UUID distritoExternoId,
    UUID zonaExternoId,
    String titulo,
    String descripcion,
    double latitud,
    double longitud,
    String volumenEstimado,
    String nivelCriticidad,
    String fuente
) {}
