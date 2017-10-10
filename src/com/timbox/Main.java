package com.timbox;
import javax.xml.soap.SOAPMessage;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
        // Parametros generales para el sevicio
        String usuario = "AAA010101000";
        String contrasena = "h6584D56fVdBbSmmnB";

        //
        // Timbrar Factura
        //
        // Parametros para el servicio
        byte[] archivoXml = Files.readAllBytes(Paths.get("archivoXml.xml"));
        String xmlBase64 = Base64.getEncoder().encodeToString(archivoXml);

        try {
            // Creacion del objeto Timbrado
            Timbrado tim = new Timbrado(usuario, contrasena, xmlBase64);
            //Ejecucion del servicio
            String facturaTimbrada = tim.Timbrar();
            // Imprime la respuesta
            System.out.println("Comprobante timbrado: \n");
            System.out.print(facturaTimbrada);
        } catch (Exception exception) {
            throw exception;
        }

        //Cancelar Factura
        // Parametros para el servicio
        String rfcEmisorCancelacion = "AAA010101AAA";
        String[] uuidsCancelacion = { "E28DBCF2-F852-4B2F-8198-CD8383891EB0", "3CFF7200-0DE5-4BEE-AC22-AA2A49052FBC", "51408B33-FE29-47DA-9517-FBF420240FD3" };
        byte[] pfx = Files.readAllBytes(Paths.get("archivoPfx.pfx"));
        String pfxContrasena = "12345678a";
        // Conversion del PFX a base64
        String pfxBase64  = Base64.getEncoder().encodeToString(pfx);

        try {
            // Creacion del objeto Cancelacion
            Cancelacion cancelacion = new Cancelacion(usuario, contrasena, rfcEmisorCancelacion, uuidsCancelacion, pfxBase64, pfxContrasena);
            //Ejecucion del servicio
            String facturaCancelada = cancelacion.Cancelar();
            // Imprime la respuesta
            System.out.println("Comprabantes cancelados: \n");
            System.out.println(facturaCancelada);
        } catch (Exception exception) {
            throw exception;
        }
    }
}
