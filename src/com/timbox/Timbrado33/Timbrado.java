/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Timbrado;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author flavio
 */
public class Timbrado {
     // URL del servicio
    static String URL = "https://staging.ws.timbox.com.mx/timbrado_cfdi33/action";

    // Accion para el timbrado
    final static String ACCION = "timbrar_cfdi";
    // Propiedades
    private String usuario = "", contrasena = "", sxml = "";

    public Timbrado(String usuarioValue, String contrasenaValue, String documentoValue) {
        usuario = usuarioValue;
        contrasena = contrasenaValue;
        sxml = documentoValue;
    }
    
    
     public String Timbrar() throws Exception {
        
        SOAPConnection soapConnection = null;
        String response = "";
        
        try {
            // Conexion SOAP
            //SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            //soapConnection = soapConnectionFactory.createConnection();
            soapConnection = SOAPConnectionFactory.newInstance().createConnection();
            MessageFactory messageFactory = MessageFactory.newInstance();
            //SOAPMessage soapResponse = messageFactory.createMessage();
            // Ejecucion del metodo para timbrar_cfdi
            SOAPMessage soapResponse = null;
            try {
                soapResponse = soapConnection.call( peticionTimbrado(), URL);
            } catch (SOAPException e) {
                System.out.println(e.getMessage().toString());
                //e.printStackTrace();
                //System.out.println(peticionTimbrado());
                System.out.println(e.getCause().toString());
                soapResponse = messageFactory.createMessage();
            }
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            soapResponse.writeTo(outputStream);
            response = new String(outputStream.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            soapConnection.close();
        }

        return response;
    }

    private SOAPMessage peticionTimbrado() throws Exception {
        //MessageFactory messageFactory = MessageFactory.newInstance();
        //SOAPMessage soapMessage = messageFactory.createMessage();
        
        SOAPMessage soapMessage = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL).createMessage();
        
        //Soap Envelope
        SOAPPart soapPart = soapMessage.getSOAPPart();
        //String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"></soapenv:Envelope>";
        //soapPart.setContent(new StreamSource(new ByteArrayInputStream(soap.getBytes("utf-8"))));

        // Creacion de envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        //SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
        envelope.addNamespaceDeclaration("urn", "urn:WashOut");
        envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance"); 
        envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
        envelope.addNamespaceDeclaration("soapenv", "http://schemas.xmlsoap.org/soap/encoding/");
        
        // Cuerpo del envelope
        SOAPBody soapBody = envelope.getBody();
        SOAPElement urn = soapBody.addChildElement(ACCION, "urn");
        urn.setAttribute("soapenv:encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");
        
        // Parametros para el usuario
        SOAPElement usernameElement = urn.addChildElement("username");
        usernameElement.addTextNode(usuario);
        usernameElement.setAttribute("xsi:type", "xsd:string");
        // Parametros para la contrasena
        SOAPElement passwordElement = urn.addChildElement("password");
        passwordElement.addTextNode(contrasena);
        passwordElement.setAttribute("xsi:type", "xsd:string");
        // Parametros para el xml
        SOAPElement sxmlElement = urn.addChildElement("sxml");
        sxmlElement.addTextNode(sxml);
        sxmlElement.setAttribute("xsi:type", "xsd:string");
        
        try {
            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.setHeader("SOAPAction", ACCION);
        } catch (Exception e) {
            e.printStackTrace();
        }

        soapMessage.saveChanges();
 
        return soapMessage;
    }
    
}
