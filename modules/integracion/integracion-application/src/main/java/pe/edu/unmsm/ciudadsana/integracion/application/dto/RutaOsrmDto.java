package pe.edu.unmsm.ciudadsana.integracion.application.dto;

import java.util.List;

public record RutaOsrmDto(
        double distanciaM,
        double duracionS,
        List<double[]> geometria
) {}
