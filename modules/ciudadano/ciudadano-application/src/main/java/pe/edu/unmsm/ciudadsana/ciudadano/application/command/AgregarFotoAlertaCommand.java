package pe.edu.unmsm.ciudadsana.ciudadano.application.command;

import java.util.UUID;

public record AgregarFotoAlertaCommand(
    UUID alertaId,
    UUID tenantId,
    String urlArchivo,
    String tipoMime,
    Long tamanioBytes
) {}
