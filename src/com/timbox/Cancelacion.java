package com.timbox;

import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;

public class Cancelacion {
    // URL del servicio
    static String URL = "https://staging.ws.timbox.com.mx/timbrado_cfdi33/wsdl";
    // Accion para el timbrado
    final static String ACCION = "cancelar_cfdi";
    // Propiedades
    private String usuario = "", contrasena = "", sxml = "", rfcEmisor = "", pfx = "", pfxContrasena = "";
    private String[] uuids;

    public Cancelacion(String usuarioValue, String contrasenaValue, String rfcEmisorValue, String[] uuidsValue, String pfxValue, String pfxContrasenaValue) {
        usuario = usuarioValue;
        contrasena = contrasenaValue;
        rfcEmisor = rfcEmisorValue;
        uuids = uuidsValue;
        pfx = pfxValue;
        pfxContrasena = pfxContrasenaValue;
    }

    public String Cancelar() throws Exception {
        // Conexion SOAP
        String response = "";
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapResponse = messageFactory.createMessage();
        try {
            // Ejecucion del metodo para cancelar_cfdi
            soapResponse = soapConnection.call(peticionCancelacion(), URL);
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

    private SOAPMessage peticionCancelacion() throws Exception {
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
        usernameElement.setAttribute("xsi:type", "xsd:string");
        // Parametros para la contrasena
        SOAPElement passwordElement = urn.addChildElement("password");
        passwordElement.addTextNode(contrasena);
        passwordElement.setAttribute("xsi:type", "xsd:string");
        // Parametros para el rfc
        SOAPElement rfcElement = urn.addChildElement("rfcemisor");
        rfcElement.addTextNode(rfcEmisor);
        rfcElement.setAttribute("xsi:type", "xsd:string");
        // Parametros para uuids
        SOAPElement uuidsElement = urn.addChildElement("uuids");
        uuidsElement.setAttribute("xsi:type", "urn:uuid");
        // Creacion de uuids con string array
        for (String uuid: uuids) {
            SOAPElement uuidElement = uuidsElement.addChildElement("uuid");
            uuidElement.addTextNode(uuid);
            uuidElement.setAttribute("xsi:type", "urn:string");
        }
        // Parametros para el pfxbase64
        SOAPElement pfxbase64Element = urn.addChildElement("pfxbase64");
        pfxbase64Element.addTextNode(pfx);
        pfxbase64Element.setAttribute("xsi:type", "xsd:string");
        // Parametros para el pfxpassword
        SOAPElement pfxpasswordElement = urn.addChildElement("pfxpassword");
        pfxpasswordElement.addTextNode(pfxContrasena);
        pfxpasswordElement.setAttribute("xsi:type", "xsd:string");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", ACCION);

        soapMessage.saveChanges();
        return soapMessage;
    }
}
