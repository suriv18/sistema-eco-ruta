package pe.edu.unmsm.ciudadsana.integracion.infrastructure.external.osrm;

import java.util.List;

record OsrmTableResponse(
        String code,
        List<List<Double>> durations,
        List<List<Double>> distances
) {}
