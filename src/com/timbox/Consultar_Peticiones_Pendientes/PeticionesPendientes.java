/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultar_Peticiones_Pendientes;

import java.io.ByteArrayOutputStream;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.*;
/**
 *
 * @author timbox-07
 */
public class PeticionesPendientes {
    
    // URL del servicio
    static String URL = "https://staging.ws.timbox.com.mx/cancelacion/action";

    
    // Accion para consultar peticiones pendientes
    final static String ACCION = "consultar_peticiones_pendientes";
    // Propiedades
    private String usuario = "", contrasena = "", rfcReceptor = "", cer_pem = "", key_pem = "";

    public PeticionesPendientes(String usuarioValue, String contrasenaValue, String rfcReceptorValue, String cerFileValue, String keyFileValue) {
        usuario = usuarioValue;
        contrasena = contrasenaValue;
        rfcReceptor = rfcReceptorValue;
        cer_pem = cerFileValue;
        key_pem = keyFileValue;
    }

    public String ConsultaPeticiones() throws Exception {
        // Conexion SOAP
        String response = "";
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapResponse = messageFactory.createMessage();

        try {
            // Ejecucion del metodo para consultar peticiones pendientes
            soapResponse = soapConnection.call(peticionesPendientes(), URL);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            soapResponse.writeTo(outputStream);
            response = new String(outputStream.toByteArray());

        } catch (Exception ex) {
            throw ex;
        } finally {
            soapConnection.close();
        }

        return response;
    }

    private SOAPMessage peticionesPendientes() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        // Creacion de envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("urn", "urn:WashOut");
        envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");

        // Cuerpo del envelope
        SOAPBody soapBody = envelope.getBody();
        SOAPElement urn = soapBody.addChildElement(ACCION, "urn");

        // Parametros para el usuario
        SOAPElement usernameElement = urn.addChildElement("username");
        usernameElement.addTextNode(this.usuario);
        usernameElement.setAttribute("xsi:type", "xsd:string");

        // Parametros para la contrasena
        SOAPElement passwordElement = urn.addChildElement("password");
        passwordElement.addTextNode(this.contrasena);
        passwordElement.setAttribute("xsi:type", "xsd:string");

        // Parametros para el cert_pem
        SOAPElement rfcReceptorElement = urn.addChildElement("rfc_receptor");
        rfcReceptorElement.addTextNode(this.rfcReceptor);
        rfcReceptorElement.setAttribute("xsi:type", "xsd:string");

        // Parametros para el llave_pem
        // Parametros para el cert_pem
        SOAPElement pfxbase64Element = urn.addChildElement("cert_pem");
        pfxbase64Element.addTextNode(cer_pem);
        pfxbase64Element.setAttribute("xsi:type", "xsd:string");

        // Parametros para el llave_pem
        SOAPElement pfxpasswordElement = urn.addChildElement("llave_pem");
        pfxpasswordElement.addTextNode(key_pem);
        pfxpasswordElement.setAttribute("xsi:type", "xsd:string");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", ACCION);
        headers.setHeader("Content-Type", "application/xml");
        soapMessage.saveChanges();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapMessage.writeTo(out);

        return soapMessage;
    }
    
}
