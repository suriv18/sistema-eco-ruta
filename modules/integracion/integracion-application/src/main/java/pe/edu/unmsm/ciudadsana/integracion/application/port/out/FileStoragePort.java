package pe.edu.unmsm.ciudadsana.integracion.application.port.out;

import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface FileStoragePort {

    Result<String> guardar(String nombreArchivo, byte[] contenido, String contentType);

    Result<byte[]> obtener(String ruta);

    Result<Void> eliminar(String ruta);
}
