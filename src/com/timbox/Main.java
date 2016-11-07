package com.timbox;
import javax.xml.soap.SOAPMessage;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Main {

    public static void main(String[] args) throws Exception {
        //Timbrar Factura
        // Parametros para el servicio
        String usuarioTimbrado = "usuario_prueba";
        String contrasenaTimbrado = "contrasena_prueba";
        String documentoTimbrado = "xml";

        try {
            // Creacion del objeto Timbrado
            Timbrado timbrado = new Timbrado(usuarioTimbrado, contrasenaTimbrado, documentoTimbrado);
            //Ejecucion del servicio
            SOAPMessage soapResponse = timbrado.Timbrar();
            // Imprime la respuesta
            System.out.print("Response SOAP Message:");
            soapResponse.writeTo(System.out);
        } catch (Exception exception) {
            throw exception;
        }

        //Cancelar Factura
        // Parametros para el servicio
        String usuarioCancelacion = "usuario_prueba";
        String contrasenaCancelacion = "contrasena_prueba";
        String rfcEmisorCancelacion = "RFCPRUEBA";
        String[] uuidsCancelacion = { "UUID1", "UUID2", "UUID3" };
        String pfxCancelacion = "valor_PFX";
        String pfxContrasenaCancelacion = "contrasena_prueba";
        // Conversion del PFX a base64
        byte[] bytes = pfxCancelacion.getBytes(StandardCharsets.UTF_8);
        String encodedPfxCancelacion  = Base64.getEncoder().encodeToString(bytes);

        try {
            // Creacion del objeto Cancelacion
            Cancelacion timbrado = new Cancelacion(usuarioCancelacion, contrasenaCancelacion, rfcEmisorCancelacion, uuidsCancelacion, encodedPfxCancelacion, pfxContrasenaCancelacion);
            //Ejecucion del servicio
            SOAPMessage soapResponse = timbrado.Cancelar();
            // Imprime la respuesta
            System.out.print("Response SOAP Message:");
            soapResponse.writeTo(System.out);
        } catch (Exception exception) {
            throw exception;
        }
    }
}
