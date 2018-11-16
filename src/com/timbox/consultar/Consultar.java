package com.timbox.consultar;

import java.io.ByteArrayOutputStream;
import javax.xml.soap.*;

public class Consultar {

    // URL del servicio
    static String URL = "https://staging.ws.timbox.com.mx/cancelacion/action";
    // Accion para consultar status
    final static String ACCION = "consultar_estatus";
    // Propiedades
    private String usuario = "", contrasena = "", rfcEmisor = "", rfcReceptor = "", uuid = "", total = "";

    public Consultar(String usuarioValue, String contrasenaValue, String uuidValue, String rfcEmisorValue, String rfcReceptorValue, String totalValue) {
        usuario = usuarioValue;
        contrasena = contrasenaValue;
        uuid = uuidValue;
        rfcEmisor = rfcEmisorValue;
        rfcReceptor = rfcReceptorValue;
        total = totalValue;
    }

    public String Consulta() throws Exception {
        // Conexion SOAP
        String response = "";
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapResponse = messageFactory.createMessage();

        try {
            // Ejecucion del metodo para consultar estatus
            soapResponse = soapConnection.call(peticionConsultar(), URL);

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

    private SOAPMessage peticionConsultar() throws Exception {
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

        // Parametros para la contrasena
        SOAPElement uuidElement = urn.addChildElement("uuid");
        uuidElement.addTextNode(uuid);
        uuidElement.setAttribute("xsi:type", "xsd:string");

        // Parametros para el rfc_emisor
        SOAPElement rfcEmisorElement = urn.addChildElement("rfc_emisor");
        rfcEmisorElement.addTextNode(rfcEmisor);
        rfcEmisorElement.setAttribute("xsi:type", "xsd:string");

        // Parametros para el cert_pem
        SOAPElement rfcReceptorElement = urn.addChildElement("rfc_receptor");
        rfcReceptorElement.addTextNode(rfcReceptor);
        rfcReceptorElement.setAttribute("xsi:type", "xsd:string");

        // Parametros para el llave_pem
        SOAPElement totalElement = urn.addChildElement("total");
        totalElement.addTextNode(total);
        totalElement.setAttribute("xsi:type", "xsd:string");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", ACCION);

        soapMessage.saveChanges();

        return soapMessage;
    }
}
