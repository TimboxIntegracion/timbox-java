package com.timbox.consultar_peticiones_pendientes;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {

        // Parametros generales para el sevicio
        String usuario = "AAA010101000";
        String contrasena = "h6584D56fVdBbSmmnB";

        // Consultar Peticiones Pendientes
        // Parametros para el servicio
        String rfc_receptor = "AAA010101AAA";

        byte[] file_cer_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.cer.pem"));
        byte[] file_key_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.key.pem"));

        String content_cer_file = new String(file_cer_pem, "UTF8");
        String content_key_file = new String(file_key_pem, "UTF8");

        try {
            // Creacion del objeto PeticionesPendientes
            PeticionesPendientes peticiones = new PeticionesPendientes(usuario, contrasena, rfc_receptor, content_cer_file, content_key_file);
            // Ejecucion del servicio
            String Comprobantes = peticiones.ConsultaPeticiones();
            // Imprime la respuesta
            System.out.println("Peticiones pendientes: \n");
            System.out.println(Comprobantes);
        } catch (Exception exception) {
            throw exception;
        }
    }

}
