/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cancelacion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
/**
 *
 * @author timbox-07
 */
public class CancelacionMain {
    
    public static void Cancelacion() throws Exception{
        // Parametros generales para el sevicio
        String usuario = "";
        String contrasena = "";

        // Cancelar Factura
        // Parametros para el servicio
        String rfc_emisor = "EKU9003173C9";
        String rfc_receptor = "XAXX010101000";

        String uuid = "1FED34D7-A1FF-4890-85DA-1C789EBB2724";

        String total = "809.68";
        
        String motivo = "02";
        
        String folio_sustituto = "";

        byte[] file_cer_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.cer.pem"));
        byte[] file_key_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.key.pem"));

        String content_cer_file = new String(file_cer_pem, "UTF8");
        String content_key_file = new String(file_key_pem, "UTF8");

        //Agregar N folios a cancelar
        List<folio> folios = new ArrayList<folio>();
        folios.add(new folio(uuid, rfc_receptor, total,motivo,folio_sustituto));

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
