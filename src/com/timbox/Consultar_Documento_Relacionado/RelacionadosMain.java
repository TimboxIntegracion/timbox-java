/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultar_Documento_Relacionado;

import java.nio.file.Files;
import java.nio.file.Paths;
/**
 *
 * @author timbox-07
 */
public class RelacionadosMain {
    
    public static void DocumentoRelacionados() throws Exception{
    
        // Parametros generales para el sevicio
        String usuario = "";
        String contrasena = "";

        // Consultar Documentos Relacionados
        // Parametros para el servicio
        String rfc_receptor = "EKU9003173C9";
        String uuid = "A2872717-52E1-4AC1-877A-A74102AB6EDD";

        byte[] file_cer_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.cer.pem"));
        byte[] file_key_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.key.pem"));

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
