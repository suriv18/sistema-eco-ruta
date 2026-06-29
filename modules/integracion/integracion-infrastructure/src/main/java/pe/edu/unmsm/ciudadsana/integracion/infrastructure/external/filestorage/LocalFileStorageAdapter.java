package pe.edu.unmsm.ciudadsana.integracion.infrastructure.external.filestorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.integracion.application.port.out.FileStoragePort;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class LocalFileStorageAdapter implements FileStoragePort {

    private static final Logger log = LoggerFactory.getLogger(LocalFileStorageAdapter.class);

    private final Path baseDir;

    public LocalFileStorageAdapter(
            @Value("${integracion.storage.base-dir:uploads}") String baseDir) {
        this.baseDir = Paths.get(baseDir);
    }

    @Override
    public Result<String> guardar(String nombreArchivo, byte[] contenido, String contentType) {
        try {
            String rutaRelativa = UUID.randomUUID() + "_" + nombreArchivo;
            Path destino = baseDir.resolve(rutaRelativa);
            Files.createDirectories(destino.getParent());
            Files.write(destino, contenido);
            return Result.success(rutaRelativa);
        } catch (IOException e) {
            log.error("Error al guardar archivo {}: {}", nombreArchivo, e.getMessage());
            return Result.failure(ErrorCode.ERROR_PERSISTENCIA, "No se pudo guardar el archivo: " + e.getMessage());
        }
    }

    @Override
    public Result<byte[]> obtener(String ruta) {
        try {
            Path path = baseDir.resolve(ruta);
            if (!Files.exists(path)) {
                return Result.failure(ErrorCode.RECURSO_NO_ENCONTRADO, "Archivo no encontrado: " + ruta);
            }
            return Result.success(Files.readAllBytes(path));
        } catch (IOException e) {
            log.error("Error al leer archivo {}: {}", ruta, e.getMessage());
            return Result.failure(ErrorCode.ERROR_PERSISTENCIA, "No se pudo leer el archivo: " + e.getMessage());
        }
    }

    @Override
    public Result<Void> eliminar(String ruta) {
        try {
            Path path = baseDir.resolve(ruta);
            Files.deleteIfExists(path);
            return Result.success(null);
        } catch (IOException e) {
            log.error("Error al eliminar archivo {}: {}", ruta, e.getMessage());
            return Result.failure(ErrorCode.ERROR_PERSISTENCIA, "No se pudo eliminar el archivo: " + e.getMessage());
        }
    }
}
