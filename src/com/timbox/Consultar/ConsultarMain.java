/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultar;

/**
 *
 * @author timbox-07
 */
public class ConsultarMain {
    
    public static void Consultar() throws Exception{
    
        // Parametros generales para el sevicio
        String usuario = "";
        String contrasena = "";

        // Consultar Estatus
        // Parametros para el servicio
        String rfc_emisor = "EKU9003173C9";
        String rfc_receptor = "IAD121214B34";

        String uuid = "752D5486-8C1E-4F1A-8C9A-C6C28DA02E78";
        String total = "7261.60";

        try {
            // Creacion del objeto Consultar
            Consultar consulta = new Consultar(usuario, contrasena, uuid, rfc_emisor, rfc_receptor, total);
            // Ejecucion del servicio
            String consulta_estatus = consulta.Consulta();
            // Imprime la respuesta
            System.out.println("Estado: \n");
            System.out.println(consulta_estatus);
        } catch (Exception exception) {
            throw exception;
        }
    
    }
    
}
