package pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AsignarPermisoRequest(@NotNull UUID permisoId) {}
