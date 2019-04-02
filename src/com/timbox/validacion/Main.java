package com.timbox.validacion;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        // Parametros generales para el sevicio
        String usuario = "AAA010101000";
        String contrasena = "h6584D56fVdBbSmmnB";

        // Validar CFDI
        // Parametros para el servicio
        byte[] archivoXml = Files.readAllBytes(Paths.get("ejemplo_cfdi_33.xml"));
        String xmlBase64 = Base64.getEncoder().encodeToString(archivoXml);

        //Agregar N Comprobantes a validar
        List<Comprobante> xmls = new ArrayList<Comprobante>();
        xmls.add(new Comprobante(xmlBase64,"1"));
        xmls.add(new Comprobante(xmlBase64,"2"));

        try {
            // Creacion del objeto Validacion
            Validacion validacion = new Validacion(usuario, contrasena, xmls);
            // Ejecucion del servicio
            String xmlValidados = validacion.Validar();
            // Imprime la respuesta
            System.out.println("Comprabantes Validados: \n");
            System.out.println(xmlValidados);
        } catch (Exception exception) {
            throw exception;
        }

    }
}
