package com.timbox;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.soap.SOAPMessage;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Main {

    public static void main(String[] args) throws Exception {
        // Parametros generales para el sevicio
        String usuario = "AAA010101000";
        String contrasena = "h6584D56fVdBbSmmnB";

        //
        // Timbrar Factura
        //
        // Parametros para el servicio
        sellarXml("archivoXml.xml");
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
    
    public static void sellarXml(String xmlFile) throws Exception{
        File inputFile = new File(xmlFile);
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(inputFile);
        //Obtener la fecha de ahorita y remplazarla en el atributo fecha
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        timeStamp = timeStamp.replace('_','T');        
        document.getRootElement().setAttribute("Fecha", timeStamp);
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(document, new FileOutputStream(xmlFile));
        
        //Crear la cadena original
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt;
        xslt = new StreamSource(new File("cadenaoriginal_3_3.xslt"));
        Transformer transformer = factory.newTransformer(xslt);
        Source text;
        text = new StreamSource(new File(xmlFile));
        transformer.transform(text, new StreamResult(new File("cadena_original.txt")));
        
        //Crear digestion
        String cmd = "openssl dgst -sha256 -sign CSD01_AAA010101AAA.key.pem -out digest.txt cadena_original.txt";
        String[] cmdArray = cmd.split(" ");
        Process p;
        Runtime r=Runtime.getRuntime();
        p=r.exec(cmd);
        
        //Generar Sello
        cmd = "openssl enc -in digest.txt -out sello.txt -base64 -A -K CSD01_AAA010101AAA.key.pem";
        cmdArray = cmd.split(" ");
        p=r.exec(cmd);
        
        inputFile = new File(xmlFile);
        document = saxBuilder.build(inputFile);
        String sello = new Scanner(new File("sello.txt")).useDelimiter("\\Z").next();
        document.getRootElement().setAttribute("Sello", sello);
        xmlOutputter.output(document, new FileOutputStream(xmlFile));
    }
}
