package pe.edu.unmsm.ciudadsana.ciudadano.application.dto;

import java.time.Instant;
import java.util.UUID;

public record ValidacionAlertaDto(
    UUID id,
    boolean esDuplicada,
    UUID alertaOriginalId,
    boolean dentroGeocerca,
    double scoreSpam,
    String resultado,
    Instant validadaEn
) {}
