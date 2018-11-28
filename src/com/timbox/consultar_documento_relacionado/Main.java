package com.timbox.consultar_documento_relacionado;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
    
        // Parametros generales para el sevicio
        String usuario = "AAA010101000";
        String contrasena = "h6584D56fVdBbSmmnB";

        // Consultar Documentos Relacionados
        // Parametros para el servicio
        String rfc_receptor = "AAA010101AAA";
        String uuid = "A2872717-52E1-4AC1-877A-A74102AB6EDD";

        byte[] file_cer_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.cer.pem"));
        byte[] file_key_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.key.pem"));

        String content_cer_file = new String(file_cer_pem, "UTF8");
        String content_key_file = new String(file_key_pem, "UTF8");

        try {
            // Creacion del objeto Documentos
            Relacionados documentos = new Relacionados(usuario, contrasena, uuid, rfc_receptor, content_cer_file, content_key_file);
            // Ejecucion del servicio
            String docs_relacionados = documentos.Consultar();
            // Imprime la respuesta
            System.out.println("Documentos Relacionados: \n");
            System.out.println(docs_relacionados);
        } catch (Exception exception) {
            throw exception;
        }

    }
}
