package com.timbox.cancelacion;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        // Parametros generales para el sevicio
        String usuario = "AAA010101000";
        String contrasena = "h6584D56fVdBbSmmnB";

        // Cancelar Factura
        // Parametros para el servicio
        String rfc_emisor = "AAA010101AAA";
        String rfc_receptor = "IAD121214B34";

        String uuid = "752D5486-8C1E-4F1A-8C9A-C6C28DA02E78";
        String uuid2 = "DF7C62C2-A15A-4765-9034-EEDD8C9A9057";

        String total = "7261.60";
        String total2 = "1751.60";

        byte[] file_cer_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.cer.pem"));
        byte[] file_key_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.key.pem"));

        String content_cer_file = new String(file_cer_pem, "UTF8");
        String content_key_file = new String(file_key_pem, "UTF8");

        //Agregar N folios a cancelar
        List<folio> folios = new ArrayList<folio>();
        folios.add(new folio(uuid, rfc_receptor, total));
        folios.add(new folio(uuid2, rfc_receptor, total2));

        try {
            // Creacion del objeto Cancelacion
            Cancelacion cancelacion = new Cancelacion(usuario, contrasena, rfc_emisor, folios, content_cer_file, content_key_file);
            // Ejecucion del servicio
            String facturaCancelada = cancelacion.Cancelar();
            // Imprime la respuesta
            System.out.println("Comprabantes cancelados: \n");
            System.out.println(facturaCancelada);
        } catch (Exception exception) {
            throw exception;
        }

    }
}
