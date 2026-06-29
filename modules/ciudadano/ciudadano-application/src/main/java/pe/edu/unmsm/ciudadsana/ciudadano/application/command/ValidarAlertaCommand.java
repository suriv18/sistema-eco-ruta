package pe.edu.unmsm.ciudadsana.ciudadano.application.command;

import java.util.UUID;

public record ValidarAlertaCommand(
    UUID alertaId,
    UUID tenantId,
    boolean esDuplicada,
    UUID alertaOriginalId,
    boolean dentroGeocerca,
    double scoreSpam,
    String resultado
) {}
