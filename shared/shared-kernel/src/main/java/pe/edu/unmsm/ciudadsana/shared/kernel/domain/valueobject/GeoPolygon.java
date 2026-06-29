package pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.List;

public record GeoPolygon(List<GeoPoint> vertices) implements ValueObject {

    public GeoPolygon {
        if (vertices == null || vertices.size() < 4) {
            throw new IllegalArgumentException("GeoPolygon requiere al menos 4 puntos (3 únicos + cierre)");
        }
        GeoPoint primero = vertices.getFirst();
        GeoPoint ultimo = vertices.getLast();
        if (!primero.equals(ultimo)) {
            throw new IllegalArgumentException("GeoPolygon debe estar cerrado (primer punto == último punto)");
        }
        vertices = List.copyOf(vertices);
    }

    public static GeoPolygon of(List<GeoPoint> vertices) {
        return new GeoPolygon(vertices);
    }

    public int totalVertices() {
        return vertices.size();
    }
}
