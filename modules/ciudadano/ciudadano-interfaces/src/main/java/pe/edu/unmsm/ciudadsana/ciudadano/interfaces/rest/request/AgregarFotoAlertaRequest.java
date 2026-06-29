package pe.edu.unmsm.ciudadsana.ciudadano.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AgregarFotoAlertaRequest(
        @NotBlank String urlArchivo,
        @NotBlank @Size(max = 50) String tipoMime,
        Long tamanioBytes
) {}
