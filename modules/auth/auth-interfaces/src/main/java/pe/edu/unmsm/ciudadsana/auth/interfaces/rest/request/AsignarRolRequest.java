package pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AsignarRolRequest(@NotNull UUID rolId) {}
