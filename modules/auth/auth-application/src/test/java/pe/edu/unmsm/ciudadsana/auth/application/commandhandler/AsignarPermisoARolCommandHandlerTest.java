package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.command.AsignarPermisoARolCommand;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PermisoPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Permiso;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PermisoId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsignarPermisoARolCommandHandlerTest {

    @Mock
    RolPersistencePort rolPort;

    @Mock
    PermisoPersistencePort permisoPort;

    @InjectMocks
    AsignarPermisoARolCommandHandler handler;

    private static final UUID ROL_ID = UUID.randomUUID();
    private static final UUID PERMISO_ID = UUID.randomUUID();

    @Test
    void asignar_rolYPermisoExistentes_sinDuplicado_retornaSuccess() {
        Rol rol = Rol.reconstitute(RolId.of(ROL_ID), "ADMIN", "Admin", "desc", true);
        Permiso permiso = Permiso.reconstitute(PermisoId.of(PERMISO_ID), "LEER_REPORTE", "REPORTES", "desc");
        when(rolPort.findById(any())).thenReturn(Optional.of(rol));
        when(permisoPort.findById(any())).thenReturn(Optional.of(permiso));
        when(rolPort.tienePermiso(ROL_ID, PERMISO_ID)).thenReturn(false);

        Result<Void> result = handler.asignar(new AsignarPermisoARolCommand(ROL_ID, PERMISO_ID));

        assertThat(result.isSuccess()).isTrue();
        verify(rolPort).asignarPermiso(ROL_ID, PERMISO_ID);
    }

    @Test
    void asignar_rolNoExiste_retornaRolNoEncontrado() {
        when(rolPort.findById(any())).thenReturn(Optional.empty());

        Result<Void> result = handler.asignar(new AsignarPermisoARolCommand(ROL_ID, PERMISO_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.ROL_NO_ENCONTRADO);
    }

    @Test
    void asignar_permisoNoExiste_retornaPermisoNoEncontrado() {
        Rol rol = Rol.reconstitute(RolId.of(ROL_ID), "ADMIN", "Admin", "desc", true);
        when(rolPort.findById(any())).thenReturn(Optional.of(rol));
        when(permisoPort.findById(any())).thenReturn(Optional.empty());

        Result<Void> result = handler.asignar(new AsignarPermisoARolCommand(ROL_ID, PERMISO_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.PERMISO_NO_ENCONTRADO);
    }

    @Test
    void asignar_permisoYaAsignado_retornaDuplicado() {
        Rol rol = Rol.reconstitute(RolId.of(ROL_ID), "ADMIN", "Admin", "desc", true);
        Permiso permiso = Permiso.reconstitute(PermisoId.of(PERMISO_ID), "LEER_REPORTE", "REPORTES", "desc");
        when(rolPort.findById(any())).thenReturn(Optional.of(rol));
        when(permisoPort.findById(any())).thenReturn(Optional.of(permiso));
        when(rolPort.tienePermiso(ROL_ID, PERMISO_ID)).thenReturn(true);

        Result<Void> result = handler.asignar(new AsignarPermisoARolCommand(ROL_ID, PERMISO_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.PERMISO_DUPLICADO);
    }
}
