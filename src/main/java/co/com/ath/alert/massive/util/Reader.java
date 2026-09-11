package co.com.ath.alert.massive.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author lmeza Clase que permite leer linea por linea el archivo capturado
 */
public class Reader {

	private static volatile Reader instance = null;
    private static final Logger log = LoggerFactory.getLogger(Reader.class);

    // Private constructor to prevent instantiation
    private Reader() {
    }

    /**
     * Metodo para obtener la instancia singleton de Reader.
     *
     * @return instancia singleton de Reader
     */
    public static Reader getInstance() {
        if (instance == null) {
            synchronized (Reader.class) {
                if (instance == null) {
                    instance = new Reader();
                }
            }
        }
        return instance;
    }

    /**
     * Metodo que permite leer el archivo.
     *
     * @param filePath Ruta del archivo a leer
     * @return Lista de líneas del archivo
     * @throws IOException Si ocurre un error al leer el archivo
     */
    public List<String> loadTextFile(String filePath) throws IOException {
        log.info("Ruta del archivo capturada desde clase: {}", filePath);

        List<String> fileData = new ArrayList<>();
        try (FileInputStream fileInput = new FileInputStream(filePath);
             InputStreamReader isr = new InputStreamReader(fileInput);
             BufferedReader br = new BufferedReader(isr)) {

            String line;
            while ((line = br.readLine()) != null) {
                fileData.add(line);
            }
        }

        return fileData;
    }

    /**
     * Metodo para eliminar archivos ya procesados.
     *
     * @param filePath Ruta del archivo a eliminar
     * @return true si el archivo fue eliminado con éxito, false en caso contrario
     */
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                log.info("Archivo eliminado exitosamente: {}", filePath);
                return true;
            } else {
                log.warn("No se pudo eliminar el archivo: {}", filePath);
            }
        } else {
            log.warn("El archivo no existe: {}", filePath);
        }
        return false;
    }

    /**
     * Metodo que permite validar si existe el archivo.
     *
     * @param filePath Ruta del archivo a validar
     * @return true si el archivo existe, false en caso contrario
     */
    public boolean fileExist(String filePath) {
        File file = new File(filePath);       
        if (file.exists()) {
            log.info("Se encontró el siguiente archivo: {}", filePath);
            return true;
        } else {
            log.info("No se encontró ningún archivo con la ruta especificada: {}", filePath);
            return false;
        }
    }

}
