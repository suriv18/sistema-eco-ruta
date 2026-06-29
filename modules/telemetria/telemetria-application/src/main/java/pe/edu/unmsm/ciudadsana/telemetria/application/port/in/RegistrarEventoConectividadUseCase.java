package pe.edu.unmsm.ciudadsana.telemetria.application.port.in;

import pe.edu.unmsm.ciudadsana.telemetria.application.command.RegistrarEventoConectividadCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.EventoConectividadResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface RegistrarEventoConectividadUseCase {
    Result<EventoConectividadResponseDto> registrar(RegistrarEventoConectividadCommand command);
}
