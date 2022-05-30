/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Timbrado40;

import Timbrado40.Timbrado40;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author 
 */
public class Timbrado40Main {
    public static void Timbrado40() throws Exception{
        String path = "src/Documentos/";
        
        // Parametros generales para el sevicio
        String usuario = "";
        String contrasena = "";

        // Timbrar Factura
        //Actualizar sello en XML antes de mandar
        generar_sello(path + "ejemplo_cfdi_40.xml");
        Path pathyofile = Paths.get(path + "ejemplo_cfdi_40.xml");
        byte[] archivoXml = Files.readAllBytes(pathyofile.toAbsolutePath());
        String xmlBase64 = Base64.getEncoder().encodeToString(archivoXml);
        try {
            // Creacion del objeto Timbrado
            Timbrado40 tim = new Timbrado40(usuario, contrasena, xmlBase64);
            //Ejecucion del servicio
            String facturaTimbrada = tim.Timbrar();
            // Imprime la respuesta
            System.out.println("Comprobante timbrado: \n");
            System.out.print(facturaTimbrada);
        } catch (Exception e) {
            System.err.println("Eror" + e);
        }
    }
    
    public static String searchCsvLine(int searchColumnIndex, String searchString) throws IOException {
        String path = "src/Documentos/";
        String resultRow = null;
        BufferedReader br = new BufferedReader(new FileReader(path + "cat_postal_code.csv"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            if (values[searchColumnIndex].equals(searchString)) {
                resultRow = line;
                break;
            }
        }
        br.close();
        return resultRow;
    }
    
    public static void generar_sello(String xmlFile) throws IOException, Exception {
        String path = "src/Documentos/";
        File inputFile = new File(xmlFile);
        SAXBuilder saxBuilder = new SAXBuilder();

        Document document = saxBuilder.build(inputFile);
        String lugarExpedicion = document.getRootElement().getAttributeValue("LugarExpedicion");
        String postalCode = searchCsvLine(1, lugarExpedicion);
        Calendar calendar = new GregorianCalendar();
        TimeZone tz = TimeZone.getTimeZone(postalCode.split(",")[6]);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        //Obtener zona horaria de Lugar Expedicion
        formatter.setTimeZone(TimeZone.getTimeZone(tz.getID()));
        //Obtener la fecha actual y remplazarla en el atributo fecha
        String dateNow = formatter.format(calendar.getTime());
        String timeStamp = dateNow.replace('_', 'T');
        document.getRootElement().setAttribute("Fecha", timeStamp);
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(document, new FileOutputStream(xmlFile));

        //Crear la cadena original
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt;
        xslt = new StreamSource(new File(path + "cadenaoriginal_4_0.xslt"));
        Transformer transformer = factory.newTransformer(xslt);
        Source text  = new StreamSource(new File(xmlFile));
        transformer.transform(text, new StreamResult(new File(path +"cadena_original40.txt")));
        
        String[] cmdArray = null;
        //Crear digestion
        String cmd = "openssl dgst -sha256 -sign src/Certificados/XIA190128J61.key.pem -out src/Documentos/digest40.txt src/Documentos/cadena_original40.txt";        
        cmdArray = cmd.split(" ");
        Process p;
        Runtime r = Runtime.getRuntime();
        p = r.exec(cmd);

        p.waitFor();
        //Generar Sello
        cmd = "openssl enc -in src/Documentos/digest40.txt -out src/Documentos/sello40.txt -base64 -A -K src/Certificados/XIA190128J61.key.pem";
        cmdArray = cmd.split(" ");
        p = r.exec(cmd);

        p.waitFor();
        
        //Actualizar sello en el comprobante(xml)
        inputFile = new File(xmlFile);
        document = saxBuilder.build(inputFile);
        String sello = " ";
        try {
            sello = new Scanner(new File(path + "sello40.txt")).useDelimiter("\\Z").next();
            System.out.println("Se escribio correctamente el sello");
        } catch (Exception e) {
            System.out.println("Error al escribir el sello");
            //e.printStackTrace();
        }
        
        document.getRootElement().setAttribute("Sello", sello);
        xmlOutputter.output(document, new FileOutputStream(xmlFile));
    }
}

