package com.timbox;

import javax.xml.soap.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Timbrado {
    // URL del servicio
    static String URL = "https://staging.ws.timbox.com.mx/timbrado/action";
    // Accion para el timbrado
    final static String ACCION = "timbrar_cfdi";
    // Propiedades
    private String usuario = "", contrasena = "", sxml = "";

    public Timbrado(String usuarioValue, String contrasenaValue, String documentoValue) {
        usuario = usuarioValue;
        contrasena = contrasenaValue;
        // Conversion del documento XML a base64
        byte[] bytes = documentoValue.getBytes(StandardCharsets.UTF_8);
        String encodedString = Base64.getEncoder().encodeToString(bytes);
        sxml = encodedString;
    }

    public SOAPMessage Timbrar() throws Exception {
        // Conexion SOAP
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapResponse = messageFactory.createMessage();
        try {
            // Ejecucion del metodo para timbrar_cfdi
            soapResponse = soapConnection.call(peticionTimbrado(), URL);
        } catch (Exception ex) {
            throw ex;
        } finally {
            soapConnection.close();
        }

        return soapResponse;
    }

    private SOAPMessage peticionTimbrado() throws Exception {
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
        usernameElement.addTextNode(usuario);
        usernameElement.setAttribute("xsi:type","xsd:string");
        // Parametros para la contrasena
        SOAPElement passwordElement = urn.addChildElement("password");
        passwordElement.addTextNode(contrasena);
        passwordElement.setAttribute("xsi:type","xsd:string");
        // Parametros para el xml
        SOAPElement sxmlElement = urn.addChildElement("sxml");
        sxmlElement.addTextNode(sxml);
        sxmlElement.setAttribute("xsi:type","xsd:string");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", ACCION);

        soapMessage.saveChanges();
        return soapMessage;
    }
}
