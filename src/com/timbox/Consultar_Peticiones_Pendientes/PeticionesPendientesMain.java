/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultar_Peticiones_Pendientes;

import java.nio.file.Files;
import java.nio.file.Paths;
/**
 *
 * @author timbox-07
 */
public class PeticionesPendientesMain {
    
    public static void PeticionesPendientes() throws Exception{
    
        // Parametros generales para el sevicio
        String usuario = "";
        String contrasena = "";

        // Consultar Peticiones Pendientes
        // Parametros para el servicio
        String rfc_receptor = "EKU9003173C9";

        byte[] file_cer_pem = Files.readAllBytes(Paths.get("path\\EKU9003173C9.cer.pem"));
        byte[] file_key_pem = Files.readAllBytes(Paths.get("path\\EKU9003173C9.key.pem"));

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
