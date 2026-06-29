package pe.edu.unmsm.ciudadsana.integracion.application.dto;

import java.util.List;

public record MatrizCostosDto(
        List<List<Double>> tiemposS,
        List<List<Double>> distanciasM
) {}
