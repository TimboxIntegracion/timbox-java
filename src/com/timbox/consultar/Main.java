package com.timbox.consultar;

public class Main {

    public static void main(String[] args) throws Exception {

        // Parametros generales para el sevicio
        String usuario = "AAA010101000";
        String contrasena = "h6584D56fVdBbSmmnB";

        // Consultar Estatus
        // Parametros para el servicio
        String rfc_emisor = "AAA010101AAA";
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
