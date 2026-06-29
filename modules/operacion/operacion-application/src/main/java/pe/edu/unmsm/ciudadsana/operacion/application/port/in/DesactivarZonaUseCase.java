package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.command.DesactivarZonaCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface DesactivarZonaUseCase {
    Result<Void> desactivar(DesactivarZonaCommand command);
}
