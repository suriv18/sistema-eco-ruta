package pe.edu.unmsm.ciudadsana.integracion.infrastructure.external.osrm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

record OsrmRouteResponse(
        String code,
        List<Route> routes
) {
    record Route(
            double distance,
            double duration,
            Geometry geometry
    ) {}

    record Geometry(
            String type,
            List<List<Double>> coordinates
    ) {}
}
