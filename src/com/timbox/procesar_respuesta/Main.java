package com.timbox.procesar_respuesta;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        // Parametros generales para el sevicio
        String usuario = "AAA010101000";
        String contrasena = "h6584D56fVdBbSmmnB";

        // Parametros para el servicio
        String rfc_emisor = "AAA010101AAA";
        String rfc_receptor = "AAA010101AAA";
        String uuid = "752D5486-8C1E-4F1A-8C9A-C6C28DA02E78";
        String total = "7261.60";

        // A(Aceptar la solicitud), R(Rechazar la solicitud)
        String respuesta = "A";

        byte[] file_cer_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.cer.pem"));
        byte[] file_key_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.key.pem"));

        String content_cer_file = new String(file_cer_pem, "UTF8");
        String content_key_file = new String(file_key_pem, "UTF8");

        // Agregar N foliosRespuesras a procesar
        List<foliosRespuestas> foliosrespuestas = new ArrayList();
        foliosrespuestas.add(new foliosRespuestas(uuid, rfc_emisor, total, respuesta));

        try {
            // Creacion del objeto Procesar
            Procesar procesar = new Procesar(usuario, contrasena, rfc_receptor, foliosrespuestas, content_cer_file, content_key_file);
            //Ejecucion del servicio
            String procesar_respuestas = procesar.ProcesarRespuestas();
            // Imprime la respuesta
            System.out.println("Respuestas procesadas: \n");
            System.out.println(procesar_respuestas);
        } catch (Exception exception) {
            throw exception;
        }
    }
}
